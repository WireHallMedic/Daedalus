package Daedalus.Actor;

import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Zone.*;
import Daedalus.Combat.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;
import WidlerSuite.WSFontConstants;
import WidlerSuite.ShadowFoV;
import WidlerSuite.ShadowFoVRect;

public class Actor extends UnboundTile implements ActorConstants
{
	private String name;
	private AI ai;
   private int charge;
   private boolean dead;
   private Inventory inventory;
   private StatBlock baseStats;
   private ShadowFoV fov;
   private ZoneMap curZone;      // used to know when stuff needs to be updated
   private boolean turnHasStarted;
   private int curHealth;


	public String getName(){return name;}
	public AI getAI(){return ai;}
   public int getCharge(){return charge;}
   public boolean isDead(){return dead;}
   public Inventory getInventory(){return inventory;}
   public StatBlock getBaseStats(){return baseStats;}
   public ShadowFoV getFoV(){return fov;}
   public int getCurHealth(){return curHealth;}


	public void setName(String n){name = n;}
	public void setAI(AI a){ai = a;}
   public void setCharge(int c){charge = c;}
   public void setBaseStats(StatBlock bs){baseStats = bs;}
   public void setFoV(ShadowFoV f){fov = f;}
   public void setCurHealth(int ch){curHealth = ch;}

   
   public Actor()
   {
      super(GUIConstants.SQUARE_PALETTE, '?', GUIConstants.CYAN, GUIConstants.ORANGE);
      setLowerTileIndex(WSFontConstants.CIRCLE_TILE);
      ai = new AI(this);
      name = "Unknown Actor";
      charge = 0;
      inventory = new Inventory(this);
      baseStats = new StatBlock();
      ShadowFoV fov = null;
      curZone = null;
      turnHasStarted = false;
      
      baseStats.setMaxHealth(10);
      baseStats.setVisionRadius(10);
      fullHeal();
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
   
   public void discharge(ActionSpeed speed)
   {
      discharge(speed.increments);
   }
   
   public void startOfTurn()
   {
      // flag to ensure only runs once per turn
      if(!turnHasStarted)
      {
         // do stuff if we're on a new map
         if(curZone != Game.getCurZone())
         {
            curZone = Game.getCurZone();
            fov = new ShadowFoVRect(curZone.getVisibilityMap());
         }
         turnHasStarted = true;
         updateFoV();
      }
   }
   
   public void endOfTurn()
   {
      turnHasStarted = false;
   }
   
   // vision
   public void updateFoV()
   {
      fov.calcFoV(getTileLoc().x, getTileLoc().y, getVisionRadius());
      if(this == Game.getPlayer())
         updateLastSeenMap();
   }
   
   public boolean canSee(int x, int y)
   {
      return fov.isVisible(x, y);
   }
   public boolean canSee(Actor a){return canSee(a.getTileLoc());}
   public boolean canSee(Coord c){return canSee(c.x, c.y);}
   
   private void updateLastSeenMap()
   {
      for(int x = getTileLoc().x - getVisionRadius(); x < getTileLoc().x + getVisionRadius(); x++)
      for(int y = getTileLoc().y - getVisionRadius(); y < getTileLoc().y + getVisionRadius(); y++)
      {
         if(canSee(x, y))
         {
            curZone.setLastSeen(x, y);
         }
      }
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
   
   public void fullHeal()
   {
      curHealth = getMaxHealth();
   }
   
   // returns the damage dealth
   public int applyDamage(Damage d)
   {
      curHealth = Math.max(0, curHealth - d.getSum());
      
      if(curHealth == 0)
         die();
      return d.getSum();
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