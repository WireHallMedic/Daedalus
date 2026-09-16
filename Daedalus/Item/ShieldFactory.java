package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Combat.*;
import Daedalus.Ability.*;

public class ShieldFactory implements ItemConstants, GUIConstants, CombatConstants
{
   public static Shield getBasicShield()
   {
      return new Shield("Basic Shield");
   }
   
   
   // enemy shields
   /////////////////////////////////////////
   public static Shield getDroneShield()
   {
      Shield s = new Shield("Drone Shield");
      s.setMaxDamageCapacity(Shield.STANDARD_MAX_DAMAGE_CAPACITY / 2);
      s.setMaxChargeTurns(Shield.STANDARD_MAX_CHARGE_TIME_STANDARD_TURNS / 2);
      s.fullyCharge();
      return s;
   }
}