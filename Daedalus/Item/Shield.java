package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Ability.*;
import Daedalus.Combat.*;

public class Shield extends ChargeItem implements ItemConstants, GUIConstants
{
	private int maxDamageCapacity;
	private int curDamageCapacity;
	private int chargeDelay;
	private double chargePerDamage;


	public int getMaxDamageCapacity(){return maxDamageCapacity;}
	public int getCurDamageCapacity(){return curDamageCapacity;}
	public int getChargeDelay(){return chargeDelay;}
	public double getChargePerDamage(){return chargePerDamage;}


	public void setMaxDamageCapacity(int m){maxDamageCapacity = m;}
	public void setCurDamageCapacity(int c){curDamageCapacity = c;}
	public void setChargeDelay(int c){chargeDelay = c;}
	public void setChargePerDamage(){chargePerDamage = (double)getMaxCharge() / (double)getMaxDamageCapacity();}


   public Shield(String name)
   {
      super(name, ItemBase.SHIELD);
      maxDamageCapacity = 10;
      curDamageCapacity = 0;
      setChargeDelayStandardTurns(5);
      setChargePerDamage();
   }
   
   public void setChargeDelayStandardTurns(int cd)
   {
      chargeDelay = ActorConstants.ActionSpeed.NORMAL.increments * cd;
   }
}