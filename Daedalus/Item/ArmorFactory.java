package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import Daedalus.Combat.*;
import Daedalus.Ability.*;
import java.util.*;

public class ArmorFactory implements ItemConstants, GUIConstants, CombatConstants
{
   private static Vector<? extends TableItem> standardTable = getStandardTable();
   private static Vector<? extends TableItem> upgradeTable = getUpgradeTable();
   
   private enum ArmorUpgrade{REINFORCED, INSULATED, ENGINEERS, ELITE};
   
   // basic types
   //////////////////////////////////////
   
   public static Armor getUtilityHarness()
   {
      Armor a = new Armor("Utility Harness");
      return a;
   }

   public static Armor getScoutArmor()
   {
      Armor a = new Armor("Scout Armor");
      a.setMaxGadgets(Armor.DEFAULT_GADGET_SLOTS + 1);
      return a;
   }

   public static Armor getStandardArmor()
   {
      Armor a = new Armor("Standard Armor");
      a.setDamageProtection(DamageType.CONCUSSION, 2);
      a.setDamageProtection(DamageType.PIERCE, 2);
      return a;
   }

   public static Armor getAssaultArmor()
   {
      Armor a = new Armor("Assault Armor");
      for(DamageType type: DamageType.values())
         a.setDamageProtection(type, 2);
      a.setDamageProtection(DamageType.CONCUSSION, 4);
      a.setDamageProtection(DamageType.PIERCE, 4);
      a.setMaxGadgets(Armor.DEFAULT_GADGET_SLOTS - 1);
      return a;
   }
   
   public static void setLowQuality(Armor a)
   {
      a.setName("Scavenged " + a.getName());
      a.setFGColor(LOW_QUALITY_COLOR);

      for(DamageType dType: DamageType.values())
      {
         int dmg = a.getDamageProtection(dType);
         if(dmg > 0)
         {
            a.setDamageProtection(dType, Math.max(1, dmg - 1));
         }
      }
      a.setMaxGadgets(Math.max(1, Math.max(1, a.getMaxGadgets() - 1)));
   }
   
   public static void setInsulated(Armor a)
   {
      a.setName("Insulated " + a.getName());

      a.setDamageProtection(DamageType.FIRE, a.getDamageProtection(DamageType.FIRE) + 2);
      a.setDamageProtection(DamageType.CRYO, a.getDamageProtection(DamageType.CRYO) + 2);
      a.setDamageProtection(DamageType.CORROSION, a.getDamageProtection(DamageType.CORROSION) + 2);
      a.setDamageProtection(DamageType.ELECTRIC, a.getDamageProtection(DamageType.ELECTRIC) + 2);
   }

   public static void setReinforced(Armor a)
   {
      a.setName("Reinforced " + a.getName());

      a.setDamageProtection(DamageType.CONCUSSION, a.getDamageProtection(DamageType.CONCUSSION) + 2);
      a.setDamageProtection(DamageType.PIERCE, a.getDamageProtection(DamageType.PIERCE) + 2);
   }

   public static void setExtraGadgets(Armor a)
   {
      a.setName("Engineer's " + a.getName());

      a.setMaxGadgets(Math.min(1, Math.max(5, a.getMaxGadgets() + 1)));
   }
   
   public static void setElite(Armor a)
   {
      a.setName("Elite " + a.getName());

      for(DamageType dType: DamageType.values())
      {
         a.setDamageProtection(dType, a.getDamageProtection(dType) + 1);
      }
   }
   
   public static Armor getByBaseType(Armor.BaseType baseType)
   {
      switch(baseType)
      {
         case UTILITY_HARNESS:   return getUtilityHarness();
         case SCOUT:             return getScoutArmor();
         case STANDARD:          return getStandardArmor();
         case ASSAULT:           return getAssaultArmor();
      }
      return null;
   }
   
   public static Armor rollArmor(int level)
   {
      ArmorTableEntry result = (ArmorTableEntry)RNG.roll(standardTable, level);
      
      Armor a = getByBaseType(result.type);
      if(result.quality == ItemQuality.LOW)
         setLowQuality(a);
      if(result.quality == ItemQuality.HIGH)
      {
         rollUpgrade(a, level);
         a.setFGColor(HIGH_QUALITY_COLOR);
      }
      return a;
   }
   
   public static void rollUpgrade(Armor a, int level)
   {
      TableItemWrapper roll = (TableItemWrapper)RNG.roll(upgradeTable, level);
      if(roll != null)
      {
         switch((ArmorUpgrade)roll.getObject())
         {
            case REINFORCED : setReinforced(a); break;
            case INSULATED :  setInsulated(a); break;
            case ENGINEERS :  setExtraGadgets(a); break;
            case ELITE :      setElite(a); break;
         }
      }
   }
   
   private static Vector<ArmorTableEntry> getStandardTable()
   {
      Vector<ArmorTableEntry> list = new Vector<ArmorTableEntry>();
      list.add(new ArmorTableEntry(Armor.BaseType.UTILITY_HARNESS, ItemQuality.LOW, 0, 5, 4.0));
      
      list.add(new ArmorTableEntry(Armor.BaseType.SCOUT, ItemQuality.LOW, 2, 100, 2.0));
      list.add(new ArmorTableEntry(Armor.BaseType.STANDARD, ItemQuality.LOW, 2, 100, 2.0));
      
      list.add(new ArmorTableEntry(Armor.BaseType.UTILITY_HARNESS, ItemQuality.STANDARD, 3, 10, 1.0));
      
      list.add(new ArmorTableEntry(Armor.BaseType.SCOUT, ItemQuality.STANDARD, 5, 100, 1.0));
      list.add(new ArmorTableEntry(Armor.BaseType.STANDARD, ItemQuality.STANDARD, 5, 100, 1.0));
      list.add(new ArmorTableEntry(Armor.BaseType.ASSAULT, ItemQuality.LOW, 5, 10, 1.0));
      
      list.add(new ArmorTableEntry(Armor.BaseType.ASSAULT, ItemQuality.STANDARD, 9, 100, 0.5));
      
      list.add(new ArmorTableEntry(Armor.BaseType.SCOUT, ItemQuality.HIGH, 8, 100, 0.25));
      list.add(new ArmorTableEntry(Armor.BaseType.STANDARD, ItemQuality.HIGH, 8, 100, 0.25));
      
      list.add(new ArmorTableEntry(Armor.BaseType.ASSAULT, ItemQuality.HIGH, 10, 10, 0.25));
      
      
      return list;
   }
   
   private static Vector<TableItemWrapper> getUpgradeTable()
   {
      Vector<TableItemWrapper> list = new Vector<TableItemWrapper>();
      list.add(new TableItemWrapper(ArmorUpgrade.REINFORCED, 1, 100, 1.0));
      list.add(new TableItemWrapper(ArmorUpgrade.INSULATED, 1, 100, 1.0));
      list.add(new TableItemWrapper(ArmorUpgrade.ENGINEERS, 1, 100, .75));
      list.add(new TableItemWrapper(ArmorUpgrade.ELITE, 1, 100, 0.5));
      
      return list;
   }
   
   
   // function for automated test
   public static boolean standardTableContains(Armor.BaseType type)
   {
      for(int i = 0; i < standardTable.size(); i++)
      {
         ArmorTableEntry element = (ArmorTableEntry)standardTable.elementAt(i);
         if(element.type== type)
            return true;
      }
      return false;
   }
   
   private static class ArmorTableEntry implements TableItem
   {
      public Armor.BaseType type;
      public ItemQuality quality;
      private int minLevel;
   	private int maxLevel;
   	private int weight;
      
      public int getMinLevel(){return minLevel;}
   	public int getMaxLevel(){return maxLevel;}
   	public int getWeight(){return weight;}
   
   
   	public void setMinLevel(int m){minLevel = m;}
   	public void setMaxLevel(int m){maxLevel = m;}
   	public void setWeight(int w){weight = w;}
      
      public ArmorTableEntry(Armor.BaseType t, ItemQuality iq, int min, int max, double weightMultiplier)
      {
         type = t;
         quality = iq;
         minLevel = min;
         maxLevel = max;
         weight = (int)(BASE_WEIGHT * weightMultiplier);
      }
   }
}