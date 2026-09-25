package Daedalus.Zone;

import java.util.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;

public class Zone
{
	private ZoneMap map;
	private Vector<Actor> actorList;


	public ZoneMap getMap(){return map;}
	public Vector<Actor> getActorList(){return actorList;}


	public void setMap(ZoneMap m){map = m;}
	public void setActorList(Vector<Actor> a){actorList = a;}

   
   public Zone(ZoneMap zMap)
   {
      map = zMap;
      actorList = new Vector<Actor>();
   }
   
   public Zone()
   {
      this(null);
   }
   
   
   // returns the tileLoc of the specified exit, or null if it is not here
   public Coord getEntranceLoc(Exit e)
   {
      for(Coord c: map.getExitList())
      {
         if(map.getTile(c) == e)
            return new Coord(c);
      }
      return null;
   }
   
   public int calculateThreat()
   {
      int totalThreat = 0;
      for(Actor a: actorList)
         totalThreat += a.getThreat();
      return totalThreat;
   }
   
   public boolean shouldRepopulate()
   {
      return calculateThreat() < map.getMinThreatLevel();
   }
   
   // places an actor away from existing actors, and not in the player's vision if they are present
   public void randomlyPlaceActors(Vector<Actor> newActorList, boolean activeZone)
   {
      boolean[][] placeMap = new boolean[map.getWidth()][map.getHeight()];
      boolean playerPresent = actorList.contains(Game.getPlayer());
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         placeMap[x][y] = map.isLowPassable(x, y);
      }
      DijkstraMap dropMap = new DijkstraMap(placeMap);
      for(Actor curActor: actorList)
      {
         dropMap.addGoal(curActor.getTileLoc());
      }
      
      for(Actor a: newActorList)
      {
         // special case for no actors present
         double searchThreshold = DijkstraMap.MAX_DISTANCE;
         // normally search GTE max / 2
         if(dropMap.getGoalsPlaced() > 0)
            searchThreshold = dropMap.getHighestPassable() / 2.0;
         Vector<Coord> locList = new Vector<Coord>();
         
         for(int x = 0; x < map.getWidth(); x++)
         for(int y = 0; y < map.getHeight(); y++)
         {
            if(map.isValidLocationForItem(x, y) &&
               placeMap[x][y] &&
               dropMap.getValue(x, y) >= searchThreshold)
            {
               Coord c = new Coord(x, y);
               // if player is present, skip tiles within vision range of player
               if(!(playerPresent && EngineTools.getAngbandDistance(Game.getPlayer().getTileLoc(), c) <=
                  Game.getPlayer().getVisionRadius()))
               {
                  locList.add(c);
               }
            }
         }
         Coord actorLoc = locList.elementAt(RNG.nextInt(locList.size()));
         a.setTileLoc(actorLoc, activeZone);
         dropMap.addGoal(actorLoc);
         if(!actorList.contains(a))
            actorList.add(a);
      }
   }
   
      public void randomlyPlaceActor(Actor actor, boolean activeZone)
      {
         Vector<Actor> actorList = new Vector<Actor>();
         actorList.add(actor);
         randomlyPlaceActors(actorList, activeZone);
      }
}