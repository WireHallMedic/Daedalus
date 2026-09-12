package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Combat.*;
import Daedalus.Ability.*;

public class Weapon extends ChargeItem implements ItemConstants, GUIConstants, CombatConstants
{
   private static final int STANDARD_CHARGE_TIME_PER_SHOT = 6;

   public enum BaseType
   {
      BOLTGUN,          // single projectile
      SHOTGUN,          // spread
      AUTOGUN,          // rapid-fire
      PLASMA_LAUNCHER,  // exploding projectile
      BEAM_CANNON;      // line
      
   }
   
	private Attack attack;
	private int rateOfFire;
   private int maxShots;
   private int chargeTimePerShot;
   private int chargePerShot;


	public Attack getAttack(){return attack;}
	public int getRateOfFire(){return rateOfFire;}
   public int getMaxShots(){return maxShots;}
   public int getChargeTimePerShot(){return chargeTimePerShot;}


	public void setAttack(Attack a){attack = a;}
	public void setRateOfFire(int r){rateOfFire = r;}
   public void setMaxShots(int msc){maxShots = msc; setValues();}
   public void setChargeTimePerShot(int ctps){chargeTimePerShot = ctps; setValues();}


   public Weapon(String name)
   {
      super(name, ItemBase.WEAPON);
      attack = new Attack("Unknown Attack");
      rateOfFire = 1;
      maxShots = 5;
      chargeTimePerShot = STANDARD_CHARGE_TIME_PER_SHOT;
      setValues();
      fullyCharge();
   }
   
   public Weapon(String name, Attack atk)
   {
      this(name);
      attack = atk;
   }
   
   
   private void setValues()
   {
      setMaxCharge(getMaxShots() * getChargeTimePerShot() * getChargeRate());
      chargePerShot = getMaxCharge() / getMaxShots();
   }

   
   public int getChargedShots()
   {
      return getCurCharge() / chargePerShot;
   }
   
   
   public void discharge()
   {
      super.discharge(chargePerShot);
   }
   
   @Override
   public void setMaxChargeTurns(int val)
   {
      throw new Error("setMaxChargeTurns() inappropriate for weapons, use setMaxShots() and setChargeTimePerShot() instead");
   }
   
   
   public void setChargeTimePerShotTurns(int ctps)
   {
      setChargeTimePerShot(ActorConstants.ActionSpeed.NORMAL.increments * ctps);
   }
   
   
   public void setAlwaysCharged(boolean a)
   {
      if(a)
      {
         setMaxShots(1);
      }
      super.setAlwaysCharged(a);
   }
   
   
   public static Weapon getMock()
   {
      Weapon w = new Weapon("Test Weapon", Attack.getMock());
      w.fullyCharge();
      return w;
   }
   
   
}