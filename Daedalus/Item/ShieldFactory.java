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
   
   public static Shield getStandardShield()
   {
      Shield s = new Shield("Shield");
      s.setMaxDamageCapacity(Shield.STANDARD_MAX_DAMAGE_CAPACITY);
      s.setChargeDelay(Shield.STANDARD_CHARGE_DELAY);
      s.setMaxChargeTurns(Shield.STANDARD_MAX_CHARGE_TIME);
      return s;
   }
   
   public static Shield getHeavyShield()
   {
      Shield s = new Shield("Heavy Shield");
      s.setMaxDamageCapacity(Shield.STANDARD_MAX_DAMAGE_CAPACITY * 2);
      s.setChargeDelay(Shield.STANDARD_CHARGE_DELAY * 2);
      s.setMaxChargeTurns(Shield.STANDARD_MAX_CHARGE_TIME * 2);
      return s;
   }
   
   public static Shield getQuickChargeShield()
   {
      Shield s = new Shield("Quick-Charge Shield");
      s.setMaxDamageCapacity(Shield.STANDARD_MAX_DAMAGE_CAPACITY / 2);
      s.setChargeDelay(Shield.STANDARD_CHARGE_DELAY / 2);
      s.setMaxChargeTurns(Shield.STANDARD_MAX_CHARGE_TIME / 2);
      return s;
   }
   
   public static Shield getByBaseType(Shield.BaseType baseType)
   {
      switch(baseType)
      {
         case STANDARD:       return getStandardShield();
         case QUICK_CHARGE:   return getQuickChargeShield();
         case HEAVY:          return getHeavyShield();
      }
      return null;
   }
   
   
   // enemy shields
   /////////////////////////////////////////
   public static Shield getDroneShield()
   {
      Shield s = new Shield("Drone Shield");
      s.setMaxDamageCapacity(Shield.STANDARD_MAX_DAMAGE_CAPACITY / 2);
      s.setMaxChargeTurns(Shield.STANDARD_MAX_CHARGE_TIME / 2);
      s.fullyCharge();
      return s;
   }
}