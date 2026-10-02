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
   
   
   public static Consumable getMedPatch()
   {
      Consumable c = new Consumable("Med-Patch");
      Ability a = getSelfTargetingAbility("Med-Patch");
      StatusEffect se = new StatusEffect("Healing");
      se.addTag(StatusEffectTag.HEALING);
      se.setIntensity(2);
      a.setStatusEffect(se);
      c.setAbility(a);
      return c;
   }
   
   public static Consumable getGrenade()
   {
      Consumable c = new Consumable("Grenade");
      Attack a = new Attack("Grenade");
      a.setBaseDamage(new Damage(DamageType.CONCUSSION, DEFAULT_BASE_SHOT_DAMAGE * 3));
      a.setRandomDamage(new Damage(DamageType.CONCUSSION, DEFAULT_RANDOM_SHOT_DAMAGE * 2));
      a.setTargetingType(AbilityConstants.TargetingType.BLAST);
      a.setRange(5);
      a.setHitVerb("blasts");
      a.setImpactEffect(AbilityConstants.ImpactEffect.EXPLOSION);
      c.setAbility(a);
      return c;
   }
   
   public static Consumable getSmokeGrenade()
   {
      Consumable c = new Consumable("Smoke Grenade");
      Ability a = new Ability("Smoke");
      a.setRange(5);
      a.setSpecialEffect(SpecialEffect.SMOKE);
      c.setAbility(a);
      return c;
   }
   
   public static Consumable getDecoy()
   {
      Consumable c = new Consumable("Holo-Decoy");
      Ability a = new Ability("Decoy");
      a.setRange(5);
      a.setSpecialEffect(SpecialEffect.DECOY);
      c.setAbility(a);
      return c;
   }
   
   public static Consumable getStims()
   {
      Consumable c = new Consumable("Stims");
      Ability a = getSelfTargetingAbility("Stims");
      StatusEffect se = new StatusEffect("Invigorated");
	   se.getStatBlock().setMoveSpeed(ActorConstants.ActionSpeed.FAST);
	   se.getStatBlock().setAttackSpeed(ActorConstants.ActionSpeed.FAST);
	   se.getStatBlock().setInteractSpeed(ActorConstants.ActionSpeed.FAST);
      a.setStatusEffect(se);
      c.setAbility(a);
      return c;
   }
   
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