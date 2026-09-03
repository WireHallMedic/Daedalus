package Daedalus.Actor;

import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;
import WidlerSuite.WSFontConstants;

public class Actor extends UnboundTile
{
	private String name;
	private AI ai;
   private int charge;
   private boolean dead;
   public static final int FULLY_CHARGED = 10;


	public String getName(){return name;}
	public AI getAI(){return ai;}
   public int getCharge(){return charge;}
   public boolean isDead(){return dead;}


	public void setName(String n){name = n;}
	public void setAI(AI a){ai = a;}
   public void setCharge(int c){charge = c;}

   
   public Actor()
   {
      super(GUIConstants.SQUARE_PALETTE, '?', GUIConstants.CYAN, GUIConstants.ORANGE);
      setLowerTileIndex(WSFontConstants.CIRCLE_TILE);
      ai = new AI(this);
      name = "Unknown Actor";
      charge = 0;
   }
      
   @Override
	public void setTileLoc(int x, int y)
   {
      Coord prevLoc = getTileLoc();
      super.setTileLoc(x, y);
      Game.setPlayerPosition(this, prevLoc);
   }
   
   
   // initiative
   public void charge()
   {
      if(charge < FULLY_CHARGED)
         charge++;
   }
   
   public boolean isCharged()
   {
      return charge >= FULLY_CHARGED;
   }
   
   public void discharge(int amt)
   {
      charge -= amt;
   }
   
   // health
   public void die()
   {
      dead = true;
   }
   
   // AI stuff
   public boolean hasPlan(){return ai.hasPlan();}
   public void plan(){ai.plan();}
   public void clearPlan(){ai.clearPlan();}
   public void act(){ai.act();}
}