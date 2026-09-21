package Daedalus.AI;

import Daedalus.GUI.*;
import Daedalus.Zone.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;

public class PlayerAI extends AI implements AIConstants, ZoneConstants
{
   public PlayerAI(Actor a)
   {
      super(a);
      setTeam(Team.PLAYER);
   }
   
   // resolve contextual plan, or wait for player
   @Override
   public void plan()
   {
      if(pendingAction == ActorAction.CONTEXTUAL && pendingTarget != null)
      {
         // target is origin
         if(pendingTarget.equals(self.getTileLoc()))
         {
            // standing on item
            if(Game.getCurMap().isItemAt(pendingTarget))
            {
               pendingAction = ActorAction.PICK_UP;
            }
            // standing on exit
            else if(Game.getCurMap().getTile(pendingTarget) instanceof Exit)
            {
               pendingAction = ActorAction.INTERACT;
            }
            // invalid
            else
            {
               clearPlan();
            }
         }
         // target is not origin
         else
         {
            // empty tile
            if(Game.canStep(self, pendingTarget))
            {
               pendingAction = ActorAction.STEP;
            }
            // toggle tile
            else if(Game.getCurMap().getTile(pendingTarget) instanceof ToggleTile)
            {
               pendingAction = ActorAction.INTERACT;
            }
            // read sign
            else if(Game.getCurMap().getTile(pendingTarget) instanceof Sign)
            {
               pendingAction = ActorAction.INTERACT;
            }
            // occupied by enemy
            else if(Game.isActorAt(pendingTarget) && isEnemy(Game.getActorAt(pendingTarget)))
            {
               pendingAction = ActorAction.NATURAL_ATTACK;
            }
            // occupied by pet
            else if(Game.isActorAt(pendingTarget) && Game.getActorAt(pendingTarget) instanceof Pet)
            {
               pendingAction = ActorAction.INTERACT;
            }
            // invalid
            else
            {
               MainGamePanel.addMessage("You can't move there.", true);
               clearPlan();
            }
         }
      }
   }
   
   @Override
   public void clearPlan()
   {
      pendingTarget = null;
      pendingAction = ActorAction.CONTEXTUAL;
   }
}