package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import Daedalus.Combat.*;
import Daedalus.Ability.*;
import java.util.*;

public class ArmorFactory implements ItemConstants, GUIConstants, CombatConstants
{
   public static Vector<? extends TableItem> standardTable = getStandardTable();
   
   public static void setLowQuality(Armor a)
   {
      a.setName("Low-Quality " + a.getName());
      a.setFGColor(LOW_QUALITY_COLOR);

      for(DamageType dType: DamageType.values())
      {
         int dmg = a.getDamageProtection(dType);
         if(dmg > 0)
         {
            a.setDamageProtection(dType, Math.max(1, dmg - 1));
         }
      }
      a.setMaxGadgets(Math.max(1, a.getMaxGadgets() - 1));
   }
   
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
      TableItemWrapper result = (TableItemWrapper)RNG.roll(standardTable, level);
      ArmorTableEntry entry = (ArmorTableEntry)result.getObject();
      Armor.BaseType type = entry.type;
      ItemQuality quality = entry.quality;
      
      Armor a = getByBaseType(type);
      if(quality == ItemQuality.LOW)
         setLowQuality(a);
      return a;
   }
   
   private static Vector<ArmorTableEntry> getStandardTable()
   {
      Vector<ArmorTableEntry> list = new Vector<ArmorTableEntry>();
      list.add(new ArmorTableEntry(Armor.BaseType.UTILITY_HARNESS, ItemQuality.LOW, 0, 100, 1.0));
      list.add(new ArmorTableEntry(Armor.BaseType.SCOUT, ItemQuality.LOW, 0, 100, 1.0));
      list.add(new ArmorTableEntry(Armor.BaseType.STANDARD, ItemQuality.LOW, 0, 100, 1.0));
      list.add(new ArmorTableEntry(Armor.BaseType.ASSAULT, ItemQuality.LOW, 0, 100, 1.0));
      
      list.add(new ArmorTableEntry(Armor.BaseType.UTILITY_HARNESS, ItemQuality.STANDARD, 0, 100, 1.0));
      list.add(new ArmorTableEntry(Armor.BaseType.SCOUT, ItemQuality.STANDARD, 0, 100, 1.0));
      list.add(new ArmorTableEntry(Armor.BaseType.STANDARD, ItemQuality.STANDARD, 0, 100, 1.0));
      list.add(new ArmorTableEntry(Armor.BaseType.ASSAULT, ItemQuality.STANDARD, 0, 100, 1.0));
      return list;
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