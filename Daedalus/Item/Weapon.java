package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Ability.*;
import Daedalus.Combat.*;

public class Weapon extends ChargeItem implements ItemConstants, GUIConstants
{
   public static final int STANDARD_MAX_CHARGE_TIME_STANDARD_TURNS = 10;
   
	private Attack attack;
	private int rateOfFire;
   private int maxShotCapacity;
   private int chargePerShot;


	public Attack getAttack(){return attack;}
	public int getRateOfFire(){return rateOfFire;}
   public int getMaxShotCapacity(){return maxShotCapacity;}
   public int getChargePerShot(){return chargePerShot;}


	public void setAttack(Attack a){attack = a;}
	public void setRateOfFire(int r){rateOfFire = r;}
   public void setMaxShotCapacity(int msc){maxShotCapacity = msc; setChargePerShot();}


   public Weapon(String name)
   {
      super(name, ItemBase.WEAPON);
      attack = new Attack("Unknown Attack");
      rateOfFire = 1;
      maxShotCapacity = 5;
      setMaxChargeNormalTurns(STANDARD_MAX_CHARGE_TIME_STANDARD_TURNS);
      setChargePerShot();
   }
   
   public Weapon(String name, Attack atk)
   {
      this(name);
      attack = atk;
   }
   
   public void setChargePerShot()
   {
      if(getMaxShotCapacity() > 0)
         chargePerShot = getMaxCharge() / getMaxShotCapacity();
      else
         chargePerShot = -1;
   }
   
   
	public void setMaxCharge(int m)
   {
      super.setMaxCharge(m);
      setChargePerShot();
   }
   
   public void setMaxChargeNormalTurns(int t)
   {
      super.setMaxCharge(t);
      setChargePerShot();
   }
   
   public int getChargedShots()
   {
      return getCurCharge() / getChargePerShot();
   }
   
   public void discharge()
   {
      setCurCharge(getCurCharge() - getChargePerShot());
   }
   
   public static Weapon getMock()
   {
      Weapon w = new Weapon("Test Weapon", Attack.getMock());
      return w;
   }
}