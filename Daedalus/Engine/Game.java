package Daedalus.Engine;

import Daedalus.Zone.*;
import Daedalus.Actor.*;
import java.util.*;
import java.awt.event.*;

public class Game implements Runnable
{
	private static ZoneMap curZone = null;
	private static Actor player = null;
   private static Vector<Actor> actorList = null;
   private static int initiativeIndex;
   private static boolean continueF;
   private static boolean playF;


	public static ZoneMap getCurZone(){return curZone;}
	public static Actor getPlayer(){return player;}
   public static Vector<Actor> getActorList(){return actorList;}


	public static void setCurZone(ZoneMap c){curZone = c;}
	public static void setPlayer(Actor p){player = p;}
   public static void setActorList(Vector<Actor> al){actorList = al;}

   public Game()
   {
      curZone = null;
	   player = null;
      actorList = null;
      initiativeIndex = 0;
      continueF = true;
      playF = false;
      new Thread(this).start();
   }
   
   public void run()
   {
      while(continueF)
      {
         while(playF)
         {
            if(actorList != null && actorList.size() > 0)
            {
               // select actor
               Actor curActor = actorList.elementAt(initiativeIndex);
               // charge if needed
               if(!curActor.isCharged())
               {
                  System.out.println(curActor.getName() + " Charging");
                  curActor.charge();
               }
               // cur actor is charged, try to plan and act
               if(curActor.isCharged())
               {
                  // plan if needed
                  if(!curActor.hasPlan())
                     curActor.plan();
                  // try to act
                  if(curActor.hasPlan())
                  {
                     System.out.println(curActor.getName() + " Acting");
                     curActor.act();
                     incrementInitiativeIndex();
                  }
               }
               // cur actor not done charging, increment
               else
               {
                  incrementInitiativeIndex();
               }
            }
         }
         Thread.yield();
      }
   }
   
   private void incrementInitiativeIndex()
   {
      System.out.println("Looping");
      initiativeIndex++;
      if(initiativeIndex == actorList.size())
         initiativeIndex = 0;
   }
   
   public void addActor(Actor a)
   {
      if(actorList == null)
         actorList = new Vector<Actor>();
      actorList.add(a);
   }
   
   public static void play(){playF = true;}
   public static void pause(){playF = false;}
}