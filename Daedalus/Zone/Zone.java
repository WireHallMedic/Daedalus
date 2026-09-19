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
}