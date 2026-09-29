package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Combat.*;
import Daedalus.Ability.*;

public class ArmorFactory implements ItemConstants, GUIConstants, CombatConstants
{

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
}