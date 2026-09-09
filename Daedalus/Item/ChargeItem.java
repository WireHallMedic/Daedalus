package Daedalus.Item;

import Daedalus.GUI.*;

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
   }
   public ChargeItem(String name, int tileIndex){this(name, tileIndex, WHITE);}
   public ChargeItem(String name, ItemBase base){this(name, base.tileIndex, WHITE);}
   public ChargeItem(String name, ItemBase base, int fgColor){this(name, base.tileIndex, fgColor);}

}