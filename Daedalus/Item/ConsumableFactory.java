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
   
   private static Ability getSelfTargetingAbility(String name)
   {
      Ability a = new Ability(name);
      a.setRange(0);
      return a;
   }
   
   
   private static Vector<TableItemWrapper> getStandardTable()
   {
      Vector<TableItemWrapper> list = new Vector<TableItemWrapper>();
      for(Consumable.BaseType type: Consumable.BaseType.values())
      {
         list.add(new TableItemWrapper(type, 0, 100, 1.0));
      }
      return list;
   }

}