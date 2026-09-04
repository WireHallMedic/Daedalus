package Daedalus.Item;

import Daedalus.GUI.*;

public class Credits extends Item implements ItemConstants, GUIConstants
{
	private int value;


	public int getValue(){return value;}


	public void setValue(int v){value = v;}


   public Credits(int val)
   {
      super("Credits", ItemBase.CREDITS.tileIndex, YELLOW, BLACK);
      value = val;
   }
   
   public void add(int val)
   {
      value += val;
   }
   public void add(Credits that){add(that.getValue());}
   
   public void subtract(int val)
   {
      value -= val;
   }
   public void subtract(Credits that){subtract(that.getValue());}
   
   
   public String getName()
   {
      return value + " Credits";
   }
   
   public String getNameWithParticle()
   {
      return getName();
   }
}