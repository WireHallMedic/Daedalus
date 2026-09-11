package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;

public abstract class ChargeItem extends Item implements ItemConstants, GUIConstants
{
	private int chargeRate;
	private int maxCharge;
	private int curCharge;
   private boolean alwaysCharged;


	public int getChargeRate(){return chargeRate;}
	public int getMaxCharge(){return maxCharge;}
	public int getCurCharge(){return curCharge;}
   public boolean isAlwaysCharged(){return alwaysCharged;}


	public void setChargeRate(int c){chargeRate = c;}
	public void setMaxCharge(int m){maxCharge = m;}
	public void setCurCharge(int c){curCharge = c;}
   public void setAlwaysCharged(boolean a){alwaysCharged = a;}
   
   
   public ChargeItem(String name, int tileIndex, int fgColor)
   {
      super(name, tileIndex, fgColor);
      setChargeRate(STANDARD_CHARGE_PER_TURN);
      setMaxCharge(10 * STANDARD_CHARGE_PER_TURN);
      alwaysCharged = false;
      fullyCharge();
   }
   public ChargeItem(String name, int tileIndex){this(name, tileIndex, WHITE);}
   public ChargeItem(String name, ItemBase base){this(name, base.tileIndex, WHITE);}
   public ChargeItem(String name, ItemBase base, int fgColor){this(name, base.tileIndex, fgColor);}
   
   
   public void setMaxChargeTurns(int t)
   {
      setMaxCharge(t * getChargeRate() * ActorConstants.ActionSpeed.NORMAL.increments);
   }
   
   
   public void fullyCharge()
   {
      setCurCharge(getMaxCharge());
   }
   
   public void charge()
   {
      curCharge = Math.min(curCharge + chargeRate, maxCharge);
   }
   
   public void discharge(int amt)
   {
      if(!alwaysCharged)
         setCurCharge(getCurCharge() - amt);
   }
}