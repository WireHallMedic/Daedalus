package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Combat.*;
import Daedalus.Ability.*;

public class WeaponFactory implements ItemConstants, GUIConstants, CombatConstants
{
   public static void setLowQuality(Weapon w)
   {
      w.setName("Low-Quality " + w.getName());
      w.setFGColor(LOW_QUALITY_COLOR);
      Attack a = w.getAttack();
      Damage base = a.getBaseDamage();
      Damage random = a.getRandomDamage();
      for(int i = 0; i < DamageType.values().length; i++)
      {
         DamageType type = DamageType.values()[i];
         if(base.getValue(type) > 0)
            base.setValue(type, Math.max(1, base.getValue(type) - 2));
         if(random.getValue(type) > 0)
            random.setValue(type, Math.max(1, random.getValue(type) - 1));
      }
   }
   
   // basic weapons
   
   public static Weapon getBoltgun()
   {
      Weapon w = new Weapon("Boltgun");
      Attack a = w.getAttack();
      a.setBaseDamage(new Damage(DamageType.PIERCE, DEFAULT_BASE_SHOT_DAMAGE));
      a.setRandomDamage(new Damage(DamageType.PIERCE, DEFAULT_RANDOM_SHOT_DAMAGE));
      a.setTargetingType(AbilityConstants.TargetingType.POINT);
      a.setRange(10);
      a.setHitVerb("shoots");
      w.setMaxShots(6);
      w.fullyCharge();
      return w;
   }
   
   
   public static Weapon getScattergun()
   {
      Weapon w = new Weapon("Scattergun");
      Attack a = w.getAttack();
      a.setBaseDamage(new Damage(DamageType.CONCUSSION, DEFAULT_BASE_SHOT_DAMAGE * 3 / 2));
      a.setRandomDamage(new Damage(DamageType.CONCUSSION, DEFAULT_RANDOM_SHOT_DAMAGE));
      a.setTargetingType(AbilityConstants.TargetingType.CONE);
      a.setDamageDropoff(true);
      a.setRange(7);
      a.setHitVerb("blasts");
      w.setMaxShots(4);
      w.fullyCharge();
      return w;
   }
   
   
   public static Weapon getAutogun()
   {
      Weapon w = new Weapon("Autogun");
      Attack a = w.getAttack();
      a.setBaseDamage(new Damage(DamageType.PIERCE, DEFAULT_BASE_SHOT_DAMAGE / 3));
      a.setRandomDamage(new Damage(DamageType.PIERCE, DEFAULT_RANDOM_SHOT_DAMAGE));
      a.setTargetingType(AbilityConstants.TargetingType.POINT);
      a.setRange(7);
      a.setHitVerb("shoots");
      w.setRateOfFire(3);
      w.setMaxShots(4);
      w.fullyCharge();
      return w;
   }
   
   
   public static Weapon getPlasmaCannon()
   {
      Weapon w = new Weapon("Plasma Cannon");
      Attack a = w.getAttack();
      a.setBaseDamage(new Damage(DamageType.CONCUSSION, DEFAULT_BASE_SHOT_DAMAGE * 3));
      a.setRandomDamage(new Damage(DamageType.CONCUSSION, DEFAULT_RANDOM_SHOT_DAMAGE * 2));
      a.setTargetingType(AbilityConstants.TargetingType.BLAST);
      a.setRange(7);
      a.setHitVerb("blasts");
      a.setImpactEffect(AbilityConstants.ImpactEffect.EXPLOSION);
      w.setMaxShots(1);
      w.setChargeTimePerShot(10);
      w.fullyCharge();
      return w;
   }
   
   public static Weapon getBeamCannon()
   {
      Weapon w = new Weapon("Beam Cannon", Attack.getMock());
      Attack a = w.getAttack();
      a.setBaseDamage(new Damage(DamageType.CONCUSSION, DEFAULT_BASE_SHOT_DAMAGE * 2));
      a.setRandomDamage(new Damage(DamageType.CONCUSSION, DEFAULT_RANDOM_SHOT_DAMAGE));
      a.setHitVerb("blasts");
      w.getAttack().setTargetingType(AbilityConstants.TargetingType.BEAM);
      w.getAttack().setRange(5);
      w.setMaxShots(2);
      w.setChargeTimePerShot(10);
      w.fullyCharge();
      return w;
   }
   
   
   public static Weapon getBasicMelee(int damage)
   {
      Attack a = new Attack("Punch");
      a.setMelee(true);
      a.setRange(1);
      a.setBaseDamage(new Damage(CombatConstants.DamageType.CONCUSSION, damage));
      a.setRandomDamage(new Damage(CombatConstants.DamageType.CONCUSSION, 2));
      a.setHitVerb("strikes");
      Weapon w = new Weapon("Unarmed", a);
      w.setAlwaysCharged(true);
      w.fullyCharge();
      return w;
   }
   public static Weapon getBasicMelee(){return getBasicMelee(DEFAULT_BASE_SHOT_DAMAGE / 2);}
   
   
   public static Weapon getByBaseType(Weapon.BaseType baseType)
   {
      switch(baseType)
      {
         case MELEE:             return getBasicMelee();
         case BOLTGUN:           return getBoltgun();
         case SCATTERGUN:        return getScattergun();
         case AUTOGUN:           return getAutogun();
         case PLASMA_CANNON:     return getPlasmaCannon();
         case BEAM_CANNON:       return getBeamCannon();
      }
      return null;
   }
   
   // upgrades
   ////////////////////////////////////////////////////
   
   public static void improveCapacity(Weapon w)
   {
      w.setName("High-Capacity " + w.getName());
      int newMaxShots = Math.max((w.getMaxShots() * 3) / 2, w.getMaxShots() + 1);
      w.setMaxShots(newMaxShots);
   }
   
   public static void improveRange(Weapon w)
   {
      w.setName("Long-Range " + w.getName());
      int newRange = w.getAttack().getRange() + 2;
      if(w.getAttack().getRange() <= 5)
         newRange = w.getAttack().getRange() + 1;
      w.getAttack().setRange(newRange);
   }
   
   public static void improveDamage(Weapon w)
   {
      w.setName("Heavy " + w.getName());
      if(w.getRateOfFire() > 1)
         increaseRoF(w);
      else
         increaseHighestDamage(w);
   }
   
   public static void improveRecharge(Weapon w)
   {
      // most weapons improved by 1 s
      int newChargeTimePerShot = w.getChargeTimePerShot() - 2;
      // slow charging weapons (10+ s per shot) improved by 2s
      if(w.getChargeTimePerShot() >= 20)
         newChargeTimePerShot = w.getChargeTimePerShot() - 4;
      // fast charging weapons (3- s per shot) improved by 0.5s
      if(w.getChargeTimePerShot() <= 6)
         newChargeTimePerShot = w.getChargeTimePerShot() - 1;
      newChargeTimePerShot = Math.max(2, newChargeTimePerShot);
      w.setChargeTimePerShot(newChargeTimePerShot);
   }
   
   private static void increaseRoF(Weapon w)
   {
      w.setRateOfFire(w.getRateOfFire() + 1);
   }
   
   private static void increaseHighestDamage(Weapon w)
   {
      DamageType highestType = DamageType.values()[0];
      int highestDamage = w.getAttack().getBaseDamage().getValue(highestType);
      for(DamageType curType: DamageType.values())
      {
         if(w.getAttack().getBaseDamage().getValue(curType) > highestDamage)
         {
            highestType = curType;
            highestDamage = w.getAttack().getBaseDamage().getValue(curType);
         }
      }
      int newBase = Math.max((int)(highestDamage * 1.25), highestDamage + 2);
      int newRandom = w.getAttack().getRandomDamage().getValue(highestType) + 1;
      w.getAttack().getBaseDamage().setValue(highestType, newBase);
      w.getAttack().getRandomDamage().setValue(highestType, newRandom);
   }
   
   // enemy weapons
   ////////////////////////////////////////////////////
   public static Weapon getDroneGun()
   {
      Weapon w = new Weapon("Drone Gun");
      Attack a = w.getAttack();
      a.setBaseDamage(new Damage(DamageType.PIERCE, DEFAULT_BASE_SHOT_DAMAGE));
      a.setRandomDamage(new Damage(DamageType.PIERCE, DEFAULT_RANDOM_SHOT_DAMAGE));
      a.setTargetingType(AbilityConstants.TargetingType.POINT);
      a.setRange(5);
      a.setHitVerb("shoots");
      w.setMaxShots(1);
      w.setChargeTimePerShotTurns(2);
      w.fullyCharge();
      return w;
   }
   
   
   public static Weapon getJackalJaws()
   {
      Attack a = new Attack("Bite");
      a.setMelee(true);
      a.setRange(1);
      a.setBaseDamage(new Damage(CombatConstants.DamageType.PIERCE, DEFAULT_BASE_SHOT_DAMAGE / 2));
      a.setRandomDamage(new Damage(CombatConstants.DamageType.PIERCE, 2));
      a.setHitVerb("bites");
      Weapon w = new Weapon("Jaws", a);
      w.setAlwaysCharged(true);
      w.fullyCharge();
      return w;
   }
   
   
   public static Weapon getTestWeapon()
   {
      Weapon w = getScattergun();
      return w;
   }
}