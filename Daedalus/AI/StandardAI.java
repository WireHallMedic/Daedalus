/*
When they have an enemy:
   Has line of effect, has range to enemy, weapon charged: attack
   Has line of effect, has range to enemy, weapon not charged: wander
   Does not have line of effect or does not have range: move to have both
Else: roll to wander

*/


package Daedalus.AI;

import Daedalus.GUI.*;
import Daedalus.Zone.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;
import java.util.*;

public class StandardAI extends WanderAI implements AIConstants, ZoneConstants
{
   public StandardAI(Actor s)
   {
      super(s);
      setWanderChance(.25);
   }
   
   public void plan()
   {
      Actor targetActor = getClosestEnemy();
      if(targetActor != null)
      {
         // has target, line of effect, and range
         setPassMap(targetActor, false);
         if(hasLineOfEffect(self, targetActor) &&
            EngineTools.getAngbandDistance(self.getTileLoc(), targetActor.getTileLoc()) <= self.getCurWeapon().getAttack().getRange())
         {
            // can shoot
            if(self.getCurWeapon().canFire())
            {
               setPendingAction(ActorAction.BASIC_ATTACK);
               setPendingTarget(targetActor.getTileLoc());
               return;
            }
            // can't shoot, wander
            else
            {
               setWanderPlan();
               return;
            }
         }
         // has target, but not line of effect and range
         else
         {
            // path towards target
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
         }

      }
      // no target, call parent
      super.plan();
   }
}