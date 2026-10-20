package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;

public class RegionBuilder implements ZoneConstants, GUIConstants, ActorConstants
{
   public static Vector<Zone> buildWasteland()
   {
      int regionWidth = 4;
      int regionHeight = 4;
      Vector<Zone> zoneList = new Vector<Zone>();
      ZoneMap[][] overlandArr = new ZoneMap[regionWidth][regionHeight];
      int[][] exits = new int[regionWidth][regionHeight];
      MazeBuilder maze = new MazeBuilder(regionWidth, regionHeight, .67);
      int hideoutEntranceX = 1 + RNG.nextInt(regionWidth - 2);
      int crashedShipX = 1 + RNG.nextInt(regionWidth - 2);
      int hogtownEntranceY = 1 + RNG.nextInt(regionHeight - 2);
      int dumpZoneEntranceY = 1 + RNG.nextInt(regionHeight - 2);
      
      int[][] mazeMap = maze.getTileIndexMap();
      mazeMap[(hideoutEntranceX * 3) + 1][(regionHeight * 3) - 2] = 'X';
      
      // calc exits
      for(int x = 0; x < regionWidth; x++)
      for(int y = 0; y < regionHeight; y++)
      {
         if(maze.exitsNorth(x, y))
            exits[x][y] += BITMASK_NORTH;
         if(maze.exitsEast(x, y))
            exits[x][y] += BITMASK_EAST;
         if(maze.exitsSouth(x, y))
            exits[x][y] += BITMASK_SOUTH;
         if(maze.exitsWest(x, y))
            exits[x][y] += BITMASK_WEST;
      }
      exits[hideoutEntranceX][regionHeight - 1] += BITMASK_DOWN;
      exits[0][hogtownEntranceY] += BITMASK_WEST;
      exits[regionWidth - 1][dumpZoneEntranceY] += BITMASK_EAST;
      
      ZoneMap hideout = WastelandMapFactory.getHideout(mazeMap);
      zoneList.add(new Zone(hideout));
      Actor a = ActorFactory.getDog("Euclid");
      zoneList.elementAt(0).getMap().dropActor(a, 8, 11, zoneList.elementAt(0).getActorList());
      zoneList.elementAt(0).getActorList().add(a);
      
      // create main area
      for(int x = 0; x < regionWidth; x++)
      for(int y = 0; y < regionHeight; y++)
      {
         if(y == 0 && x == crashedShipX)
            overlandArr[x][y] = WastelandMapFactory.getCrashedShipMap(1, exits[x][y]);
         else
            overlandArr[x][y] = WastelandMapFactory.getBasicMap(1, exits[x][y]);
      }
      
      // add signs
      ZoneMap map = overlandArr[0][hogtownEntranceY];
      for(int y = 0; y < map.getHeight(); y++)
      {
         if(map.getTile(0, y) instanceof Exit)
         {
            MapFactory.dropSign(map, 0, y, "         Hogtown\n  No Poor People Allowed");
         }
      }
      map = overlandArr[regionWidth - 1][dumpZoneEntranceY];
      for(int y = 0; y < map.getHeight(); y++)
      {
         if(map.getTile(map.getWidth() - 1, y) instanceof Exit)
         {
            MapFactory.dropSign(map, map.getWidth() - 1, y, "Dump Zone");
         }
      }
      
      // connect hideout exit
      ZoneMap hideoutEntranceMap = overlandArr[hideoutEntranceX][regionHeight - 1];
      Exit hideoutEntrance = hideoutEntranceMap.getExitByDirection(ExitDirection.DOWN);
      hideoutEntrance.pair(hideout.getExitByDirection(ExitDirection.UP));
      
      // link exits and paint
      for(int x = 0; x < regionWidth; x++)
      for(int y = 0; y < regionHeight; y++)
      {
         if(overlandArr[x][y].getExitByDirection(ExitDirection.SOUTH) != null)
         {
            overlandArr[x][y].getExitByDirection(ExitDirection.SOUTH).pair(overlandArr[x][y+1].getExitByDirection(ExitDirection.NORTH));
         }
         // don't catch the exit to Dump Zone
         if(overlandArr[x][y].getExitByDirection(ExitDirection.EAST) != null && x < regionWidth - 1)
         {
            overlandArr[x][y].getExitByDirection(ExitDirection.EAST).pair(overlandArr[x+1][y].getExitByDirection(ExitDirection.WEST));
         }
         //MapPainter.paintWastelandOverworld(overlandArr[x][y]);
         zoneList.add(new Zone(overlandArr[x][y]));
      }
      
      // set random encounter tables
      for(int i = 1; i < zoneList.size(); i++)
      {
         Vector<TableItemWrapper> randEncTab = new Vector<TableItemWrapper>();
         randEncTab.add(new TableItemWrapper(ActorBase.JACKAL, 1, 100, TableItem.BASE_WEIGHT * 2));
         randEncTab.add(new TableItemWrapper(ActorBase.ROACH, 1, 100, TableItem.BASE_WEIGHT * 2));
         randEncTab.add(new TableItemWrapper(ActorBase.BANDIT, 1, 100, TableItem.BASE_WEIGHT));
         zoneList.elementAt(i).setRandomEncounterTable(randEncTab);
      }
      
      return zoneList;
   }
   
   public static Vector<Zone> buildTestRegion()
   {
      Vector<Zone> zoneList = new Vector<Zone>();
      
      Zone testZone = new Zone();
      testZone.setMap(ZoneMap.getTestMap());
      testZone.setActorList(new Vector<Actor>());
      
      zoneList.add(testZone);
      return zoneList;
   }
}