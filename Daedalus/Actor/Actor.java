package Daedalus.Actor;

import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;
import WidlerSuite.WSFontConstants;

public class Actor extends UnboundTile implements ActorConstants
{
	private String name;
	private AI ai;
   private int charge;
   private boolean dead;
   private Inventory inventory;
   private StatBlock baseStats;


	public String getName(){return name;}
	public AI getAI(){return ai;}
   public int getCharge(){return charge;}
   public boolean isDead(){return dead;}
   public Inventory getInventory(){return inventory;}
   public StatBlock getBaseStats(){return baseStats;}


	public void setName(String n){name = n;}
	public void setAI(AI a){ai = a;}
   public void setCharge(int c){charge = c;}
   public void setBaseStats(StatBlock bs){baseStats = bs;}

   
   public Actor()
   {
      super(GUIConstants.SQUARE_PALETTE, '?', GUIConstants.CYAN, GUIConstants.ORANGE);
      setLowerTileIndex(WSFontConstants.CIRCLE_TILE);
      ai = new AI(this);
      name = "Unknown Actor";
      charge = 0;
      inventory = new Inventory(this);
      baseStats = new StatBlock();
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
   
   // stat block
   public int getMaxHealth(){return baseStats.getMaxHealth();}
	public int getVisionRadius(){return baseStats.getVisionRadius();}
	public ActionSpeed getMoveSpeed(){return baseStats.getMoveSpeed();}
	public ActionSpeed getAttackSpeed(){return baseStats.getAttackSpeed();}
	public ActionSpeed getInteractSpeed(){return baseStats.getInteractSpeed();}
   
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
   
   // items
   public void addToInventory(Item item)
   {
      inventory.add(item);
   }
}