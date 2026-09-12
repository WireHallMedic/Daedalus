package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Combat.*;
import Daedalus.Ability.*;

public class WeaponFactory implements ItemConstants, GUIConstants, CombatConstants
{

   public static Weapon getBoltgun()
   {
      Weapon w = new Weapon("Boltgun");
      Attack a = w.getAttack();
      a.setBaseDamage(new Damage(DamageType.PIERCE, DEFAULT_BASE_SHOT_DAMAGE));
      a.setRandomDamage(new Damage(DamageType.PIERCE, DEFAULT_RANDOM_SHOT_DAMAGE));
      a.setTargetingType(AbilityConstants.TargetingType.POINT);
      a.setRange(10);
      w.fullyCharge();
      return w;
   }
   
   
   public static Weapon getShotgun()
   {
      Weapon w = new Weapon("Shotgun");
      Attack a = w.getAttack();
      a.setBaseDamage(new Damage(DamageType.CONCUSSION, DEFAULT_BASE_SHOT_DAMAGE * 3 / 2));
      a.setRandomDamage(new Damage(DamageType.CONCUSSION, DEFAULT_RANDOM_SHOT_DAMAGE));
      a.setTargetingType(AbilityConstants.TargetingType.CONE);
      a.setDamageDropoff(true);
      a.setRange(7);
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
      w.setRateOfFire(3);
      w.fullyCharge();
      return w;
   }
   
   
   public static Weapon getPlasmaLauncher()
   {
      Weapon w = new Weapon("Plasma Launcher");
      Attack a = w.getAttack();
      a.setBaseDamage(new Damage(DamageType.CONCUSSION, DEFAULT_BASE_SHOT_DAMAGE * 3));
      a.setRandomDamage(new Damage(DamageType.CONCUSSION, DEFAULT_RANDOM_SHOT_DAMAGE * 2));
      a.setTargetingType(AbilityConstants.TargetingType.BLAST);
      a.setRange(7);
      w.fullyCharge();
      return w;
   }
   
   public static Weapon getBeamCannon()
   {
      Weapon w = new Weapon("Beam Cannon", Attack.getMock());
      Attack a = w.getAttack();
      a.setBaseDamage(new Damage(DamageType.CONCUSSION, DEFAULT_BASE_SHOT_DAMAGE * 2));
      a.setRandomDamage(new Damage(DamageType.CONCUSSION, DEFAULT_RANDOM_SHOT_DAMAGE));
      w.getAttack().setTargetingType(AbilityConstants.TargetingType.BEAM);
      w.getAttack().setRange(5);
      w.fullyCharge();
      return w;
   }
   
   
   public static Weapon getBasicMelee(int damage)
   {
      Attack atk = new Attack("Punch");
      atk.setMelee(true);
      atk.setRange(1);
      atk.setBaseDamage(new Damage(CombatConstants.DamageType.CONCUSSION, Math.max(0, damage - 2)));
      atk.setRandomDamage(new Damage(CombatConstants.DamageType.CONCUSSION, 2));
      Weapon w = new Weapon("Unarmed", atk);
      w.setAlwaysCharged(true);
      w.fullyCharge();
      return w;
   }
   public static Weapon getBasicMelee(){return getBasicMelee(3);}

}