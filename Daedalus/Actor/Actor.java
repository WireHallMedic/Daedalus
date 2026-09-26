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
import java.util.*;

public class Actor extends UnboundTile implements ActorConstants, ScriptListener, ZoneConstants
{
	private String name;
	private AI ai;
   private int charge;
   private boolean dead;
   private Inventory inventory;
   private StatBlock baseStats;
   private ShadowFoV fov;
   private ZoneMap curMap;      // used to know when stuff needs to be updated
   private boolean turnHasStarted;
   private int curHealth;
	private Weapon weapon1;
	private Weapon weapon2;
   private Weapon naturalWeapon;
	private boolean weaponSelection;
   private Shield shield;
   private Armor armor;
   private int knockbackDistance;
   private Direction knockbackDirection;
   private ActorPack pack;
   private Vector<StatusEffect> statusEffectList;
   private int threat;


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
   public Weapon getNaturalWeapon(){return naturalWeapon;}
	public boolean isWeaponSelection(){return weaponSelection;}
   public Shield getShield(){return shield;}
   public Armor getArmor(){return armor;}
   public ActorPack getPack(){return pack;}
   public boolean hasPack(){return pack != null;}
   public Vector<StatusEffect> getStatusEffectList(){return statusEffectList;}
   public int getThreat(){return threat;}


	public void setName(String n){name = n;}
	public void setAI(AI a){ai = a;}
   public void setCharge(int c){charge = c;}
   public void setBaseStats(StatBlock bs){baseStats = bs;}
   public void setFoV(ShadowFoV f){fov = f;}
   public void setCurHealth(int ch){curHealth = ch;}
	public void setWeapon1(Weapon w){weapon1 = w;}
	public void setWeapon2(Weapon w){weapon2 = w;}
   public void setNaturalWeapon(Weapon w){naturalWeapon = w;}
	public void setWeaponSelection(boolean w){weaponSelection = w;}
   public void setShield(Shield s){shield = s;}
   public void setArmor(Armor a){armor = a;}
   public void setPack(ActorPack p){pack = p;}
   public void setCurMap(ZoneMap map){curMap = map;}
   public void setStatusEffectList(Vector<StatusEffect> list){statusEffectList = list;}
   public void setThreat(int t){threat = t;}

   
   public Actor()
   {
      super(GUIConstants.SQUARE_PALETTE, '?', GUIConstants.CYAN, GUIConstants.ORANGE);
      setLowerTileIndex(WSFontConstants.CIRCLE_TILE);
      ai = new AI(this);
      name = "Unknown Actor";
      charge = STARTING_CHARGE;
      inventory = new Inventory(this);
      baseStats = new StatBlock();
      ShadowFoV fov = null;
      curMap = null;
      turnHasStarted = false;
      knockbackDistance = 0;
      knockbackDirection = null;
      pack = null;
      
      // items
   	weapon1 = null;
   	weapon2 = null;
      naturalWeapon = WeaponFactory.getBasicMelee();
      shield = null;
      armor = null;
      weaponSelection = true;
      
      //stats and status effects
      baseStats.setMaxHealth(10);
      baseStats.setVisionRadius(10);
      
      statusEffectList = new Vector<StatusEffect>();
      
      threat = 0;
      
      fullHeal();
   }
   
   public Actor(String n)
   {
      this();
      setName(n);
   }
      
   
	public void setTileLoc(int x, int y, boolean updateActorMap)
   {
      Coord prevLoc = getTileLoc();
      super.setTileLoc(x, y);
      if(updateActorMap)
         Game.setActorPosition(this, prevLoc);
   }
   @Override public void setTileLoc(Coord c){setTileLoc(c.x, c.y, true);}
   @Override public void setTileLoc(int x, int y){setTileLoc(x, y, true);}
   public void setTileLoc(Coord c, boolean updateActorMap){setTileLoc(c.x, c.y, updateActorMap);}
   
   
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
      if(hasArmor())
         armor.charge();
      incrementStatusEffects();
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
         if(curMap != Game.getCurMap())
         {
            curMap = Game.getCurMap();
            fov = new ShadowFoV(curMap.getVisibilityMap());
         }
         turnHasStarted = true;
         updateFoV();
         ai.incrementMemory();
         ai.updateMemory();
         ai.cleanMemory();
         if(!(ai instanceof PlayerAI))
            ai.manageAlertness();
      }
   }
   
   public void endOfTurn()
   {
      ai.updateMemory();
      turnHasStarted = false;
   }
   
   // vision
   public void updateFoV()
   {
      if(fov == null || curMap != Game.getCurMap())
      {
         curMap = Game.getCurMap();
         fov = new ShadowFoV(curMap.getVisibilityMap());
      }
      fov.calcFoV(getTileLoc().x, getTileLoc().y, getVisionRadius());
      if(this == Game.getPlayer())
         updateLastSeenMap();
   }
   
   public boolean canSee(int x, int y)
   {
      if(curMap == null)
         return false;
      if(fov == null)
      {
         fov = new ShadowFoV(curMap.getVisibilityMap());
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
            curMap.setLastSeen(x, y);
         }
      }
   }
   
   // stat block
   public int getMaxHealth(){return baseStats.getMaxHealth();}
	public int getVisionRadius(){return baseStats.getVisionRadius();}
	public ActionSpeed getMoveSpeed(){return baseStats.getMoveSpeed();}
	public ActionSpeed getAttackSpeed(){return baseStats.getAttackSpeed();}
	public ActionSpeed getInteractSpeed(){return baseStats.getInteractSpeed();}
   public boolean isFlying(){return baseStats.isFlying();}
   
   
   // status effects
   private void incrementStatusEffects()
   {
      for(int i = 0; i < statusEffectList.size(); i++)
      {
         statusEffectList.elementAt(i).increment();
         if(statusEffectList.elementAt(i).isExpired())
         {
            if(this == Game.getPlayer())
               MainGamePanel.addMessage("You are no longer " + statusEffectList.elementAt(i).getName() + ". ");
            statusEffectList.removeElementAt(i);
            i--;
         }
         else
         {
            statusEffectList.elementAt(i).applyTags(this);
         }
      }
   }
   
   public void add(StatusEffect se)
   {
      statusEffectList.add(se);
   }
   
   // health
   public void die()
   {
      dead = true;
      if(this != Game.getPlayer())
         dropAllItems();
      if(hasPack())
         pack.removeMember(this);
   }
   
   public void fullHeal()
   {
      curHealth = getMaxHealth();
   }
   
   public void heal(int val)
   {
      curHealth = Math.min(getMaxHealth(), curHealth + val);
   }
   
   
   // returns the damage taken, including shield damage
   // initial damage is absorbed by shield, then reduced by armor. We have to move things around 
   // a little because armor cares about damage subtypes and shields don't.
   public int applyDamage(Damage damage, double damageMultiplier)
   {
      boolean checkShieldBreak = getCurShield() > 0;
      int ablatedDamage = 0;
      int healthDamage = 0;
      // note how much blocked by shield
      if(hasShield())
         ablatedDamage = getShield().applyDamage((int)(damage.getSum() * damageMultiplier));
      // reduce by armor
      if(hasArmor())
         damage = getArmor().absorbDamage(damage);
      // apply shield ablation and apply to health
      healthDamage = Math.max(0, (int)(damage.getSum() * damageMultiplier) - ablatedDamage);
      
      curHealth = Math.max(0, curHealth - healthDamage);
      
      if(checkShieldBreak && getCurShield() == 0)
         AnimationScriptFactory.addShieldParticles(this);
         
      if(curHealth == 0)
         die();
      return healthDamage + ablatedDamage;
   }
   public int applyDamage(Damage damage){return applyDamage(damage, 1.0);}
   
   // AI stuff
   public boolean hasPlan(){return ai.hasPlan();}
   public void plan(){ai.plan();}
   public void clearPlan(){ai.clearPlan();}
   public void act(){ai.act();}
   

   public void notice(Actor a, boolean alertFriends)
   {
      ai.notice(a, alertFriends);
   }
   public void notice(Actor a){notice(a, true);}
   
   // items
   public void addToInventory(Item item)
   {
      inventory.add(item);
   }
   
   public void dropAllItems()
   {
      Credits credits = inventory.getCredits();
      while(inventory.size() > 0)
         Game.getCurMap().dropItem(getInventory().takeItem(0), getTileLoc());
      if(inventory.getCredits().getValue() > 0)
      {
         Game.getCurMap().dropItem(new Credits(inventory.getCredits()), getTileLoc());
         inventory.getCredits().setValue(0);
      }
      if(weapon1 != null && weapon1.isDroppable())
      {
         Game.getCurMap().dropItem(weapon1, getTileLoc());
         weapon1 = null;
      }
      if(weapon2 != null && weapon2.isDroppable())
      {
         Game.getCurMap().dropItem(weapon2, getTileLoc());
         weapon2 = null;
      }
      if(shield != null && shield.isDroppable())
      {
         Game.getCurMap().dropItem(shield, getTileLoc());
         shield = null;
      }
      if(armor != null && armor.isDroppable())
      {
         Game.getCurMap().dropItem(armor, getTileLoc());
         armor = null;
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
      if(weaponSelection && weapon1 != null)
         return weapon1;
      if(weapon2 != null)
         return weapon2;
      return naturalWeapon;
   }
   
   public Weapon getOffWeapon()
   {
      if(weaponSelection)
         return weapon2;
      return weapon1;
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
      return getCurWeapon().getAttack();
   }
   
   public Attack getNaturalAttack()
   {
      return getNaturalWeapon().getAttack();
   }
   
   
   // knockback
   public void setKnockback(int distance, Direction dir)
   {
      knockbackDistance = distance;
      knockbackDirection = dir;
      if(knockbackDistance > 0)
         resolveKnockbackStep();
   }
   
   public void scriptExpiring(AnimationScript source)
   {
      if(knockbackDistance > 0)
      {
         resolveKnockbackStep();
      }
   }
   
   private void resolveKnockbackStep()
   {
      Coord targetTile = getTileLoc();
      targetTile.add(knockbackDirection.getAsCoord());
      knockbackDistance--;
      if(Game.canStep(this, targetTile))
      {
         setTileLoc(targetTile);
         setXOffset(0.0 - knockbackDirection.x);
         setYOffset(0.0 - knockbackDirection.y);
         AnimationScript as = AnimationScriptFactory.getKnockback(this, knockbackDirection);
         as.addScriptListener(this);
         AnimationManager.addLocking(as);
      }
      else
      {
         knockbackDistance = 0;
         if(this == Game.getPlayer())
            AnimationManager.setScreenShake();
      }
   }
}