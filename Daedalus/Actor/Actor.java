package Daedalus.Actor;

import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Zone.*;
import Daedalus.Combat.*;
import Daedalus.Engine.*;
import Daedalus.Ability.*;
import java.util.*;

public class Actor extends UnboundTile implements ActorConstants, ScriptListener, ZoneConstants
{
	private AI ai;
   private int charge;
   private boolean dead;
   private Inventory inventory;
   private StatBlock baseStats;
   private StatBlock curStats;
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


	public AI getAI(){return ai;}
   public int getCharge(){return charge;}
   public boolean isDead(){return dead;}
   public Inventory getInventory(){return inventory;}
   public StatBlock getBaseStats(){return baseStats;}
   public StatBlock getCurStats(){return curStats;}
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
      setLowerTileIndex(FontConstants.CIRCLE_TILE);
      ai = new AI(this);
      setName("Unknown Actor");
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
      setCurStats();
      
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
            fov = new ShadowFoV(curMap.getVisibilityMap(), this);
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
         fov = new ShadowFoV(curMap.getVisibilityMap(), this);
      }
      fov.calcFoV(this);
      if(this == Game.getPlayer())
         updateLastSeenMap();
   }
   
   public boolean canSee(int x, int y)
   {
      if(curMap == null)
         return false;
      if(fov == null)
      {
         fov = new ShadowFoV(curMap.getVisibilityMap(), this);
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
   public int getMaxHealth(){return curStats.getMaxHealth();}
	public ActionSpeed getMoveSpeed(){return curStats.getMoveSpeed();}
	public ActionSpeed getAttackSpeed(){return curStats.getAttackSpeed();}
	public ActionSpeed getInteractSpeed(){return curStats.getInteractSpeed();}
   public boolean isFlying(){return curStats.isFlying();}
   
   
   public void setCurStats()
   {
      StatBlock newBlock = new StatBlock(baseStats);
      for(StatusEffect se: statusEffectList)
         newBlock.add(se.getStatBlock());
      curStats = newBlock;
   }
   
   
	public int getVisionRadius()
   {
      int v = baseStats.getVisionRadius();
      if(ai.getAlertness() == AIConstants.Alertness.INERT)
         v = Math.max(1, v - 2);
      return v;
   }
   
   
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
      setCurStats();            
      if(isBurning())
      {
         Damage d = new Damage(CombatConstants.DamageType.FIRE, getBurning());
         applyDamage(d);
      }
   }
   
   public void add(StatusEffect se)
   {
      boolean combined = false;
      for(StatusEffect existingEffect: statusEffectList)
      {
         if(existingEffect.canCombine(se))
         {
            existingEffect.combine(se);
            combined = true;
            break;
         }
      }
      if(!combined)
      {
         statusEffectList.add(se);
         for(int i = 0; i < se.getTagList().size(); i++)
            AnimationScriptFactory.addStatusEffectVE(this, se.getTagList().elementAt(i));
      }
      setCurStats();
   }
   
   public boolean hasStatusEffectTag(AbilityConstants.StatusEffectTag tag)
   {
      for(StatusEffect se: statusEffectList)
         if(se.hasTag(tag))
            return true;
      return false;
   }
   
   // returns the amount the actor is burning, 0 if not burning
   public int getBurning()
   {
      int burning = 0;
      for(StatusEffect se: statusEffectList)
      {
         if(se.hasTag(AbilityConstants.StatusEffectTag.BURNING))
         {
            burning = Math.max(burning, se.getIntensity());
         }
      }
      return burning;
   }
   
   public boolean isBurning()
   {
      return getBurning() > 0;
   }
   
   // returns the amount the actor is burning, 0 if not burning
   public int getVulnerability()
   {
      int vulnerability = 0;
      for(StatusEffect se: statusEffectList)
      {
         if(se.hasTag(AbilityConstants.StatusEffectTag.VULNERABLE))
         {
            vulnerability = Math.max(vulnerability, se.getIntensity());
         }
      }
      return vulnerability;
   }
   
   public boolean isVulnerable()
   {
      return getVulnerability() > 0;
   }
   
   public String getStatusEffectString(boolean withTimes)
   {
      String str = "";
      if(statusEffectList.size() > 0)
      {
         str += statusEffectList.elementAt(0).getName();
         if(withTimes)
            str += " " + GUITools.turnsToSeconds(statusEffectList.elementAt(0).getRemainingDuration());
      }
      for(int i = 1; i < statusEffectList.size(); i++)
      {
         str += ", " + statusEffectList.elementAt(i).getName();
         if(withTimes)
            str += " " + GUITools.turnsToSeconds(statusEffectList.elementAt(0).getRemainingDuration());
      }
      return str;
   }
   public String getStatusEffectString(){return getStatusEffectString(false);}
   
   
   // health
   public void die()
   {
      dead = true;
      if(this != Game.getPlayer())
         dropAllItems();
      if(hasPack())
         pack.removeMember(this);
      Game.getCurMap().dropCorpse(new Corpse(this), this.getTileLoc());
   }
   
   public void fullHeal()
   {
      setCurStats();
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
         damage = getArmor().absorbDamage(damage, getVulnerability());
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
   
   public void scriptExpiring(GroundAnimationScript source)
   {
      if(source.getScriptListenerNote() == AbilityConstants.KNOCKBACK_TAG &&
         knockbackDistance > 0)
      {
         resolveKnockbackStep();
      }
      else if(source.getScriptListenerNote() instanceof AbilityConstants.StatusEffectTag)
      {
         AbilityConstants.StatusEffectTag tag = (AbilityConstants.StatusEffectTag)source.getScriptListenerNote();
         if(hasStatusEffectTag(tag))
            AnimationScriptFactory.addStatusEffectVE(this, tag);
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