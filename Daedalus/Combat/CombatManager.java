package Daedalus.Combat;

import Daedalus.Ability.*;
import Daedalus.Engine.*;
import Daedalus.Actor.*;
import Daedalus.Zone.*;
import Daedalus.GUI.*;
import WidlerSuite.Coord;
import java.util.*;

public class CombatManager implements CombatConstants, AbilityConstants, ZoneConstants
{
   private static int applyAttack(Actor attacker, Actor defender, Attack attack)
   {
      int damageDealt = defender.applyDamage(attack.rollDamage(), getDamageDropoffMultiplier(attack, attacker, defender));
      return damageDealt;
   }
   
   public static void resolveAttack(Actor attacker, Attack attack, Coord targetLoc, int rateOfFire)
   {
      Vector<Coord> affectedList = attack.getAffectedTiles(attacker.getTileLoc(), targetLoc);
      Vector<Actor> defenderList = new Vector<Actor>();
      for(int i = 0; i < affectedList.size(); i++)
      {
         if(Game.isActorAt(affectedList.elementAt(i)))
            defenderList.add(Game.getActorAt(affectedList.elementAt(i)));
      }
      Direction dir = Direction.getDirectionTo(attacker.getTileLoc(), targetLoc);
      AnimationScript as;
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
      MainGamePanel.clearMessage();
      for(int i = 0; i < defenderList.size(); i++)
      {
         Actor defender = defenderList.elementAt(i);
         int damageCount = 0;
         for(int j = 0; j < rateOfFire; j++)
            damageCount += applyAttack(attacker, defender, attack);
         String damageMessage = String.format("%s hits %s for %d damage! ", attacker.getName(), defender.getName(), damageCount);
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
   
   
   // get the damage dropoff by range
   // multiplier is 1.0 at a distiance of 1, scaling down linearly to 0.5 at max range
   // dist 0 returns 1.0
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
}