package Daedalus;

import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Engine.*;
import Daedalus.Ability.*;
import Daedalus.Actor.*;
import Daedalus.Zone.*;
import WidlerSuite.Coord;
import java.util.*;

public class Main
{
   public static void main(String[] args)
   {
      DaeFrame frame = new DaeFrame();
      Game game = new Game();
      
      Vector<Zone> zoneList = RegionBuilder.buildWasteland();

      
      
//       Vector<Actor> actorList = new Vector<Actor>();
//       
//       Zone zone = new Zone();
//       zone.setMap(ZoneMap.getTestMap());
//       zone.setActorList(actorList);
      
      Actor a = ActorFactory.getPlayer();
      a.setTileLoc(3, 5);
      game.setPlayer(a);
      
//       Actor b = ActorFactory.getJackal();
//       b.setTileLoc(3, 11);
//       actorList.add(b);
//       
// 
//       b = ActorFactory.getDrone();
//       b.setTileLoc(4, 11);
//       actorList.add(b);
//       
// 
//       b = ActorFactory.getDrone();
//       b.setTileLoc(10, 7);
//       actorList.add(b);
      

      Actor b = ActorFactory.getDrone();
      b.setTileLoc(6, 6);
      zoneList.elementAt(1).getActorList().add(b);
      
      game.setZoneList(zoneList);
      game.setZone(zoneList.elementAt(0), new Coord(3, 5));
      game.play();

   }
}