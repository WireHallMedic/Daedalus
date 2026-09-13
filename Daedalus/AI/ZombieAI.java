package Daedalus.AI;

import Daedalus.GUI.*;
import Daedalus.Zone.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import Daedalus.Combat.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;

public class ZombieAI extends AI
{
   public ZombieAI(Actor a)
   {
      super(a);
   }
   
   public void plan()
   {
      Actor targetActor = getClosestEnemy();
      if(targetActor != null)
      {
         // adjacent, attack
         if(EngineTools.getAngbandDistance(self.getTileLoc(), targetActor.getTileLoc()) == 1)
         {
            setPendingAction(ActorAction.BASIC_ATTACK);
            setPendingTarget(targetActor.getTileLoc());
            return;
         }
         // not adjacent, try to step
         Coord targetTile = getDumbstepTowards(targetActor);
         if(targetTile != null)
         {
            setPendingAction(ActorAction.STEP);
            setPendingTarget(targetTile);
            return;
         }
      }
      // can't step, delay
      super.plan();
   }
}