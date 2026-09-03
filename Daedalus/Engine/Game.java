package Daedalus.Engine;

import Daedalus.GUI.*;
import Daedalus.Zone.*;
import Daedalus.Actor.*;
import WidlerSuite.Coord;
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
	private static Actor[][] actorMap;      // used to make isActorAt() and getActorAt() O(1)


	public static ZoneMap getCurZone(){return curZone;}
	public static Actor getPlayer(){return player;}
   public static Vector<Actor> getActorList(){return actorList;}


	public static void setCurZone(ZoneMap c){curZone = c; setActorMap();}
	public static void setPlayer(Actor p){player = p;}
   public static void setActorList(Vector<Actor> al){actorList = al; setActorMap();}

   public Game()
   {
      curZone = null;
      actorMap = null;
	   player = null;
      actorList = null;
      initiativeIndex = 0;
      continueF = true;
      playF = false;
      new Thread(this).start();
   }
   
   private static void setActorMap()
   {
      if(curZone != null)
      {
         actorMap = new Actor[curZone.getWidth()][curZone.getHeight()];
         for(int x = 0; x < curZone.getWidth(); x++)
         for(int y = 0; y < curZone.getHeight(); y++)
            actorMap[x][y] = null;
      }
      if(actorList != null)
      {
         for(Actor a: actorList)
         {
            setPlayerPosition(a, null);
         }
      }
   }
   
   // update actor location
   public static void setPlayerPosition(Actor a, Coord lastPos)
   {
      if(curZone == null || actorMap == null)
         return;
      if(lastPos != null && curZone.isInBounds(lastPos))
         actorMap[lastPos.x][lastPos.y] = null;
      if(curZone.isInBounds(a))
      {
         // throw error if tile already contains someone else
         if(actorMap[a.getTileLoc().x][a.getTileLoc().y] != null &&
            actorMap[a.getTileLoc().x][a.getTileLoc().y] != a)
         {
            String errorStr = String.format("Location %s already occupied by %s when attempting to assign %s to it", 
                                          a.getTileLoc(), actorMap[a.getTileLoc().x][a.getTileLoc().y].getName(), a.getName());
            throw new Error(errorStr);
         }
         actorMap[a.getTileLoc().x][a.getTileLoc().y] = a;
      }
   }
   
   public static boolean isActorAt(int x, int y)
   {
      return curZone.isInBounds(x, y) && actorMap[x][y] != null;
   }
   public static boolean isActorAt(Coord c){return isActorAt(c.x, c.y);}
   
   public static Actor getActorAt(int x, int y)
   {
      if(isActorAt(x, y) && curZone.isInBounds(x, y))
         return actorMap[x][y];
      return null;
   }
   public static Actor getActorAt(Coord c){return getActorAt(c.x, c.y);}
   
   public static boolean canStep(Actor a, int x, int y)
   {
      return !isActorAt(x, y) && curZone.canStep(a, x, y);
   }
   public static boolean canStep(Actor a, Coord c){return canStep(a, c.x, c.y);}
   
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
                  curActor.charge();
               }
               // cur actor is charged, try to plan and act
               if(curActor.isCharged())
               {
                  // plan if needed
                  if(!curActor.hasPlan())
                     curActor.plan();
                  // try to act
                  if(curActor.hasPlan() && AnimationManager.isClearToAct(curActor))
                  {
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
      initiativeIndex++;
      if(initiativeIndex == actorList.size())
         initiativeIndex = 0;
   }
   
   public void addActor(Actor a)
   {
      if(actorList == null)
         actorList = new Vector<Actor>();
      actorList.add(a);
      setPlayerPosition(a, null);
   }
   
   public static void play(){playF = true;}
   public static void pause(){playF = false;}
}