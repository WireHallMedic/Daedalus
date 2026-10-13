package Daedalus;

import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Combat.*;
import Daedalus.Engine.*;
import Daedalus.Ability.*;
import Daedalus.Actor.*;
import Daedalus.Zone.*;
import java.util.*;

public class Main
{
   public static void main(String[] args)
   {
      DaeFrame frame = new DaeFrame();
      boolean testing = false;
      Vector<Zone> zoneList = null;
      
      if(testing)
         zoneList = RegionBuilder.buildTestRegion();
      else
         zoneList = RegionBuilder.buildWasteland();
      
      Actor a = ActorFactory.getPlayer();
      for(int i = 0; i < 10; i++)
         a.addToInventory(ConsumableFactory.getElementalGrenade());
      a.addToInventory(ConsumableFactory.getStims());
      Game.setPlayer(a);
      
      
      if(testing)
         Game.setZone(zoneList.elementAt(0), new Coord(4, 4));
      else
         Game.setZone(zoneList.elementAt(0), new Coord(4, 10));
      
      Game.setZoneList(zoneList);
      Game.play();

   }
}