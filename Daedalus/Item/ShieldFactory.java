package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Combat.*;
import Daedalus.Engine.*;
import Daedalus.Ability.*;
import java.util.*;

public class ShieldFactory implements ItemConstants, GUIConstants, CombatConstants
{
   public static Vector<? extends TableItem> standardTable = getStandardTable();
   
   public static void setLowQuality(Shield s)
   {
      s.setName("Low Quality " + s.getName());
      s.setFGColor(LOW_QUALITY_COLOR);
      if(s.getMaxDamageCapacity() > (Shield.STANDARD_MAX_DAMAGE_CAPACITY / 2))
         s.setMaxDamageCapacity(s.getMaxDamageCapacity() - 2);
      else
         s.setMaxDamageCapacity(s.getMaxDamageCapacity() - 1);
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
   
   public static Shield rollShield(int level)
   {
      TableItemWrapper result = (TableItemWrapper)RNG.roll(standardTable, level);
      ShieldTableEntry entry = (ShieldTableEntry)result.getObject();
      Shield.BaseType type = entry.type;
      ItemQuality quality = entry.quality;
      
      Shield s = getByBaseType(type);
      if(quality == ItemQuality.LOW)
         setLowQuality(s);
      return s;
   }
   
   private static Vector<TableItemWrapper> getStandardTable()
   {
      Vector<TableItemWrapper> list = new Vector<TableItemWrapper>();
      list.add(new TableItemWrapper(new ShieldTableEntry(Shield.BaseType.STANDARD, ItemQuality.LOW), 0, 100, 1.0));
      list.add(new TableItemWrapper(new ShieldTableEntry(Shield.BaseType.QUICK_CHARGE, ItemQuality.LOW), 0, 100, 1.0));
      list.add(new TableItemWrapper(new ShieldTableEntry(Shield.BaseType.HEAVY, ItemQuality.LOW), 0, 100, 1.0));
      
      list.add(new TableItemWrapper(new ShieldTableEntry(Shield.BaseType.STANDARD, ItemQuality.STANDARD), 0, 100, 1.0));
      list.add(new TableItemWrapper(new ShieldTableEntry(Shield.BaseType.QUICK_CHARGE, ItemQuality.STANDARD), 0, 100, 1.0));
      list.add(new TableItemWrapper(new ShieldTableEntry(Shield.BaseType.HEAVY, ItemQuality.STANDARD), 0, 100, 1.0));
      return list;
   }
   
   
   private static class ShieldTableEntry
   {
      public Shield.BaseType type;
      public ItemQuality quality;
      
      public ShieldTableEntry(Shield.BaseType t, ItemQuality iq)
      {
         type = t;
         quality = iq;
      }
   }
}