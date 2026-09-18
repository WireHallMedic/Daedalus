package Daedalus.Engine;

import Daedalus.GUI.*;
import Daedalus.Zone.*;
import Daedalus.Actor.*;
import WidlerSuite.Coord;
import java.util.*;
import java.awt.event.*;

public class Game implements Runnable
{
   private static Zone curZone = null;
	private static ZoneMap curMap = null;
	private static Actor player = null;
   private static Vector<Actor> actorList = null;
   private static int initiativeIndex;
   private static boolean continueF;
   private static boolean playF;
	private static Actor[][] actorMap;      // used to make isActorAt() and getActorAt() O(1)
   private static Zone nextZone = null;
   private static Coord nextZonePlayerLoc = null;


	public static ZoneMap getCurMap(){return curMap;}
	public static Actor getPlayer(){return player;}
   public static Vector<Actor> getActorList(){return actorList;}


	public static void setCurMap(ZoneMap c){curMap = c; setActorMap();}
	public static void setPlayer(Actor p){player = p;}
   public static void setActorList(Vector<Actor> al){actorList = al; setActorMap();}

   public Game()
   {
      curMap = null;
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
      if(curMap != null || curMap.getWidth() != actorMap.length || curMap.getHeight() != actorMap[0].length)
      {
         actorMap = new Actor[curMap.getWidth()][curMap.getHeight()];
      }
      for(int x = 0; x < curMap.getWidth(); x++)
      for(int y = 0; y < curMap.getHeight(); y++)
         actorMap[x][y] = null;
      
      if(actorList != null)
      {
         for(Actor a: actorList)
         {
            setActorPosition(a, null);
         }
      }
   }
   
   // update actor location
   public static void setActorPosition(Actor a, Coord lastPos)
   {
      if(curMap == null || actorMap == null)
         return;
      if(lastPos != null && curMap.isInBounds(lastPos))
         actorMap[lastPos.x][lastPos.y] = null;
      if(curMap.isInBounds(a))
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
   
   private void cleanActorList()
   {
      for(int i = 0; i < actorList.size(); i++)
      {
         if(actorList.elementAt(i).isDead())
         {
            AnimationManager.removeTargetingScripts(actorList.elementAt(i));
            removeActorAt(i);
            i--;
         }
      }
   }
   
   private static void removeActorAt(int listIndex)
   {
      Actor a = actorList.elementAt(listIndex);
      actorMap[a.getTileLoc().x][a.getTileLoc().y] = null;
      actorList.removeElementAt(listIndex);
      if(listIndex <= initiativeIndex)
         initiativeIndex--;
   }
   
   public static boolean isActorAt(int x, int y)
   {
      return curMap.isInBounds(x, y) && actorMap[x][y] != null;
   }
   public static boolean isActorAt(Coord c){return isActorAt(c.x, c.y);}
   
   public static Actor getActorAt(int x, int y)
   {
      if(isActorAt(x, y) && curMap.isInBounds(x, y))
         return actorMap[x][y];
      return null;
   }
   public static Actor getActorAt(Coord c){return getActorAt(c.x, c.y);}
   
   public static boolean canStep(Actor a, int x, int y)
   {
      return !isActorAt(x, y) && curMap.canStep(a, x, y);
   }
   public static boolean canStep(Actor a, Coord c){return canStep(a, c.x, c.y);}
   
   
   public void run()
   {
      while(continueF)
      {
         while(playF)
         {
            if(actorList != null && actorList.size() > 0 && curMap != null)
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
                  curActor.startOfTurn();
                  // plan if needed
                  if(!curActor.hasPlan())
                     curActor.plan();
                  // try to act
                  if(curActor.hasPlan() && AnimationManager.isClearToAct(curActor))
                  {
                     curActor.act();
                     curActor.endOfTurn();
                     player.updateFoV();
                     if(curActor == player)
                        MainGamePanel.incrementMessagePanel();
                     cleanActorList();
                     incrementInitiativeIndex();
                  }
               }
               // cur actor not done charging, increment
               else
               {
                  incrementInitiativeIndex();
               }
            }
            if(nextZone != null)
               transitionZone();
            Thread.yield();
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
      setActorPosition(a, null);
   }
   
   public static void play(){playF = true;}
   public static void pause(){playF = false;}
   
   
   public static void setZone(Zone z, Coord playerLoc)
   {
      nextZone = z;
      nextZonePlayerLoc = playerLoc;
      if(curZone == null)
         transitionZone();
   }
   
   public static void transitionZone()
   {
      if(actorList != null)
         actorList.remove(player);
      curZone = nextZone;
      curMap = curZone.getMap();
      actorList = curZone.getActorList();
      actorList.add(player);
      player.setTileLoc(nextZonePlayerLoc);
      setActorMap();
      nextZone = null;
      nextZonePlayerLoc = null;
      AnimationManager.clear();
   }
}