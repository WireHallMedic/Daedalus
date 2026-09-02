package Daedalus.AI;

import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;

public class AI implements AIConstants
{
	private Actor self;
	private Coord pendingTarget;
	private ActorAction pendingAction;


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
   
   public boolean hasPlan()
   {
      return pendingTarget != null && pendingAction != null;
   }
   
   public void plan()
   {
      if(self == Game.getPlayer())
         return;
      System.out.println(self.getName() + " plans");
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
   
   private void doDelay()
   {
      System.out.println(self.getName() + " delays");
      self.discharge(1);
   }
   
   private void doStep()
   {
      self.adjustTileLoc(pendingTarget);
      self.discharge(1);
   }
}