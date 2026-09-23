package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Combat.*;
import Daedalus.Ability.*;

public class ShieldFactory implements ItemConstants, GUIConstants, CombatConstants
{
   public static void setLowQuality(Shield s)
   {
      s.setName("Low Quality " + s.getName());
      s.setFGColor(LOW_QUALITY_COLOR);
      s.setMaxDamageCapacity(s.getMaxDamageCapacity() - 2);
      s.fullyCharge();
   }
   
   
   public static Shield getBasicShield()
   {
      Shield s = new Shield("Basic Shield");
      s.setMaxDamageCapacity(Shield.STANDARD_MAX_DAMAGE_CAPACITY);
      s.setChargeDelayTurns(Shield.STANDARD_CHARGE_DELAY_NORMAL_TURNS);
      s.setMaxChargeTurns(Shield.STANDARD_MAX_CHARGE_TIME_STANDARD_TURNS);
      return s;
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