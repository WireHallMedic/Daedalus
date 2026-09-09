package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Ability.*;
import Daedalus.Combat.*;

public class Weapon extends ChargeItem implements ItemConstants, GUIConstants
{
	private Attack attack;
	private int rateOfFire;


	public Attack getAttack(){return attack;}
	public int getRateOfFire(){return rateOfFire;}


	public void setAttack(Attack a){attack = a;}
	public void setRateOfFire(int r){rateOfFire = r;}


   public Weapon(String name)
   {
      super(name, ItemBase.WEAPON);
      attack = new Attack("Unknown Attack");
      rateOfFire = 1;
   }
   
   public Weapon(String name, Attack atk)
   {
      this(name);
      attack = atk;
   }
   
   public static Weapon getMock()
   {
      Weapon w = new Weapon("Test Weapon", Attack.getMock());
      return w;
   }
   
}