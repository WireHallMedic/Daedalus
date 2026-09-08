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
   private static void applyAttack(Actor attacker, Actor defender, Attack attack)
   {
      int damageDealt = defender.applyDamage(attack.getDamage());
   }
   
   public static void resolveAttack(Actor attacker, Attack attack, Coord targetLoc)
   {
      Vector<Coord> affectedList = attack.getAffectedTiles(attacker.getTileLoc(), targetLoc);
      Vector<Actor> defenderList = new Vector<Actor>();
      for(int i = 0; i < affectedList.size(); i++)
      {
         if(Game.isActorAt(affectedList.elementAt(i)))
            defenderList.add(Game.getActorAt(affectedList.elementAt(i)));
      }
      Direction dir = Direction.getDirectionTo(attacker.getTileLoc(), targetLoc);
      AnimationScript as = AnimationScriptFactory.getRecoil(attacker, dir);
      AnimationManager.addLocking(as);
      AnimationManager.setScreenRumble();
      for(int i = 0; i < defenderList.size(); i++)
      {
         Actor defender = defenderList.elementAt(i);
         applyAttack(attacker, defender, attack);
         dir = Direction.getDirectionTo(defender.getTileLoc(), attacker.getTileLoc());
         as = AnimationScriptFactory.getRecoil(defender, dir);
         AnimationManager.addLocking(as);
      }
   }
}