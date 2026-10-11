package Daedalus.Combat;

import Daedalus.Ability.*;
import Daedalus.Engine.*;
import Daedalus.Actor.*;
import Daedalus.Zone.*;
import Daedalus.GUI.*;
import java.util.*;

public class CombatManager implements CombatConstants, AbilityConstants, ZoneConstants
{
   private static int applyAttack(Actor attacker, Actor defender, Attack attack, Coord attackOrigin)
   {
      Damage rolledDamage = attack.rollDamage();
      double dropoffMultiplier = getDamageDropoffMultiplier(attack, attacker, defender);
      int damageDealt = defender.applyDamage(attack.rollDamage(), dropoffMultiplier);
      int knockback = rolledDamage.getKnockback(dropoffMultiplier);
      if(knockback > 0)
      {
         if(attack.getTargetingType() == TargetingType.BLAST &&
            defender.getTileLoc().equals(attackOrigin))
         {
            attackOrigin = attacker.getTileLoc();
         }
         defender.setKnockback(knockback, Direction.getDirectionTo(attackOrigin, defender.getTileLoc()));
      }
      if(attack.getStatusEffect() != null)
      {
         defender.addStatusEffect(attack.getStatusEffect().copy());
      }
      return damageDealt;
   }
   
   // primary exposed method
   public static void resolveAbility(Actor attacker, Ability ability, Coord targetLoc, int rateOfFire)
   {
      Attack attack = null;
      if(ability instanceof Attack)
         attack = (Attack)ability;
      Vector<Coord> affectedList = ability.getAffectedTiles(attacker.getTileLoc(), targetLoc);
      Vector<Actor> defenderList = new Vector<Actor>();
      Coord abilityOrigin = attacker.getTileLoc();
      if(ability.getTargetingType() == TargetingType.BLAST)
         abilityOrigin = EngineTools.getBlastOrigin(attacker.getTileLoc(), targetLoc, ability.getRange());
      
      // add arc targets
      Vector<Actor> arcList = new Vector<Actor>();
      if(ability.getSpecialEffect() == SpecialEffect.ARC)
      {
         for(Coord targetTile: affectedList)
         {
            if(Game.isActorAt(targetTile))
            {
               for(int i = 0; i < rateOfFire; i++)
               {
                  if(RNG.nextDouble() < attack.getProcChance())
                  {
                     Actor arcTarget = getArcTarget(attacker, Game.getActorAt(targetTile), ability.getRange() / 2);
                     if(arcTarget != null)
                     {
                        arcList.add(arcTarget);
                     }
                  }
               }
            }
         }
         for(Actor arcTarget: arcList)
            if(!EngineTools.listContains(affectedList, arcTarget.getTileLoc()))
               affectedList.add(arcTarget.getTileLoc());
      }
      
      // process affected tiles
      for(int i = 0; i < affectedList.size(); i++)
      {
         Coord affectedTile = affectedList.elementAt(i);
         if(ability instanceof Attack && attack.isMelee())
            AnimationScriptFactory.addMeleeGroundFlash(affectedTile);
         else
            AnimationScriptFactory.addGroundFlash(affectedTile);
         if(Game.isActorAt(affectedTile))
            defenderList.add(Game.getActorAt(affectedTile));
         if(ability instanceof Attack)
            Game.getCurMap().attackTile(affectedTile, attack.isHeavy());
         if(ability.startsFires())
         {
            // point attacks only start fires if there's no actor in the tile
            if(ability.getTargetingType() != TargetingType.POINT || !Game.isActorAt(affectedTile))
            {
               if(Game.getCurMap().isValidLocationForFire(affectedTile) && RNG.nextDouble() < ability.getProcChance())
               {
                  int fireDuration = DEFAULT_FIRE_DURATION + RNG.nextInt(DEFAULT_FIRE_RANDOM_DURATION);
                  Game.getCurMap().setFireAt(new Fire(fireDuration), affectedTile);
               }
            }
         }
      }
      Direction dir = Direction.getDirectionTo(attacker.getTileLoc(), targetLoc);
      AnimationScript as;
      if(ability instanceof Attack && Game.shouldReport(attacker, defenderList))
      {
         if(attack.isMelee())
         {
            as = AnimationScriptFactory.getMeleeAttack(attacker, dir);
            AnimationManager.addLocking(as);
            // only do screenshake if you hit something
            if(defenderList.size() > 0)
               AnimationManager.setScreenShake(AnimationScriptFactory.MELEE_IMPACT_DELAY);
         }
         else
         {
            as = AnimationScriptFactory.getRecoil(attacker, dir);
            AnimationManager.addLocking(as);
            AnimationManager.setScreenRumble();
         }
      }
      if(ability.getImpactEffect() != null)
      {
         Coord impactLoc = affectedList.elementAt(0);
         if(ability.getTargetingType() == TargetingType.BLAST)
            impactLoc = abilityOrigin;
         if(ability.getImpactEffect() == ImpactEffect.EXPLOSION)
         {
            AnimationScriptFactory.addExplosion(impactLoc);
         }
         if(ability.getImpactEffect() == ImpactEffect.SPLASH)
         {
            AnimationScriptFactory.addSplash(impactLoc, GUIConstants.LIGHT_GREEN);
         }
      }
      for(int i = 0; i < defenderList.size(); i++)
      {
         Actor defender = defenderList.elementAt(i);
         int damageCount = 0;
         if(ability instanceof Attack)
         {
            // applyAttack also applies the status effect
            if(!arcList.contains(defender))
            {
               for(int j = 0; j < rateOfFire; j++)
                  damageCount += applyAttack(attacker, defender, attack, abilityOrigin);
            }
            else // arcing
            {
               int reps = 0;
               for(int k = 0; k < arcList.size(); k++)
                  if(arcList.elementAt(k) == defender)
                     reps++;
               for(int j = 0; j < reps; j++)
                  damageCount += applyAttack(attacker, defender, attack, abilityOrigin);
            }
         }
         else
         {
            // apply status effect if there is one and it procs
            if(ability.getStatusEffect() != null)
               if(RNG.nextDouble() < ability.getProcChance())
                  defender.addStatusEffect(ability.getStatusEffect().copy());
         }
         defender.notice(attacker);
         if(ability instanceof Attack && Game.shouldReport(attacker, defender))
         {
            String damageMessage = String.format("%s %s %s for %d damage! ", attacker.getName(), ability.getHitVerb(), 
                                                 defender.getName(), damageCount);
            if(defender.isDead())
               damageMessage += defender.getName() + " is dead! ";
            MainGamePanel.addMessage(damageMessage);
            dir = Direction.getDirectionTo(defender.getTileLoc(), attacker.getTileLoc());
            if(attack.isMelee())
            {
               as = AnimationScriptFactory.getMeleeImpact(defender, dir);
               AnimationManager.addLocking(as);
            }
            else
            {
               as = AnimationScriptFactory.getRecoil(defender, dir);
               AnimationManager.addLocking(as);
            }
         }
      }
      ability.resolveSpecialEffect(attacker.getTileLoc(), targetLoc);
   }
   public static void resolveAbility(Actor attacker, Ability ability, Coord targetLoc){resolveAbility(attacker, ability, targetLoc, 1);}
   
   
   // get the damage dropoff by range
   // multiplier is 1.0 at a distiance of 1, scaling down linearly to 0.5 at max range
   // dist 0 returns 1.0
   // formula is [ (2 * (range - 1) ) - (distance - 1) ] / [ 2 * (range - 1) ]
   public static double getDamageDropoffMultiplier(Attack a, Coord origin, Coord target)
   {
      if(!a.hasDamageDropoff() || origin.equals(target))
         return 1.0;
      int dist = EngineTools.getAngbandDistance(origin, target);
      double numerator = (2 * (a.getRange() - 1)) - (dist - 1);
      double denominator = 2 * (double)(a.getRange() - 1);
      return numerator / denominator;
   }
   public static double getDamageDropoffMultiplier(Attack a, Actor attacker, Actor defender)
   {
      return getDamageDropoffMultiplier(a, attacker.getTileLoc(), defender.getTileLoc());
   }
   
   private static Actor getArcTarget(Actor attacker, Actor defender, int range)
   {
      Vector<Actor> prospectList = new Vector<Actor>();
      {
         for(int x = -range; x < range; x++)
         for(int y = -range; y < range; y++)
         {
            Coord c = new Coord(defender.getTileLoc().x + x, defender.getTileLoc().y + y);
            if(Game.isActorAt(c))
            {
               Actor prospect = Game.getActorAt(c);
               if(prospect != attacker &&
                  prospect != defender &&
                  defender.hasLineOfEffect(prospect))
                  prospectList.add(prospect);
            }
         }
      }
      if(prospectList.size() > 0)
         return prospectList.elementAt(RNG.nextInt(prospectList.size()));
      return null;
   }
}