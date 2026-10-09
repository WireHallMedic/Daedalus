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
         case STANDARD: a.setStatusEffect(StatusEffectFactory.getHealing(2)); 
                        break;
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
         case ItemQuality.STANDARD: a = AttackFactory.getStandardExplosion();
                                    break; 
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
         case ItemQuality.LOW:      a.setSpecialEffectIntensity(10); 
                                    break;
         case ItemQuality.STANDARD: a.setSpecialEffectIntensity(16);  
                                    c.setName("Heavy " + c.getName());
                                    break;
      }
      c.setAbility(a);
      return c;
   }
   public static Consumable getSmokeGrenade(){return getSmokeGrenade(ItemQuality.STANDARD);}
   
   
   public static Consumable getCausticSmokeGrenade(ItemQuality quality)
   {
      Consumable c = new Consumable("Caustic Smoke Grenade");
      Ability a = new Ability("Smoke");
      a.setRange(5);
      a.setSpecialEffect(SpecialEffect.CAUSTIC_SMOKE);
      switch(quality)
      {
         case ItemQuality.STANDARD: a.setSpecialEffectIntensity(10); 
                                    break;
         case ItemQuality.HIGH:     a.setSpecialEffectIntensity(16);  
                                    c.setName("Heavy " + c.getName());
                                    break;
      }
      c.setAbility(a);
      return c;
   }
   public static Consumable getCausticSmokeGrenade(){return getCausticSmokeGrenade(ItemQuality.STANDARD);}
   
   
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
      int baseDuration = 8;
      StatusEffect se = StatusEffectFactory.getHasted();
      switch(quality)
      {
         case ItemQuality.LOW:      se.setMaxDuration(baseDuration);
                                    c.setName("Light " + c.getName());
                                    break;
         case ItemQuality.STANDARD: se.setMaxDuration(baseDuration * 2);
                                    break; 
         case ItemQuality.HIGH:     se.setMaxDuration(baseDuration * 3); 
                                    c.setName("Heavy " + c.getName());
                                    break;
      }
      a.setStatusEffect(se);
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
         case CAUSTIC_SMOKE_GRENADE:  return getCausticSmokeGrenade();
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
   
   
   public static Vector<ConsumableTableEntry> getStandardTable()
   {
      Vector<ConsumableTableEntry> list = new Vector<ConsumableTableEntry>();
      list.add(new ConsumableTableEntry(Consumable.BaseType.MED_PATCH, ItemQuality.LOW, 0, 6, 2.0));
      list.add(new ConsumableTableEntry(Consumable.BaseType.GRENADE, ItemQuality.LOW, 0, 6, 1.5));
      list.add(new ConsumableTableEntry(Consumable.BaseType.SMOKE_GRENADE, ItemQuality.LOW, 0, 6, 1.0));
      list.add(new ConsumableTableEntry(Consumable.BaseType.DECOY, ItemQuality.LOW, 3, 10, 1.0));
      list.add(new ConsumableTableEntry(Consumable.BaseType.STIMS, ItemQuality.LOW, 3, 10, 1.0));
      // no low-quality caustic grenades
      
      list.add(new ConsumableTableEntry(Consumable.BaseType.MED_PATCH, ItemQuality.STANDARD, 4, 10, 2.0));
      list.add(new ConsumableTableEntry(Consumable.BaseType.GRENADE, ItemQuality.STANDARD, 4, 10, 1.5));
      list.add(new ConsumableTableEntry(Consumable.BaseType.SMOKE_GRENADE, ItemQuality.STANDARD, 4, 10, 1.0));
      list.add(new ConsumableTableEntry(Consumable.BaseType.CAUSTIC_SMOKE_GRENADE, ItemQuality.STANDARD, 8, 14, 1.0));
      list.add(new ConsumableTableEntry(Consumable.BaseType.DECOY, ItemQuality.STANDARD, 8, 14, 1.0));
      list.add(new ConsumableTableEntry(Consumable.BaseType.STIMS, ItemQuality.STANDARD, 8, 14, 1.0));
      
      list.add(new ConsumableTableEntry(Consumable.BaseType.MED_PATCH, ItemQuality.HIGH, 8, 100, 2.0));
      list.add(new ConsumableTableEntry(Consumable.BaseType.GRENADE, ItemQuality.HIGH, 8, 100, 1.5));
      list.add(new ConsumableTableEntry(Consumable.BaseType.CAUSTIC_SMOKE_GRENADE, ItemQuality.HIGH, 12, 100, 1.0));
      list.add(new ConsumableTableEntry(Consumable.BaseType.DECOY, ItemQuality.HIGH, 12, 100, 1.0));
      list.add(new ConsumableTableEntry(Consumable.BaseType.STIMS, ItemQuality.HIGH, 12, 100, 1.0));
      // no high-quality smoke grenades
      return list;
   }
   
   // function for automated test
   public static boolean standardTableContains(Consumable.BaseType type)
   {
      for(int i = 0; i < standardTable.size(); i++)
      {
         ConsumableTableEntry element = (ConsumableTableEntry)standardTable.elementAt(i);
         if(element.type== type)
            return true;
      }
      return false;
   }
   
   private static class ConsumableTableEntry implements TableItem
   {
      public Consumable.BaseType type;
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
      
      public ConsumableTableEntry(Consumable.BaseType t, ItemQuality iq, int min, int max, double weightMultiplier)
      {
         type = t;
         quality = iq;
         minLevel = min;
         maxLevel = max;
         weight = (int)(BASE_WEIGHT * weightMultiplier);
      }
   }

}