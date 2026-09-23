package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;

public class RegionBuilder implements ZoneConstants, GUIConstants
{
   public static Vector<Zone> buildWasteland()
   {
      int regionWidth = 4;
      int regionHeight = 4;
      Vector<Zone> zoneList = new Vector<Zone>();
      ZoneMap[][] overlandArr = new ZoneMap[regionWidth][regionHeight];
      MazeBuilder maze = new MazeBuilder(regionWidth, regionHeight, .5);
      
      ZoneMap hideout = WastelandMapFactory.getHideout();
      zoneList.add(new Zone(hideout));
      Actor a = ActorFactory.getDog("Euclid");
      zoneList.elementAt(0).getMap().dropActor(a, 5, 6, zoneList.elementAt(0).getActorList());
      zoneList.elementAt(0).getActorList().add(a);
      
      // add exits
      for(int x = 0; x < regionWidth; x++)
      for(int y = 0; y < regionHeight; y++)
      {
         overlandArr[x][y] = WastelandMapFactory.getBasicMap();
         if(maze.exitsNorth(x, y))
            MapFactory.addRandomExit(overlandArr[x][y], 'N');
         if(maze.exitsEast(x, y))
            MapFactory.addRandomExit(overlandArr[x][y], 'E');
         if(maze.exitsSouth(x, y))
            MapFactory.addRandomExit(overlandArr[x][y], 'S');
         if(maze.exitsWest(x, y))
            MapFactory.addRandomExit(overlandArr[x][y], 'W');
         overlandArr[x][y].setExitList();
      }
      
      // add hideout entrance along south wall of south zone
      int hideoutEntranceX = 1 + RNG.nextInt(regionWidth - 2);
      ZoneMap hideoutEntranceMap = overlandArr[hideoutEntranceX][regionHeight - 1];
      MapFactory.addRandomExit(hideoutEntranceMap, 'S');
      Exit hideoutEntrance = hideoutEntranceMap.getExitByDirection(ExitDirection.SOUTH);
      hideoutEntrance.setExitDirection('D');
      hideoutEntrance.pair(hideout.getExitByDirection(ExitDirection.UP));
      
      // finalize maps
      for(int x = 0; x < regionWidth; x++)
      for(int y = 0; y < regionHeight; y++)
      {
         if(overlandArr[x][y].getExitByDirection(ExitDirection.SOUTH) != null)
            overlandArr[x][y].getExitByDirection(ExitDirection.SOUTH).pair(overlandArr[x][y].getExitByDirection(ExitDirection.NORTH));
         if(overlandArr[x][y].getExitByDirection(ExitDirection.EAST) != null)
            overlandArr[x][y].getExitByDirection(ExitDirection.EAST).pair(overlandArr[x][y].getExitByDirection(ExitDirection.WEST));
         MapPainter.paintWastelandOverworld(overlandArr[x][y]);
         zoneList.add(new Zone(overlandArr[x][y]));
      }
      
      // add actors
      for(int i = 1; i < zoneList.size(); i++)
      {
         ZoneMap map = zoneList.elementAt(i).getMap();
         for(int x = map.getWidth() / 4; x < map.getWidth() * 3 / 4; x++)
         for(int y = map.getHeight() / 4; y < map.getHeight() * 3 / 4; y++)
         {
            if(map.getTile(x, y).isLowPassable())
            {
               Coord c = new Coord(x, y);
               a = ActorFactory.getBandit();
               a.setTileLoc(c, false);
               zoneList.elementAt(i).getActorList().add(a);
               x = map.getWidth();
               y = map.getHeight();
            }
         }
      }
      
      return zoneList;
   }
}