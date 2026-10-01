package Daedalus.Zone;

import Daedalus.Engine.*;
import Daedalus.Actor.*;

public class SpawnPoint implements ActorConstants
{
	private Coord location;
	private ActorBase actorBase;


	public Coord getLocation(){return new Coord(location);}
	public ActorBase getActorBase(){return actorBase;}


	public void setLocation(Coord l){setLocation(l.x, l.y);}
	public void setLocation(int x, int y){location = new Coord(x, y);}
	public void setActorBase(ActorBase a){actorBase = a;}


   public SpawnPoint(Coord loc, ActorBase base)
   {
      location = loc.copy();
      actorBase = base;
   }
}