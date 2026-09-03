package Daedalus.AI;

import Daedalus.GUI.*;
import Daedalus.Zone.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;

public class WanderAI extends AI implements AIConstants, ZoneConstants
{
	private double stepChance;


	public double getStepChance(){return stepChance;}


	public void setStepChance(double s){stepChance = s;}

   public WanderAI(Actor a)
   {
      super(a);
      stepChance = .5;
   }
   
   public void plan()
   {
      if(RNG.nextDouble() <= stepChance)
      {
         Direction stepDir = Direction.random();
         setPendingTarget(stepDir);
         setPendingAction(ActorAction.STEP);
      }
      else
         super.plan();
   }
}