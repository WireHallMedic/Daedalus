package Daedalus.AI;

import Daedalus.GUI.*;
import Daedalus.Zone.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;

public class AI implements AIConstants, ZoneConstants
{
	protected Actor self;
	protected Coord pendingTarget;
	protected ActorAction pendingAction;
   protected int pendingIndex;            // used for supplementary information


	public Actor getSelf(){return self;}
	public Coord getPendingTarget(){return new Coord(pendingTarget);}
	public ActorAction getPendingAction(){return pendingAction;}
   public int getPendingIndex(){return pendingIndex;}


	public void setSelf(Actor s){self = s;}
	public void setPendingTarget(Coord p){setPendingTarget(p.x, p.y);}
	public void setPendingTarget(int x, int y){pendingTarget = new Coord(x, y);}
	public void setPendingAction(ActorAction p){pendingAction = p;}
   public void setPendingIndex(int p){pendingIndex = p;}

   public AI(Actor a)
   {
      self = a;
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
      ToggleTile tt = (ToggleTile)Game.getCurZone().getTile(pendingTarget);
      tt.toggle();
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
   
}