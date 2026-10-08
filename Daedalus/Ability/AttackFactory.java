package Daedalus.Ability;

import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import Daedalus.Combat.*;
import Daedalus.Engine.*;
import java.util.*;

public class AttackFactory implements CombatConstants, ItemConstants, AbilityConstants
{
   
   public static Attack getWeakExplosion()
   {
      Attack a = new Attack("Explosion");
      a.setBaseDamage(new Damage(DamageType.CONCUSSION, DEFAULT_BASE_SHOT_DAMAGE * 2));
      a.setRandomDamage(new Damage(DamageType.CONCUSSION, DEFAULT_RANDOM_SHOT_DAMAGE));
      a.setTargetingType(AbilityConstants.TargetingType.BLAST);
      a.setRange(7);
      a.setHitVerb("blasts");
      a.setImpactEffect(AbilityConstants.ImpactEffect.EXPLOSION);
      a.setHeavy(true);
      return a;
   }
   
   
   public static Attack getStandardExplosion()
   {
      Attack a = new Attack("Explosion");
      a.setBaseDamage(new Damage(DamageType.CONCUSSION, DEFAULT_BASE_SHOT_DAMAGE * 3));
      a.setRandomDamage(new Damage(DamageType.CONCUSSION, DEFAULT_RANDOM_SHOT_DAMAGE));
      a.setTargetingType(AbilityConstants.TargetingType.BLAST);
      a.setRange(7);
      a.setHitVerb("blasts");
      a.setImpactEffect(AbilityConstants.ImpactEffect.EXPLOSION);
      a.setHeavy(true);
      return a;
   }
   
   public static Attack getStrongExplosion()
   {
      Attack a = new Attack("Explosion");
      a.setBaseDamage(new Damage(DamageType.CONCUSSION, DEFAULT_BASE_SHOT_DAMAGE * 4));
      a.setRandomDamage(new Damage(DamageType.CONCUSSION, DEFAULT_RANDOM_SHOT_DAMAGE));
      a.setTargetingType(AbilityConstants.TargetingType.BLAST);
      a.setRange(7);
      a.setHitVerb("blasts");
      a.setImpactEffect(AbilityConstants.ImpactEffect.EXPLOSION);
      a.setHeavy(true);
      return a;
   }
   
   public static void addDamageType(Attack a, DamageType dType, double procChance)
   {
      Damage baseDamage = a.getBaseDamage();
      Damage randomDamage = a.getRandomDamage();
      baseDamage.setValue(dType, baseDamage.getValue(dType) + 2);
      randomDamage.setValue(dType, randomDamage.getValue(dType) + 1);
      
      if(procChance > 0.0)
      {
         a.setProcChance(procChance);
         switch(dType)
         {
            case DamageType.FIRE: a.setStatusEffect(StatusEffectFactory.getBurning()); break;
            case DamageType.CRYO: a.setStatusEffect(StatusEffectFactory.getSlowed()); break;
            case DamageType.CORROSION: a.setStatusEffect(StatusEffectFactory.getVulnerable()); break;
            case DamageType.ELECTRIC: a.setSpecialEffect(SpecialEffect.ARC); break;
         }
      }
   }
}