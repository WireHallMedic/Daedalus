package Daedalus.Combat;

import Daedalus.Ability.*;
import Daedalus.Engine.*;
import Daedalus.Actor.*;
import WidlerSuite.Coord;
import java.util.*;

public class CombatManager implements CombatConstants, AbilityConstants
{
   private static void applyAttack(Actor attacker, Actor defender, Attack attack)
   {
      int damageDealt = defender.applyDamage(attack.getDamage());
   }
   
   public static void resolveAttack(Actor attacker, Attack attack, Coord targetLoc)
   {
      System.out.println("Resolving " + attack.getName());
      Vector<Coord> affectedList = attack.getAffectedTiles(attacker.getTileLoc(), targetLoc);
      Vector<Actor> defenderList = new Vector<Actor>();
      for(int i = 0; i < affectedList.size(); i++)
      {
         if(Game.isActorAt(affectedList.elementAt(i)))
            defenderList.add(Game.getActorAt(affectedList.elementAt(i)));
      }
      for(int i = 0; i < defenderList.size(); i++)
         applyAttack(attacker, defenderList.elementAt(i), attack);
   }
}