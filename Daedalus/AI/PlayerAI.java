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
         else if(Game.isActorAt(pendingTarget) && isEnemy(Game.getActorAt(pendingTarget)))
         {
            pendingAction = ActorAction.NATURAL_ATTACK;
         }
         else
         {
            MainGamePanel.addMessage("You can't move there.", true);
            clearPlan();
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