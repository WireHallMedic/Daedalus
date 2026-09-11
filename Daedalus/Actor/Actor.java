package Daedalus.Actor;

import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Zone.*;
import Daedalus.Combat.*;
import Daedalus.Engine.*;
import Daedalus.Ability.*;
import WidlerSuite.Coord;
import WidlerSuite.WSFontConstants;
import WidlerSuite.ShadowFoV;
import WidlerSuite.ShadowFoVRect;
import java.util.*;

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
	private Weapon weapon1;
	private Weapon weapon2;
	private boolean weaponSelection;
   private Shield shield;
   private Armor armor;


	public String getName(){return name;}
	public AI getAI(){return ai;}
   public int getCharge(){return charge;}
   public boolean isDead(){return dead;}
   public Inventory getInventory(){return inventory;}
   public StatBlock getBaseStats(){return baseStats;}
   public ShadowFoV getFoV(){return fov;}
   public int getCurHealth(){return curHealth;}
	public Weapon getWeapon1(){return weapon1;}
	public Weapon getWeapon2(){return weapon2;}
	public boolean isWeaponSelection(){return weaponSelection;}
   public Shield getShield(){return shield;}
   public Armor getArmor(){return armor;}


	public void setName(String n){name = n;}
	public void setAI(AI a){ai = a;}
   public void setCharge(int c){charge = c;}
   public void setBaseStats(StatBlock bs){baseStats = bs;}
   public void setFoV(ShadowFoV f){fov = f;}
   public void setCurHealth(int ch){curHealth = ch;}
	public void setWeapon1(Weapon w){weapon1 = w;}
	public void setWeapon2(Weapon w){weapon2 = w;}
	public void setWeaponSelection(boolean w){weaponSelection = w;}
   public void setShield(Shield s){shield = s;}
   public void setArmor(Armor a){armor = a;}

   
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
      
      // items
   	weapon1 = null;
   	weapon2 = null;
      shield = null;
      armor = null;
      
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
      if(weapon1 != null)
         weapon1.charge();
      if(weapon2 != null)
         weapon2.charge();
      if(hasShield())
         shield.charge();
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
      if(curZone == null)
         return false;
      if(fov == null)
      {
         fov = new ShadowFoVRect(curZone.getVisibilityMap());
         updateFoV();
      }
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
      if(this != Game.getPlayer())
         dropAllItems();
   }
   
   public void fullHeal()
   {
      curHealth = getMaxHealth();
   }
   
   
   // returns the damage taken
   // initial damage is absorbed by shield, then reduced by armor. We have to move things around 
   // a little because armor cares about damage subtypes and shields don't.
   public int applyDamage(Damage damage)
   {
      int ablatedDamage = 0;
      int healthDamage = 0;
      // note how much blocked by shield
      if(hasShield())
         ablatedDamage = getShield().applyDamage(damage.getSum());
      // reduce by armor
      if(hasArmor())
         damage = getArmor().absorbDamage(damage);
      // apply shield ablation and apply to health
      healthDamage = Math.max(0, damage.getSum() - ablatedDamage);
      
      curHealth = Math.max(0, curHealth - healthDamage);
      
      if(curHealth == 0)
         die();
      return healthDamage;
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
   
   public void dropAllItems()
   {
      Credits credits = inventory.getCredits();
      while(inventory.size() > 0)
         Game.getCurZone().dropItem(getInventory().takeItem(0), getTileLoc());
      if(inventory.getCredits().getValue() > 0)
      {
         Game.getCurZone().dropItem(new Credits(inventory.getCredits()), getTileLoc());
         inventory.getCredits().setValue(0);
      }
      
   }
   
   public boolean hasShield()
   {
      return shield != null;
   }
   
   public int getCurShield()
   {
      if(hasShield())
         return shield.getCurDamageCapacity();
      return 0;
   }
   
   public int getMaxShield()
   {
      if(hasShield())
         return shield.getMaxDamageCapacity();
      return 0;
   }
   
   public Weapon getCurWeapon()
   {
      if(weaponSelection)
         return weapon1;
      return weapon2;
   }
   
   public void setCurWeapon(Weapon w)
   {
      if(weaponSelection)
         weapon1 = w;
      else
         weapon2 = w;
   }
   
   public void swapWeapons()
   {
      weaponSelection = !weaponSelection;
   }
   
   public boolean hasArmor()
   {
      return armor != null;
   }
   
   public Attack getBasicAttack()
   {
      if(getCurWeapon() == null)
         return null;
      return getCurWeapon().getAttack();
   }
}