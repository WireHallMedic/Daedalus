package Daedalus.AI;

import Daedalus.GUI.*;
import Daedalus.Zone.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;
import java.util.*;

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
         Vector<Coord> tileList = new Vector<Coord>();
         for(int i = 1; i < Direction.values().length; i++)
         {
            Coord c = Direction.values()[i].getAsCoord();
            c.add(self.getTileLoc());
            if(Game.canStep(self, c))
               tileList.add(c);
         }
         if(tileList.size() > 0)
         {
            setPendingTarget(tileList.elementAt(RNG.nextInt(tileList.size())));
            setPendingAction(ActorAction.STEP);
         }
         else
            super.plan();
      }
      else
         super.plan();
   }
}