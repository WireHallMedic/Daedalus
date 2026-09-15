package Daedalus.AI;

import Daedalus.GUI.*;
import Daedalus.Zone.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import Daedalus.Combat.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;
import WidlerSuite.AStar;
import WidlerSuite.StraightLine;
import java.util.*;

public class AI implements AIConstants, ZoneConstants
{
	protected Actor self;
	protected Coord pendingTarget;
	protected ActorAction pendingAction;
   protected int pendingIndex;            // used for supplementary information
   protected Team team;
   protected static boolean[][] passMap = null;    // we only need one pathing map, since actors never access it concurrently
   protected static Coord cornerLoc = null;
   protected Memory memory;

	public Actor getSelf(){return self;}
	public Coord getPendingTarget(){return new Coord(pendingTarget);}
	public ActorAction getPendingAction(){return pendingAction;}
   public int getPendingIndex(){return pendingIndex;}
   public Team getTeam(){return team;}
   public Memory getMemory(){return memory;}


	public void setSelf(Actor s){self = s;}
	public void setPendingTarget(Coord p){setPendingTarget(p.x, p.y);}
	public void setPendingTarget(int x, int y){pendingTarget = new Coord(x, y);}
	public void setPendingAction(ActorAction p){pendingAction = p;}
   public void setPendingIndex(int p){pendingIndex = p;}
   public void setTeam(Team t){team = t;}
   public void setMemory(Memory m){memory = m;}

   public AI(Actor a)
   {
      self = a;
      team = Team.EVIL;
      memory = new Memory(self);
      clearPlan();
   }
   
   // planning
   //////////////////////////////////////////////////
	public void setPendingTarget(Direction dir)
   {
      Coord loc = dir.getAsCoord();
      loc.add(self.getTileLoc());
      
      // check validity if interacting
      if(pendingAction == ActorAction.INTERACT)
      {
         if(Game.getCurZone().getTile(loc) instanceof ToggleTile)
            pendingTarget = loc;
         else
         {
            MainGamePanel.clearMessage();
            MainGamePanel.addMessage("Nothing to interact with there.", true);
            clearPlan();
         }
      }
      
      // check validity if picking up
      if(pendingAction == ActorAction.PICK_UP)
      {
         if(Game.getCurZone().isItemAt(loc))
         {
            // item is credits, or actor has room
            if(Game.getCurZone().getItemAt(loc) instanceof Credits || !self.getInventory().isFull())
               pendingTarget = loc;
            // no room
            else
            {
               MainGamePanel.clearMessage();
               MainGamePanel.addMessage("Your inventory is full.", true);
               clearPlan();
            }
         }
         else
         {
            MainGamePanel.clearMessage();
            MainGamePanel.addMessage("Nothing to pick up here.", true);
            clearPlan();
         }
      }
      // if no check needed, just assign loc
      else
      {
         pendingTarget = loc;
      }
   }
   
   public boolean hasPlan()
   {
      // basic check
      boolean plan = pendingTarget != null && pendingAction != null && pendingAction != ActorAction.CONTEXTUAL;
      
      // check thins that need pendingIndex have it
      if(pendingAction == ActorAction.DROP || pendingAction == ActorAction.USE)
         if(pendingIndex == -1)
            plan = false;
      
      return plan;
   }
   
   public void plan()
   {
      pendingTarget = new Coord();
      pendingAction = ActorAction.DELAY;
      pendingIndex = -1;
   }
   
   public void clearPlan()
   {
      pendingTarget = null;
      pendingAction = null;
   }
   
   public Coord getDumbstepTowards(Coord target)
   {
      Direction dir = Direction.getDirectionTo(self.getTileLoc(), target);
      Coord targetTile = self.getTileLoc();
      targetTile.add(dir.getAsCoord());
      if(Game.canStep(self, targetTile))
         return targetTile;
      targetTile = self.getTileLoc();
      targetTile.add(dir.nextClockwise().getAsCoord());
      if(Game.canStep(self, targetTile))
         return targetTile;
      targetTile = self.getTileLoc();
      targetTile.add(dir.prevClockwise().getAsCoord());
      if(Game.canStep(self, targetTile))
         return targetTile;
      return null;
   }
   public Coord getDumbstepTowards(Actor target){return getDumbstepTowards(target.getTileLoc());}
   
   public Coord getStepTowards(Coord target)
   {
      Vector<Coord> path = getPathTo(target);
      if(path.size() > 0)
         return path.elementAt(0);
      return null;
   }
   public Coord getStepTowards(Actor target){return getStepTowards(target.getTileLoc());}
   
   
   public boolean isEnemy(Actor that)
   {
      return team.isEnemy(that.getAI().getTeam()) && that != self;
   }
   
   // acting
   //////////////////////////////////////////////////
   
   public void act()
   {
      switch(pendingAction)
      {
         case ActorAction.DELAY :
            doDelay(); 
            break;
         case ActorAction.STEP :
            doStep(); 
            break;
         case ActorAction.INTERACT :
            doInteract();
            break;
         case ActorAction.PICK_UP :
            doPickUp();
            break;
         case ActorAction.DROP :
            doDrop();
            break;
         case ActorAction.BASIC_ATTACK :
            doBasicAttack();
            break;
         case ActorAction.NATURAL_ATTACK :
            doNaturalAttack();
            break;
         case ActorAction.SWAP_WEAPONS :
            doWeaponSwap();
            break;
         case ActorAction.EQUIP :
            doEquip();
            break;
      }
      clearPlan();
   }
   
   protected void doDelay()
   {
      self.discharge(self.getMoveSpeed());
   }
   
   protected void doStep()
   {
      Direction stepDir = Direction.getFromCoord(new Coord(pendingTarget.x - self.getTileLoc().x, 
                                                pendingTarget.y - self.getTileLoc().y));
      self.setTileLoc(pendingTarget);
      self.setXOffset(0.0 - stepDir.x);
      self.setYOffset(0.0 - stepDir.y);
      AnimationScript as = AnimationScriptFactory.getStep(self, stepDir);
      AnimationManager.addSemiLocking(as);
      self.discharge(self.getMoveSpeed());
      if(self == Game.getPlayer() && Game.getCurZone().isItemAt(self.getTileLoc()))
      {
         MainGamePanel.clearMessage();
         String itemName = Game.getCurZone().getItemAt(self.getTileLoc()).getNameWithParticle();
         MainGamePanel.addMessage("You are standing on " + itemName + ".");
      }
   }
   
   protected void doInteract()
   {
      Game.getCurZone().toggle(pendingTarget);
      self.discharge(self.getInteractSpeed());
   }
   
   protected void doPickUp()
   {
      Item item = Game.getCurZone().takeItemAt(pendingTarget);
      self.addToInventory(item);
      self.discharge(self.getInteractSpeed());
      if(self == Game.getPlayer())
      {
         MainGamePanel.clearMessage();
         MainGamePanel.addMessage("You picked up " + item.getNameWithParticle() + ".");
      }
      AnimationScriptFactory.addPickupEffect(item, pendingTarget);
   }
   
   protected void doDrop()
   {
      Item item = self.getInventory().takeItem(pendingIndex);
      Game.getCurZone().dropItem(item, pendingTarget);
      self.discharge(self.getInteractSpeed());
   }
   
   protected void doWeaponSwap()
   {
      self.swapWeapons();
      self.discharge(self.getInteractSpeed());
   }
   
   protected void doBasicAttack()
   {
      doWeaponAttack(self.getCurWeapon());
   }
   
   protected void doNaturalAttack()
   {
      doWeaponAttack(self.getNaturalWeapon());
   }
   
   protected void doWeaponAttack(Weapon w)
   {
      CombatManager.resolveAttack(self, w.getAttack(), pendingTarget, w.getRateOfFire());
      if(w != self.getNaturalWeapon())
         w.discharge();
      self.discharge(self.getAttackSpeed());
   }
   
   protected void doEquip()
   {
      Item item = self.getInventory().takeItem(pendingIndex);
      if(item instanceof Weapon)
      {
         if(self.getCurWeapon() != null)
            self.getInventory().add(self.getCurWeapon());
         self.setCurWeapon((Weapon)item);
      }
      if(item instanceof Shield)
      {
         if(self.getShield() != null)
            self.getInventory().add(self.getShield());
         self.setShield((Shield)item);
      }
      if(item instanceof Armor)
      {
         if(self.getArmor() != null)
            self.getInventory().add(self.getArmor());
         self.setArmor((Armor)item);
      }
      self.discharge(self.getInteractSpeed());
   }
   
   protected Vector<Coord> getPathTo(Coord target)
   {
      AStar aStar = new AStar();
      setPassMap(target);
      Coord origin = self.getTileLoc();
      target = target.copy();
      origin.subtract(cornerLoc);
      target.subtract(cornerLoc);
      Vector<Coord> path = aStar.path(passMap, origin, target);
      for(int i = 0; i < path.size(); i++)
         path.elementAt(i).add(cornerLoc);
      return path;
   }
   
   // you need to set the passmap before calling this; it is not done within the function
   // because it may be called many times on the same map
   private boolean hasLineOfEffect(Coord origin, Coord target)
   {
      origin = new Coord(origin.x - cornerLoc.x, origin.y - cornerLoc.y);
      target = new Coord(target.x - cornerLoc.x, target.y - cornerLoc.y);
      
      Vector<Coord> sightLine = StraightLine.findLine(origin, target, StraightLine.REMOVE_ORIGIN_AND_TARGET);
      for(int i = 0; i < sightLine.size(); i++)
      {
         if(!passMap[sightLine.elementAt(i).x][sightLine.elementAt(i).y])
            return false;
      }
      
      return true;
   }
   
   private void setPassMap(Coord target)
   {
      if(passMap == null)
         passMap = new boolean[PATHING_SEARCH_DIAMETER][PATHING_SEARCH_DIAMETER];
      int searchRadius = PATHING_SEARCH_DIAMETER / 2;
      cornerLoc = new Coord(self.getTileLoc().x - searchRadius, self.getTileLoc().y - searchRadius);
      for(int x = 0; x < PATHING_SEARCH_DIAMETER; x++)
      for(int y = 0; y < PATHING_SEARCH_DIAMETER; y++)
      {
         passMap[x][y] = Game.getCurZone().canStep(self, x + cornerLoc.x, y + cornerLoc.y);
      }
      for(int i = 0; i < Game.getActorList().size(); i++)
      {
         Actor a = Game.getActorList().elementAt(i);
         if(isInPathSearchArea(self.getTileLoc(), a, searchRadius) &&
            a != self)
         {
            passMap[a.getTileLoc().x - cornerLoc.x][a.getTileLoc().y - cornerLoc.y] = false;
         }
      }
      // set target as passable
      if(isInPathSearchArea(self.getTileLoc(), target, searchRadius))
         passMap[target.x - cornerLoc.x][target.y - cornerLoc.y] = true;
   }
   
   private boolean isInPathSearchArea(Coord center, Coord prospect, int searchRadius)
   {
      return Math.abs(center.x - prospect.x) <= searchRadius &&
             Math.abs(center.y - prospect.y) <= searchRadius;
   }
   private boolean isInPathSearchArea(Coord center, Actor prospect, int searchRadius){return isInPathSearchArea(center, prospect.getTileLoc(), searchRadius);}
   
   
   // memory
   //////////////////////////////////////////////////
   
   protected Actor getClosestEnemy()
   {
      int curDist = 1000000;
      Actor curActor = null;
      Vector<Actor> enemyList = memory.getEnemyList();
      for(int i = 0; i < enemyList.size(); i++)
      {
         Actor a = enemyList.elementAt(i);
         if(EngineTools.getAngbandDistance(self.getTileLoc(), a.getTileLoc()) < curDist)
         {
            curActor = a;
            curDist = EngineTools.getAngbandDistance(self.getTileLoc(), a.getTileLoc());
         }
      }
      return curActor;
   }
   
   public void updateMemory()
   {
      int searchRadius = FOV_SEARCH_DIAMETER / 2;
      int xOrigin = self.getTileLoc().x - searchRadius;
      int yOrigin = self.getTileLoc().y - searchRadius;
      Actor a = null;
      for(int x = 0; x < FOV_SEARCH_DIAMETER; x++)
      for(int y = 0; y < FOV_SEARCH_DIAMETER; y++)
      {
         a = Game.getActorAt(xOrigin + x, yOrigin + y);
         if(a != null && self.canSee(xOrigin + x, yOrigin + y) && a != self)
            memory.notice(a);
      }
   }
   
   public void incrementMemory()
   {
      memory.increment();
   }
   
   public void cleanMemory()
   {
      memory.cleanLists();
   }
   
   public void notice(Actor a)
   {
      memory.notice(a);
   }
}