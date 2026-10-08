package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import Daedalus.Combat.*;
import Daedalus.Ability.*;
import java.util.*;

public class ConsumableFactory implements ItemConstants, GUIConstants, CombatConstants, AbilityConstants
{
   public static Vector<? extends TableItem> standardTable = getStandardTable();
   
   
   public static Consumable getMedPatch(ItemQuality quality)
   {
      Consumable c = new Consumable("Med-Patch");
      Ability a = getSelfTargetingAbility("Med-Patch");
      switch(quality)
      {
         case LOW:      a.setStatusEffect(StatusEffectFactory.getHealing(1)); 
                        c.setName("Small " + c.getName());
                        break;
         case STANDARD: a.setStatusEffect(StatusEffectFactory.getHealing(2)); break;
         case HIGH:     a.setStatusEffect(StatusEffectFactory.getHealing(4));
                        c.setName("Large " + c.getName());
                        break;
      }
      c.setAbility(a);
      return c;
   }
   public static Consumable getMedPatch(){return getMedPatch(ItemQuality.STANDARD);}
   
   
   public static Consumable getGrenade(ItemQuality quality)
   {
      Consumable c = new Consumable("Grenade");
      Attack a = new Attack("Grenade");
      switch(quality)
      {
         case ItemQuality.LOW:      a = AttackFactory.getWeakExplosion(); 
                                    c.setName("Light " + c.getName());
                                    break;
         case ItemQuality.STANDARD: a = AttackFactory.getStandardExplosion(); break; 
         case ItemQuality.HIGH:     a = AttackFactory.getStrongExplosion();
                                    c.setName("Heavy " + c.getName());
                                    break;
      }
      a.setRange(5);
      c.setAbility(a);
      return c;
   }
   public static Consumable getGrenade(){return getGrenade(ItemQuality.STANDARD);}
   
   
   public static Consumable getSmokeGrenade(ItemQuality quality)
   {
      Consumable c = new Consumable("Smoke Grenade");
      Ability a = new Ability("Smoke");
      a.setRange(5);
      a.setSpecialEffect(SpecialEffect.SMOKE);
      switch(quality)
      {
         case ItemQuality.LOW:      a.setSpecialEffectIntensity(8); 
                                    c.setName("Light " + c.getName());
                                    break;
         case ItemQuality.STANDARD: a.setSpecialEffectIntensity(12);  
                                    break; 
         case ItemQuality.HIGH:     a.setSpecialEffectIntensity(16); 
                                    c.setName("Heavy " + c.getName());
                                    break;
      }
      c.setAbility(a);
      return c;
   }
   public static Consumable getSmokeGrenade(){return getSmokeGrenade(ItemQuality.STANDARD);}
   
   
   public static Consumable getDecoy(ItemQuality quality)
   {
      Consumable c = new Consumable("Holo-Decoy");
      Ability a = new Ability("Decoy");
      a.setRange(5);
      a.setSpecialEffect(SpecialEffect.DECOY);
      switch(quality)
      {
         case ItemQuality.LOW:      a.setSpecialEffectIntensity(1); 
                                    break;
         case ItemQuality.STANDARD: a.setSpecialEffectIntensity(2); 
                                    c.setName("Sturdy " + c.getName()); 
                                    break; 
         case ItemQuality.HIGH:     a.setSpecialEffectIntensity(3); 
                                    c.setName("Exploding " + c.getName());
                                    break;
      }
      c.setAbility(a);
      return c;
   }
   public static Consumable getDecoy(){return getDecoy(ItemQuality.STANDARD);}
   
   
   public static Consumable getStims(ItemQuality quality)
   {
      Consumable c = new Consumable("Stims");
      Ability a = getSelfTargetingAbility("Stims");
      a.setStatusEffect(StatusEffectFactory.getHasted());
      c.setAbility(a);
      return c;
   }
   public static Consumable getStims(){return getStims(ItemQuality.STANDARD);}
   
   
   private static Ability getSelfTargetingAbility(String name)
   {
      Ability a = new Ability(name);
      a.setRange(0);
      return a;
   }
   
   public static Consumable getByBaseType(Consumable.BaseType baseType)
   {
      switch(baseType)
      {
         case MED_PATCH:      return getMedPatch();
         case SMOKE_GRENADE:  return getSmokeGrenade();
         case GRENADE:        return getGrenade();
         case DECOY:          return getDecoy();
         case STIMS:          return getStims();
      }
      return null;
   }
   
   public static Consumable rollConsumable(int level)
   {
      TableItemWrapper roll = (TableItemWrapper)RNG.roll(standardTable, level);
      Consumable.BaseType result = (Consumable.BaseType)roll.getObject();
      
      return getByBaseType(result);
   }
   
   
   public static Vector<TableItemWrapper> getStandardTable()
   {
      Vector<TableItemWrapper> list = new Vector<TableItemWrapper>();
      list.add(new TableItemWrapper(Consumable.BaseType.GRENADE, 0, 100, 1.5));
      list.add(new TableItemWrapper(Consumable.BaseType.SMOKE_GRENADE, 0, 100, 1.0));
      list.add(new TableItemWrapper(Consumable.BaseType.DECOY, 0, 100, 1.0));
      list.add(new TableItemWrapper(Consumable.BaseType.STIMS, 0, 100, 1.0));
      
      // med patches are half the table
      double sum = 0.0;
      for(TableItemWrapper tiw: list)
      {
         sum += tiw.getWeight();
      }
      list.add(new TableItemWrapper(Consumable.BaseType.MED_PATCH, 0, 100, sum));
      return list;
   }
   
   
   // function for automated test
   public static boolean standardTableContains(Consumable.BaseType type)
   {
      for(int i = 0; i < standardTable.size(); i++)
      {
         TableItemWrapper element = (TableItemWrapper)standardTable.elementAt(i);
         if(element.getObject() == type)
            return true;
      }
      return false;
   }

}