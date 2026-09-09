package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;

public abstract class ChargeItem extends Item implements ItemConstants, GUIConstants
{
	private int chargePerTurn;
	private int maxCharge;
	private int curCharge;


	public int getChargePerTurn(){return chargePerTurn;}
	public int getMaxCharge(){return maxCharge;}
	public int getCurCharge(){return curCharge;}


	public void setChargePerTurn(int c){chargePerTurn = c;}
	public void setMaxCharge(int m){maxCharge = m;}
	public void setCurCharge(int c){curCharge = c;}
   
   
   public ChargeItem(String name, int tileIndex, int fgColor)
   {
      super(name, tileIndex, fgColor);
      setChargePerTurn(STANDARD_CHARGE_PER_TURN);
      setMaxChargeStandardTurns(10);
      fullyCharge();
   }
   public ChargeItem(String name, int tileIndex){this(name, tileIndex, WHITE);}
   public ChargeItem(String name, ItemBase base){this(name, base.tileIndex, WHITE);}
   public ChargeItem(String name, ItemBase base, int fgColor){this(name, base.tileIndex, fgColor);}
   
   public void setMaxChargeStandardTurns(int t)
   {
      setMaxCharge(ActorConstants.ActionSpeed.NORMAL.increments * t * getChargePerTurn());
   }
   
   public void fullyCharge()
   {
      setCurCharge(getMaxCharge());
   }
   
   public void charge()
   {
      curCharge = Math.min(curCharge + (chargePerTurn / 2), maxCharge);
   }
}