package Daedalus.Combat;

import Daedalus.Ability.*;
import Daedalus.Actor.*;

public class CombatManager implements CombatConstants, AbilityConstants
{
   public static void applyAttack(Actor attacker, Actor defender, Attack attack)
   {
      int damageDealt = defender.applyDamage(attack.getDamage());
   }
}