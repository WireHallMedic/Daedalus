package Daedalus.AI;

import Daedalus.GUI.*;
import Daedalus.Zone.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;

public class AI implements AIConstants, ZoneConstants
{
	protected Actor self;
	protected Coord pendingTarget;
	protected ActorAction pendingAction;


	public Actor getSelf(){return self;}
	public Coord getPendingTarget(){return new Coord(pendingTarget);}
	public ActorAction getPendingAction(){return pendingAction;}


	public void setSelf(Actor s){self = s;}
	public void setPendingTarget(Coord p){setPendingTarget(p.x, p.y);}
	public void setPendingTarget(int x, int y){pendingTarget = new Coord(x, y);}
	public void setPendingAction(ActorAction p){pendingAction = p;}

   public AI(Actor a)
   {
      self = a;
      pendingTarget = null;
      pendingAction = null;
   }
   
	public void setPendingTarget(Direction dir)
   {
      Coord loc = dir.getAsCoord();
      loc.add(self.getTileLoc());
      pendingTarget = loc;
   }
   
   public boolean hasPlan()
   {
      return pendingTarget != null && pendingAction != null && pendingAction != ActorAction.CONTEXTUAL;
   }
   
   public void plan()
   {
      pendingTarget = new Coord();
      pendingAction = ActorAction.DELAY;
   }
   
   public void clearPlan()
   {
      pendingTarget = null;
      pendingAction = null;
   }
   
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
      }
      clearPlan();
   }
   
   protected void doDelay()
   {
      self.discharge(1);
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
      self.discharge(1);
   }
}