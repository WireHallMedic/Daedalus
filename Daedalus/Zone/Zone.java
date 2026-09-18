package Daedalus.Zone;

import java.util.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;

public class Zone
{
	private ZoneMap map;
	private Vector<Actor> actorList;
   private int position;


	public ZoneMap getMap(){return map;}
	public Vector<Actor> getActorList(){return actorList;}
   public int getPosition(){return position;}


	public void setMap(ZoneMap m){map = m;}
	public void setActorList(Vector<Actor> a){actorList = a;}
   public void setPosition(int pos){position = pos;}

   
   public Zone(ZoneMap zMap)
   {
      map = zMap;
      actorList = new Vector<Actor>();
      position = 505050;
   }
   
   public Zone()
   {
      this(null);
   }
   
   public void setPosition(int x, int y, int z)
   {
      position = (x * 10000) + (y * 100) + z;
   }
   
   public void setXPosition(int x)
   {
      setPosition(x, getYPosition(), getZPosition());
   }
   
   public void setYPosition(int y)
   {
      setPosition(getXPosition(), y, getZPosition());
   }
   
   public void setZPosition(int z)
   {
      setPosition(getXPosition(), z, getZPosition());
   }
   
   public int getXPosition()
   {
      return position / 10000;
   }
   
   public int getYPosition()
   {
      return (position / 100) % 100;
   }
   
   public int getZPosition()
   {
      return position % 100;
   }
}