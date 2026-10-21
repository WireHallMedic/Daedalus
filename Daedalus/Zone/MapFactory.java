package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;

public class MapFactory implements ZoneConstants, GUIConstants
{
   // basic BSP map generation
   public static ZoneMap getBSPMap(int width, int height, int minRoomDiameter, int maxRoomDiameter, double extraConnectivity)
   {
      Vector<Room> roomList = BinarySpacePartitioning.partition(width - 1, height - 1, minRoomDiameter, maxRoomDiameter);
      ZoneMap map = new ZoneMap(width, height);
      for(Room curRoom: roomList)
         if(!curRoom.isParent)
            map.getRoomList().add(curRoom);
      
      // set walls
      for(Room curRoom: roomList)
      {
         for(int x = 0; x < curRoom.size.x; x++)
         {
            map.setTile(curRoom.origin.x + x, curRoom.origin.y, new ZoneTile(TileBase.WALL));
         }
         for(int y = 0; y < curRoom.size.y; y++)
         {
            map.setTile(curRoom.origin.x, curRoom.origin.y + y, new ZoneTile(TileBase.WALL));
         }
      }
      for(int x = 0; x < width; x++)
         map.setTile(x, height - 1, new ZoneTile(TileBase.WALL));
      for(int y = 0; y < height; y++)
         map.setTile(width - 1, y, new ZoneTile(TileBase.WALL));
      
      // add required doors
      for(int i = 1; i < roomList.size(); i += 2)
      {
         boolean horizontallyAdj = roomList.elementAt(i).isHorizontallyAdjacent(roomList.elementAt(i + 1));
         Vector<Coord> doorProspect = new Vector<Coord>();
         if(horizontallyAdj)
         {
            for(int y = 0; y < roomList.elementAt(i).size.y; y++)
            {
               Coord c = new Coord(roomList.elementAt(i + 1).origin.x, roomList.elementAt(i).origin.y + y);
               if(map.canPlaceDoor(c))
                  doorProspect.add(c);
            }
         }
         else // vert adj
         {
            for(int x = 0; x < roomList.elementAt(i).size.x; x++)
            {
               Coord c = new Coord(roomList.elementAt(i).origin.x + x, roomList.elementAt(i + 1).origin.y);
               if(map.canPlaceDoor(c))
                  doorProspect.add(c);
            }
         }
         if(doorProspect.size() > 0)
            map.setTile(doorProspect.elementAt(RNG.nextInt(doorProspect.size())), new Door());
      }
      
      // add additional connectivity, prioritizing larger rooms
      Vector<Room> bigRoomList = new Vector<Room>();
      for(Room curRoom: roomList)
         if(!curRoom.isParent)
            bigRoomList.add(curRoom);
      bigRoomList.sort((a, b) -> (a.size.x * a.size.y) - (b.size.x * b.size.y));
      int roomsToCheck = Math.min((int)(bigRoomList.size() * extraConnectivity), bigRoomList.size());
      for(int i = 0; i < roomsToCheck; i++)
      {
         Room curRoom = bigRoomList.elementAt(i);
         Vector<Coord> doorProspect = new Vector<Coord>();
         for(int x = curRoom.origin.x; x < curRoom.origin.x + curRoom.size.x; x++)
         {
            if(map.canPlaceDoor(x, curRoom.origin.y))
               doorProspect.add(new Coord(x, curRoom.origin.y)); 
            if(map.canPlaceDoor(x, curRoom.origin.y + curRoom.size.y))
               doorProspect.add(new Coord(x, curRoom.origin.y + curRoom.size.y)); 
         }
         for(int y = curRoom.origin.y; y < curRoom.origin.y + curRoom.size.y; y++)
         {
            if(map.canPlaceDoor(curRoom.origin.x, y))
               doorProspect.add(new Coord(curRoom.origin.x, y)); 
            if(map.canPlaceDoor(curRoom.origin.x + curRoom.size.x, y))
               doorProspect.add(new Coord(curRoom.origin.x + curRoom.size.x, y)); 
         }
         if(doorProspect.size() > 0)
            map.setTile(doorProspect.elementAt(RNG.nextInt(doorProspect.size())), new Door());
      }
      
      return map;
   }
   public static ZoneMap getBSPMap(int width, int height, int minRoomDiameter, int maxRoomDiameter)
   {
      return getBSPMap(width, height, minRoomDiameter, maxRoomDiameter, .5);
   }
   
   public static ZoneMap getCatacombMap(int width, int height, int minRoomDiameter, int maxRoomDiameter, double extraConnectivity)
   {
      ZoneMap map = new ZoneMap(width, height);
      Vector<Room> roomList = BinarySpacePartitioning.partition(width, height, minRoomDiameter + 2, maxRoomDiameter + 2);
      Vector<Room> subroomList = new Vector<Room>();
      for(Room curRoom: roomList)
      {
         if(!curRoom.isParent)
         {
            Room r = curRoom.copy();
            r.origin.x += 1;
            r.origin.y += 1;
            r.size.x -= 2;
            r.size.y -= 2;
            subroomList.add(r);
         }
      }
      
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         map.setTile(x, y, new ZoneTile(TileBase.WALL));
         
      for(Room r: subroomList)
      for(int x = r.origin.x; x < r.origin.x + r.size.x; x++)
      for(int y = r.origin.y; y < r.origin.y + r.size.y; y++)
         map.setTile(x, y, new ZoneTile(TileBase.CLEAR));
      
      map.setRoomList(subroomList);
      return map;
   }
   
   
   // adds a sign at the nearest tile that doesn't block ground movement and is ortho-adacent to at least one wall
   public static void dropSign(ZoneMap map, Coord startLoc, String text)
   {
      int searchRadius = 5;
      int searchDiameter = searchRadius + searchRadius + 1;
      Coord centerLoc = new Coord(searchDiameter / 2, searchDiameter / 2);
      
      boolean[][] prospectMap = new boolean[searchDiameter][searchDiameter];
      for(int x = 0; x < searchDiameter; x++)
      for(int y = 0; y < searchDiameter; y++)
      {
         prospectMap[x][y] = true;
      }
      
      SpiralSearch search = new SpiralSearch(prospectMap, centerLoc, false, true);
      Coord target = search.getNext();
      while(target != null)
      {  
         target.x = target.x + startLoc.x - searchRadius;
         target.y = target.y + startLoc.y - searchRadius;
         if(map.isLowPassable(target) && 
            !map.wouldBlockPathing(target) &&
            countLowPassNeighbors(map, target) < 4)
         {
            // don't drop adj to exit or on path
            if(!(map.getTile(target.x + 1, target.y) instanceof Exit) &&
               !(map.getTile(target.x - 1, target.y) instanceof Exit) &&
               !(map.getTile(target.x, target.y + 1) instanceof Exit) &&
               !(map.getTile(target.x, target.y - 1) instanceof Exit) &&
               map.getTile(target).getDecorationType() != DECORATION_PATH)
               break;
         }
         target = search.getNext();
      }
      if(target == null)
         return;
      Sign sign = new Sign(text);
      sign.setBGColor(map.getTile(target).getBGColor());
      map.setTile(target, sign);
   }
   public static void dropSign(ZoneMap map, int x, int y, String text){dropSign(map, new Coord(x, y), text);}
   
   
   // returns the number of ortho-adjacent low-pass neighbors
   public static int countLowPassNeighbors(ZoneMap map, int x, int y)
   {
      int count = 0;
      if(map.getTile(x + 1, y).isLowPassable())
         count++;
      if(map.getTile(x - 1, y).isLowPassable())
         count++;
      if(map.getTile(x, y + 1).isLowPassable())
         count++;
      if(map.getTile(x, y - 1).isLowPassable())
         count++;
      return count;
   }
   public static int countLowPassNeighbors(ZoneMap map, Coord c){return countLowPassNeighbors(map, c.x, c.y);}
   
   
   public static double getPassableRatio(ZoneMap map)
   {
      // create passable map
      boolean[][] passableArr = new boolean[map.getWidth()][map.getHeight()];
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         passableArr[x][y] = map.getTile(x, y).isPathable();
      }
      // find pathable tile in middle
      Coord searchStart = null;
      for(int x = 0; x < map.getWidth() / 2; x++)
      for(int y = 0; y < map.getHeight() / 2; y++)
      {
         int xLoc = (map.getWidth() / 4) + x;
         int yLoc = (map.getHeight() / 4) + y;
         if(map.getTile(xLoc, yLoc).isPathable())
         {
            searchStart = new Coord(xLoc, yLoc);
            break;
         }
         if(searchStart != null)
            break;
      }
      if(searchStart == null)
         return 0.0;
      // do flood fill and count results
      boolean[][] reachableArr = FloodFill.fill(passableArr, searchStart.x, searchStart.y);
      int pathable = 0;
      int total = map.getWidth() * map.getHeight();
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         if(reachableArr[x][y])
            pathable++;
      }
      return (double)pathable / (double)total;
   }
   
   public static Coord getNearestLowPassable(ZoneMap map, Coord startLoc, int searchRadius)
   {
      int searchDiameter = searchRadius + searchRadius + 1;
      Coord centerLoc = new Coord(searchDiameter / 2, searchDiameter / 2);
      
      boolean[][] prospectMap = new boolean[searchDiameter][searchDiameter];
      for(int x = 0; x < searchDiameter; x++)
      for(int y = 0; y < searchDiameter; y++)
      {
         prospectMap[x][y] = true;
      }
      
      SpiralSearch search = new SpiralSearch(prospectMap, centerLoc, false, true);
      Coord target = search.getNext();
      while(target != null)
      {
         if(map.isLowPassable(target.x + startLoc.x - searchRadius, target.y + startLoc.y - searchRadius))
            break;
         target = search.getNext();
      }
      if(target == null)
         return null;
      return new Coord(target.x + startLoc.x - searchRadius, target.y + startLoc.y - searchRadius);
   }
   public static Coord getNearestLowPassable(ZoneMap map, Coord startLoc){return getNearestLowPassable(map, startLoc, 5);}
   
   
   public static void pairExits(ZoneMap z1, ZoneMap z2, ExitDirection dirFromZ1)
   {
      Exit exit1 = z1.getExitByDirection(dirFromZ1);
      Exit exit2 = z2.getExitByDirection(dirFromZ1.getOpposite());
      exit1.pair(exit2);
   }
   
   
   public static void fillUnreachable(ZoneMap map, ZoneTile fillTile)
   {
      // create passable map
      boolean[][] passableArr = new boolean[map.getWidth()][map.getHeight()];
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         passableArr[x][y] = map.getTile(x, y).isPathable();
      }
      // find pathable tile in middle
      Coord searchStart = null;
      for(int x = 0; x < map.getWidth() / 2; x++)
      for(int y = 0; y < map.getHeight() / 2; y++)
      {
         int xLoc = (map.getWidth() / 4) + x;
         int yLoc = (map.getHeight() / 4) + y;
         if(map.getTile(xLoc, yLoc).isPathable())
         {
            searchStart = new Coord(xLoc, yLoc);
            break;
         }
         if(searchStart != null)
            break;
      }
      // do flood fill and count results
      boolean[][] reachableArr = FloodFill.fill(passableArr, searchStart.x, searchStart.y);
      
      // fill unreachable
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         if(!reachableArr[x][y] && map.getTile(x, y).isPathable())
            map.setTile(x, y, fillTile.copy());
      }
   }
   
   // will not put scatter in the four corners to make less box-like
   public static void addScatter(ZoneMap map, int startX, int startY, int width, int height, double chance, ZoneTile scatterTile)
   {
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         if((x == 0 && y == 0) ||
            (x == 0 && y == height - 1) ||
            (x == width - 1 && y == 0) ||
            (x == width - 1 && y == height - 1))
            continue;
         if(RNG.nextDouble() < chance)
         {
            map.setTile(x + startX, y + startY, scatterTile.copy());
         }
      }
   }
   
   // uses spiralsearch to make tightly grouped scatter
   public static void addTightScatter(ZoneMap map, int startX, int startY, int width, int height, int count, ZoneTile scatterTile)
   {
      boolean[][] scatterMap = new boolean[width][height];
      Coord centerLoc = new Coord(width / 2, height / 2);
      scatterMap[centerLoc.x][centerLoc.y] = true;
      
      boolean[][] prospectMap = new boolean[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         prospectMap[x][y] = true;
         scatterMap[x][y] = false;
      }
      scatterMap[centerLoc.x][centerLoc.y] = true;
      
      SpiralSearch search = null;
      Coord target = null;
      for(int i = 0; i < count; i++)
      {
         search = new SpiralSearch(prospectMap, centerLoc, false, true);
         target = search.getNext();
         while(target != null)
         {
            if(!scatterMap[target.x][target.y])
            {
               scatterMap[target.x][target.y] = true;
               break;
            }
            target = search.getNext();
         }
      }
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         if(scatterMap[x][y])
            map.setTile(x + startX, y + startY, scatterTile.copy());
   }
   
   // adds a tile to a location entirely surrounded by lowpassable
   public static void addRandomTile(ZoneMap map, ZoneTile tile)
   {
      boolean found = false;
      int xLoc = -1;
      int yLoc = -1;
      while(!found)
      {
         xLoc = RNG.nextInt(map.getWidth() - 2) + 1;
         yLoc = RNG.nextInt(map.getHeight() - 2) + 1;
         found = true;
         for(int x = -1; x < 2; x++)
         for(int y = -1; y < 2; y++)
         {
            if(map.getTile(xLoc + x, yLoc + y).isLowPassable() &&
               map.getTile(xLoc + x, yLoc + y) instanceof Door == false &&
               map.getTile(xLoc + x, yLoc + y) instanceof Exit == false &&
               map.getTile(xLoc + x, yLoc + y) instanceof ToggleTile == false)
               continue;
            else
               found = false;
         }
      }
      map.setTile(xLoc, yLoc, tile.copy());
   }
   
   
   public static void setBorder(ZoneMap map, ZoneTile tile)
   {
      for(int x = 0; x < map.getWidth(); x++)
      {
         map.setTile(x, 0, tile.copy());
         map.setTile(x, map.getHeight() - 1, tile.copy());
      }
      for(int y = 0; y < map.getHeight(); y++)
      {
         map.setTile(0, y, tile.copy());
         map.setTile(map.getWidth() - 1, y, tile.copy());
      }
   }
   
   public static void setJaggedBorder(ZoneMap map, int maxDepth, ZoneTile tile)
   {
      for(int x = 0; x < map.getWidth(); x++)
      {
         int depth = 1 + RNG.nextInt(maxDepth);
         for(int i = 0; i < depth; i++)
            map.setTile(x, i, tile.copy());
         depth = depth = 1 + RNG.nextInt(maxDepth);
         for(int i = 0; i < depth; i++)
            map.setTile(x, (map.getHeight() - 1) - i, tile.copy());
      }
      for(int y = 0; y < map.getHeight(); y++)
      {         
         int depth = 1 + RNG.nextInt(maxDepth);
         for(int i = 0; i < depth; i++)
            map.setTile(i, y, tile.copy());
         depth = depth = 1 + RNG.nextInt(maxDepth);
         for(int i = 0; i < depth; i++)
            map.setTile((map.getWidth() - 1) - i, y, tile.copy());
      }
   }
   
      
   // cannot randomly add up or down
   public static void addRandomExit(ZoneMap map, char dir)
   {
      dir = Character.toUpperCase(dir);
      int x = 0;
      int y = 0;
      int xStep = 0;
      int yStep = 0;
      if(dir == 'N')
      {
         x = RNG.nextInt(map.getWidth() / 2) + (map.getWidth() / 4);
         yStep = 1;
      }
      else if(dir == 'E')
      {
         x = map.getWidth() - 1;
         y = RNG.nextInt(map.getHeight() / 2) + (map.getHeight() / 4);
         xStep = -1;
      }
      else if(dir == 'S')
      {
         x = RNG.nextInt(map.getWidth() / 2) + (map.getWidth() / 4);
         y = map.getHeight() - 1;
         yStep = -1;
      }
      else if(dir == 'W')
      {
         y = RNG.nextInt(map.getHeight() / 2) + (map.getHeight() / 4);
         xStep = 1;
      }
      else
         throw new Error("Bad argument for random exit " + dir);
      map.setTile(x, y, new Exit(dir));
      x += xStep;
      y += yStep;
      while(!map.getTile(x, y).isLowPassable())
      {
         map.setTile(x, y, new ZoneTile(TileBase.CLEAR));
         x += xStep;
         y += yStep;
      }
      map.setExitList();
   }
   
   public static void addCrates(ZoneMap map, int x, int y, int width, int height, int numOfCrates)
   {
      int maxTries = 100;
      for(int i = 0; i < numOfCrates; i++)
      {
         for(int j = 0; j < maxTries; j++)
         {
            int xLoc = x + RNG.nextInt(width);
            int yLoc = y + RNG.nextInt(height);
            if(!map.wouldBlockPathing(xLoc, yLoc))
            {
               map.setTile(xLoc, yLoc, Chest.getCrate(map.getLevel()));
               break;
            }
         }
      }
   }
   
   
   public static void addExitPaths(ZoneMap map)
   {
      int crossroadCount = 1;
      if(map.getExitList().size() == 3)
         crossroadCount += RNG.nextInt(2);
      if(map.getExitList().size() > 3)
         crossroadCount = 2;
      
      Coord[] crossroadList = new Coord[crossroadCount];
      for(int i = 0; i < crossroadList.length; i++)
      {
         int crossroadsX = (map.getWidth() / 4) + RNG.nextInt(map.getWidth() / 2);
         int crossroadsY = (map.getHeight() / 4) + RNG.nextInt(map.getHeight() / 2);
         crossroadList[i] = getNearestLowPassable(map, new Coord(crossroadsX, crossroadsY), 5);
      }
      
      // connect exits to closest crossroads
      for(Coord exitLoc: map.getExitList())
      {
         Coord crossroads = crossroadList[0];
         for(int i = 1; i < crossroadList.length; i++)
         {
            if(EngineTools.getAngbandDistance(crossroads, exitLoc) > EngineTools.getAngbandDistance(crossroadList[i], exitLoc))
               crossroads = crossroadList[i];
         }
         addPath(map, crossroads, exitLoc, true);
      }
      // connect crossroads to each other
      for(int i = 0; i < crossroadList.length - 1; i++)
      {
         Coord curCrossroad = crossroadList[i];
         Coord nextCrossroad = crossroadList[i + 1];
         for(int j = i + 1; j < crossroadList.length; j++)
            if(EngineTools.getAngbandDistance(curCrossroad, nextCrossroad) > EngineTools.getAngbandDistance(curCrossroad, crossroadList[j]))
               nextCrossroad = crossroadList[j];
         addPath(map, curCrossroad, nextCrossroad, true);
      }
   }
   
   
   public static void addPath(ZoneMap map, Coord point1, Coord point2, boolean addBend)
   {
     addPathBase(map, map.getFullLowPassMap(), point1, point2, addBend);
   }
   public static void addPath(ZoneMap map, Coord point1, Coord point2){addPath(map, point1, point2, false);}
   
   // the passMap is not memoized in ZoneMap, so we don't want to generate it a lot
   private static void addPathBase(ZoneMap map, boolean[][] lowPassMap, Coord point1, Coord point2, boolean addBend)
   {
      if(lowPassMap == null)
         lowPassMap = map.getFullLowPassMap();
      AStar pathSearch = new AStar();
      Vector<Coord> basePath = pathSearch.path(lowPassMap, point1, point2);
      
      if(basePath.size() == 0)
         return;
         
      if(addBend)
      {
         Coord bend = basePath.elementAt(basePath.size() / 2);
         Vect modVect = new Vect(point1, point2);
         modVect.angle += Math.PI / 2.0;
         if(RNG.nextBoolean())
            modVect.angle += Math.PI;
         modVect.magnitude = RNG.nextDouble() * (basePath.size() / 4.0);
         Vect bendVect = bend.getAsVect();
         bendVect.add(modVect);
         bend = getNearestLowPassable(map, bendVect.getAsCoord(), 5);
         addPathBase(map, lowPassMap, point1, bend, false);
         addPathBase(map, lowPassMap, bend, point2, false);
      }
      else
      {
         for(Coord c: basePath)
         {
            if(!(map.getTile(c) instanceof Exit))
               map.getTile(c).setDecorationType(DECORATION_PATH);
         }
         if(!(map.getTile(point2) instanceof Exit))
            map.getTile(point1).setDecorationType(DECORATION_PATH);
         if(!(map.getTile(point2) instanceof Exit))
            map.getTile(point1).setDecorationType(DECORATION_PATH);
      }
   }

   public static ZoneMap getTestMap1()
   {
      ZoneMap map = new ZoneMap(10, 10);
      setBorder(map, new ZoneTile(TileBase.WALL));
      
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         map.getTile(x, y).setBGColor(GREY);
      }
      Exit exit = new Exit('W');
      map.setTile(1, map.getHeight() / 2, exit);
      map.setTile(3, 2, new Sign("This is a sign"));
      map.updateSubmaps();
      return map;
   }

   public static ZoneMap getTestMap2()
   {
      ZoneMap map = new ZoneMap(10, 10);
      setBorder(map, new ZoneTile(TileBase.WALL));
      
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         map.getTile(x, y).setBGColor(GREEN);
      }
      Exit exit = new Exit('E');
      map.setTile(map.getWidth() - 2, map.getHeight() / 2, exit);
      Sign sign = new Sign("This is also a sign");
      sign.setWall(true);
      map.setTile(map.getWidth() - 2, (map.getHeight() / 2) - 1, sign);
      
      map.updateSubmaps();
      return map;
   }
}