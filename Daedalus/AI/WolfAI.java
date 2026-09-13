package Daedalus.AI;

import Daedalus.GUI.*;
import Daedalus.Zone.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;
import java.util.*;

public class WolfAI extends WanderAI implements AIConstants, ZoneConstants
{
   public WolfAI(Actor s)
   {
      super(s);
      setStepChance(.25);
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
         // not adjacent, try to step by pathing
         Coord stepTowards = getStepTowards(targetActor.getTileLoc());
         if(stepTowards != null)
         {
            setPendingAction(ActorAction.STEP);
            setPendingTarget(stepTowards);
            return;
         }
         // can't path, try dumbstepping
         stepTowards = getDumbstepTowards(targetActor.getTileLoc());
         if(stepTowards != null)
         {
            setPendingAction(ActorAction.STEP);
            setPendingTarget(stepTowards);
            return;
         }
         // can't step, delay
         setPendingAction(ActorAction.DELAY);
         setPendingTarget(self.getTileLoc());
         return;

      }
      // no target, wander
      super.plan();
   }
}