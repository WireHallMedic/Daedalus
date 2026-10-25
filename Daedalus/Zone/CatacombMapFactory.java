package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;

public class CatacombMapFactory extends BSPMapFactory
{
   public static ZoneMap getCatacombMap(int width, int height, 
                                        int outerRoomMinDiameter, int outerRoomMaxDiameter, 
                                        int innerRoomMinDiameter, int innerRoomMaxDiameter, 
                                        double extraConnectivity)
   {
      ZoneMap map = new ZoneMap(width, height);
      innerRoomMaxDiameter = Math.min(innerRoomMaxDiameter, outerRoomMaxDiameter - 3);
      innerRoomMinDiameter = Math.min(innerRoomMinDiameter, outerRoomMinDiameter - 3);
      Vector<Room> roomList = BinarySpacePartitioning.partition(width, height, outerRoomMinDiameter, outerRoomMaxDiameter);
      Vector<Room> subroomList = new Vector<Room>();
      for(Room curRoom: roomList)
      {
         if(!curRoom.isParent)
         {
            // make smaller copy
            Room r = curRoom.copy();
            r.origin.x += 1;
            r.origin.y += 1;
            r.size.x = Math.min(r.size.x, innerRoomMaxDiameter);
            r.size.y = Math.min(r.size.y, innerRoomMaxDiameter);
            
            // randomize size and position
            r.size.x -= RNG.nextInt(r.size.x - innerRoomMinDiameter + 1);
            int xSpace = (curRoom.size.x - 3) - r.size.x;
            r.origin.x += RNG.nextInt(xSpace + 1);
            
            r.size.y -= RNG.nextInt(r.size.y - innerRoomMinDiameter + 1);
            int ySpace = (curRoom.size.y - 3) - r.size.y;
            r.origin.y += RNG.nextInt(ySpace + 1);

            subroomList.add(r);
         }
      }
      
      // set all walls
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         map.setTile(x, y, new ZoneTile(TileBase.WALL));
      
      // carve out rooms
      for(Room r: subroomList)
      for(int x = r.origin.x; x < r.origin.x + r.size.x; x++)
      for(int y = r.origin.y; y < r.origin.y + r.size.y; y++)
         map.setTile(x, y, new ZoneTile(TileBase.CLEAR));
      
      // add sibiling passages
      for(int i = 1; i < roomList.size(); i += 2)
      {
         boolean horizontallyAdj = roomList.elementAt(i).isHorizontallyAdjacent(roomList.elementAt(i + 1));
         if(horizontallyAdj)
            addHorizontalTunnel(roomList.elementAt(i), roomList.elementAt(i + 1), map, subroomList);
         else
            addVerticalTunnel(roomList.elementAt(i), roomList.elementAt(i + 1), map, subroomList);
      }
      // add random passages
      Vector<Room> bigRoomList = new Vector<Room>();
      for(Room curRoom: subroomList)
         bigRoomList.add(curRoom);
      bigRoomList.sort((a, b) -> (a.size.x * a.size.y) - (b.size.x * b.size.y));
      int roomsToCheck = Math.min((int)(bigRoomList.size() * extraConnectivity), bigRoomList.size());
      for(int i = 0; i < roomsToCheck; i++)
      {
         addRandomTunnel(bigRoomList.elementAt(i), map);
      }
      
      map.setRoomList(subroomList);
      setKeyTraversalTiles(map, map.getRoomList());
      return map;
   }
   public static ZoneMap getCatacombMap(int width, int height, 
                                        int outerRoomMinDiameter, int outerRoomMaxDiameter, 
                                        int innerRoomMinDiameter, int innerRoomMaxDiameter)
   {
      return getCatacombMap(width, height, outerRoomMinDiameter, outerRoomMaxDiameter,
                            innerRoomMinDiameter, innerRoomMaxDiameter, .5);
   }

   
   private static void addHorizontalTunnel(Room roomA, Room roomB, ZoneMap map, Vector<Room> subroomList)
   {
      Room subroomA = roomA;
      if(!roomA.isParent)
         subroomA = getSubroom(roomA, subroomList);
      Room subroomB = roomB;
      if(!roomB.isParent)
         subroomB = getSubroom(roomB, subroomList);
      Vector<Coord> leftList = new Vector<Coord>();
      Vector<Coord> rightList = new Vector<Coord>();
      int xStart = roomB.origin.x;
      for(int y = roomA.origin.y; y < roomA.origin.y + roomA.size.y; y++)
      {
         Coord left = null;
         Coord right = null;
         for(int x = xStart; x > roomA.origin.x; x--)
         {
            if(map.getTile(x, y).isLowPassable() && subroomA.contains(x, y))
            {
               left = new Coord(x, y);
               break;
            }
         }
         for(int x = xStart; x < roomB.origin.x + roomB.size.x; x++)
         {
            if(map.getTile(x, y).isLowPassable() && subroomB.contains(x, y))
            {
               right = new Coord(x, y);
               break;
            }
         }
         if(left != null && right != null)
         {
            leftList.add(left);
            rightList.add(right);
         }
      }

      if(leftList.size() > 0)
      {
         int pairIndex = RNG.nextInt(leftList.size());
         digTunnel(map, leftList.elementAt(pairIndex), rightList.elementAt(pairIndex));
      }
      else //needs z-connection
      {
         // z-connections require subrooms
         if(roomA.isParent)
            subroomA = getSubroom(roomA, subroomList);
         if(roomB.isParent)
            subroomB = getSubroom(roomB, subroomList);
         connectWithZ(subroomA, subroomB, map, true);
      }
   }

   
   private static void addVerticalTunnel(Room roomA, Room roomB, ZoneMap map, Vector<Room> subroomList)
   {
      Room subroomA = roomA;
      if(!roomA.isParent)
         subroomA = getSubroom(roomA, subroomList);
      Room subroomB = roomB;
      if(!roomB.isParent)
         subroomB = getSubroom(roomB, subroomList);
      Vector<Coord> upperList = new Vector<Coord>();
      Vector<Coord> lowerList = new Vector<Coord>();
      int yStart = roomB.origin.y - 1;
      for(int x = roomA.origin.x; x < roomA.origin.x + roomA.size.x; x++)
      {
         Coord upper = null;
         Coord lower = null;
         boolean foundLower = false;
         for(int y = yStart; y > roomA.origin.y; y--)
         {
            if(map.getTile(x, y).isLowPassable() && subroomA.contains(x, y))
            {
               upper = new Coord(x, y);
               break;
            }
         }
         for(int y = yStart; y < roomB.origin.y + roomB.size.y; y++)
         {
            if(map.getTile(x, y).isLowPassable() && subroomB.contains(x, y))
            {
               lower = new Coord(x, y);
               break;
            }
         }
         if(upper != null && lower != null)
         {
            upperList.add(upper);
            lowerList.add(lower);
         }
      }
      
      if(upperList.size() > 0)
      {
         int pairIndex = RNG.nextInt(upperList.size());
         digTunnel(map, upperList.elementAt(pairIndex), lowerList.elementAt(pairIndex));
      }
      else //needs z-connection
      {
         // z-connections require subrooms
         if(roomA.isParent)
            subroomA = getSubroom(roomA, subroomList);
         if(roomB.isParent)
            subroomB = getSubroom(roomB, subroomList);
         connectWithZ(subroomA, subroomB, map, false);
      }
   }
   
   private static void connectWithZ(Room subroomA, Room subroomB, ZoneMap map, boolean horizontal)
   {
      if(horizontal)
      {
         int tunnelXStart = subroomA.origin.x + subroomA.size.x - 1;
         int connectingColumn = subroomA.origin.x + subroomA.size.x + 1;
         connectingColumn += RNG.nextInt(subroomB.origin.x - (subroomA.origin.x + subroomA.size.x) - 2);
         int tunnelXEnd = subroomB.origin.x;
         int yA = subroomA.origin.y + 1 + RNG.nextInt(subroomA.size.y - 2);
         int yB = subroomB.origin.y + 1 + RNG.nextInt(subroomB.size.y - 2);
         digTunnel(map, new Coord(tunnelXStart, yA), new Coord(connectingColumn, yA));
         digTunnel(map, new Coord(connectingColumn, yA), new Coord(connectingColumn, yB));
         digTunnel(map, new Coord(connectingColumn, yB), new Coord(tunnelXEnd, yB));
      }
      else // vertical
      {
         int tunnelYStart = subroomA.origin.y + subroomA.size.y - 1;
         int connectingRow = subroomA.origin.y + subroomA.size.y + 1;
         connectingRow += RNG.nextInt(subroomB.origin.y - (subroomA.origin.y + subroomA.size.y) - 2);
         int tunnelYEnd = subroomB.origin.y;
         int xA = subroomA.origin.x + 1 + RNG.nextInt(subroomA.size.x - 2);
         int xB = subroomB.origin.x + 1 + RNG.nextInt(subroomB.size.x - 2);
         digTunnel(map, new Coord(xA, tunnelYStart), new Coord(xA, connectingRow));
         digTunnel(map, new Coord(xA, connectingRow), new Coord(xB, connectingRow));
         digTunnel(map, new Coord(xB, connectingRow), new Coord(xB, tunnelYEnd));
      }
   }

   
   public static int countExits(Room r, ZoneMap map)
   {
      int exits = 0;
      for(int x = r.origin.x - 1; x <= r.origin.x + r.size.x; x++)
      {
         if(map.getTile(x, r.origin.y - 1).isPathable())
            exits++;
         if(map.getTile(x, r.origin.y + r.size.y).isPathable())
            exits++;
      }
      for(int y = r.origin.y - 1; y <= r.origin.y + r.size.y; y++)
      {
         if(map.getTile(r.origin.x - 1, y).isPathable())
            exits++;
         if(map.getTile(r.origin.x + r.size.x, y).isPathable())
            exits++;
      }
      return exits;
   }


   public static Room getSubroom(Room parent, Vector<Room> subroomList)
   {
      for(Room prospect: subroomList)
         if(parent.contains(prospect.origin))
            return prospect;
      return null;
   }
   
   
   public static void addRandomTunnel(Room room, ZoneMap map)
   {
      Vector<Coord> startList = new Vector<Coord>();
      Vector<Coord> endList = new Vector<Coord>();
      boolean hasNorthTunnel = hasNorthTunnel(room, map);
      boolean hasEastTunnel = hasEastTunnel(room, map);
      boolean hasSouthTunnel = hasSouthTunnel(room, map);
      boolean hasWestTunnel = hasWestTunnel(room, map);
      
      for(int x = room.origin.x; x < room.origin.x + room.size.x; x++)
      {
         if(!hasNorthTunnel &&
            !map.getTile(x + 1, room.origin.y - 1).isPathable() && !map.getTile(x - 1, room.origin.y - 1).isPathable())
         for(int y = room.origin.y - 1; y > 0; y--)
         {
            if(map.getTile(x, y).isPathable())
            {
               startList.add(new Coord(x, y + 1));
               endList.add(new Coord(x, room.origin.y - 1));
               break;
            }
         }
         if(!hasSouthTunnel &&
            !map.getTile(x + 1, room.origin.y + room.size.y).isPathable() && !map.getTile(x - 1, room.origin.y + room.size.y).isPathable())
         for(int y = room.origin.y + room.size.y; y < map.getHeight(); y++)
         {
            if(map.getTile(x, y).isPathable())
            {
               startList.add(new Coord(x, room.origin.y + room.size.y));
               endList.add(new Coord(x, y - 1));
               break;
            }
         }
      }
      for(int y = room.origin.y; y < room.origin.y + room.size.y; y++)
      {
         if(!hasWestTunnel &&
            !map.getTile(room.origin.x - 1, y + 1).isPathable() && !map.getTile(room.origin.x - 1, y - 1).isPathable())
         for(int x = room.origin.x - 1; x > 0; x--)
         {
            if(map.getTile(x, y).isPathable())
            {
               startList.add(new Coord(x + 1, y));
               endList.add(new Coord(room.origin.x - 1, y));
               break;
            }
         }
         if(!hasEastTunnel &&
            !map.getTile(room.origin.x + room.size.x, y + 1).isPathable() && !map.getTile(room.origin.x + room.size.x, y - 1).isPathable())
         for(int x = room.origin.x + room.size.x; x < map.getWidth(); x++)
         {
            if(map.getTile(x, y).isPathable())
            {
               startList.add(new Coord(room.origin.x + room.size.x, y));
               endList.add(new Coord(x - 1, y));
               break;
            }
         }
      }
      if(startList.size() > 0)
      {
         int roll = RNG.nextInt(startList.size());
         Coord start = startList.elementAt(roll);
         Coord end = endList.elementAt(roll);
         digTunnel(map, start, end);
      }
   }
   
   public static void digTunnel(ZoneMap map, Coord start, Coord end)
   {
      if(start.equals(end))
         return;
      boolean horizontalTunnel = start.y == end.y;
      // dig horizontal
      if(horizontalTunnel)
      {
         // start with smaller x
         if(start.x > end.x)
         {
            Coord temp = start;
            start = end;
            end = temp;
         }
         for(int x = start.x; x < end.x + 1; x++)
         {
            Coord c = new Coord(x, start.y);
            if(!verticallyAdjToPathable(map, new Coord(c)))
               map.setTile(x, start.y, new ZoneTile(TileBase.CLEAR));
         }
      }
      // dig vertical
      else
      {
         // start with smaller y
         if(start.y > end.y)
         {
            Coord temp = start;
            start = end;
            end = temp;
         }
         for(int y = start.y; y < end.y + 1; y++)
         {
            Coord c = new Coord(start.x, y);
            if(!horizontallyAdjToPathable(map, new Coord(c)))
               map.setTile(c, new ZoneTile(TileBase.CLEAR));
         }
      }
   }
   
   // has space above or below
   private static boolean verticallyAdjToPathable(ZoneMap map, Coord loc)
   {
      if(map.getTile(loc.x, loc.y + 1).isPathable() &&
         map.getTile(loc.x - 1, loc.y + 1).isPathable() &&
         map.getTile(loc.x + 1, loc.y + 1).isPathable())
         return true;
      if(map.getTile(loc.x, loc.y - 1).isPathable() &&
         map.getTile(loc.x - 1, loc.y - 1).isPathable() &&
         map.getTile(loc.x + 1, loc.y - 1).isPathable())
         return true;
      return false;
   }
   
   // has space right or left
   private static boolean horizontallyAdjToPathable(ZoneMap map, Coord loc)
   {
      if(map.getTile(loc.x + 1, loc.y).isPathable() &&
         map.getTile(loc.x + 1, loc.y + 1).isPathable() &&
         map.getTile(loc.x + 1, loc.y - 1).isPathable())
         return true;
      if(map.getTile(loc.x - 1, loc.y).isPathable() &&
         map.getTile(loc.x - 1, loc.y + 1).isPathable() &&
         map.getTile(loc.x - 1, loc.y - 1).isPathable())
         return true;
      return false;
   }
   
   private static boolean hasNorthTunnel(Room room, ZoneMap map)
   {
      for(int x = room.origin.x; x < room.origin.x + room.size.x; x++)
         if(map.getTile(x, room.origin.y - 1).isPathable())
            return true;
      return false;
   }
   
   private static boolean hasEastTunnel(Room room, ZoneMap map)
   {
      for(int y = room.origin.y; y < room.origin.y + room.size.y; y++)
         if(map.getTile(room.origin.x + room.size.x, y).isPathable())
            return true;
      return false;
   }
   
   private static boolean hasSouthTunnel(Room room, ZoneMap map)
   {
      for(int x = room.origin.x; x < room.origin.x + room.size.x; x++)
         if(map.getTile(x, room.origin.y + room.size.y).isPathable())
            return true;
      return false;
   }
   
   private static boolean hasWestTunnel(Room room, ZoneMap map)
   {
      for(int y = room.origin.y; y < room.origin.y + room.size.y; y++)
         if(map.getTile(room.origin.x - 1, y).isPathable())
            return true;
      return false;
   }
   
   // functions that shadow BSPMapFactory functions
   ////////////////////////////////////////////////////////////////
   
   public static void setKeyTraversalTiles(ZoneMap map, Vector<Room> roomList)
   {
      for(Room r: roomList)
         setKeyTraversalTiles(map, r);
   }
   
   public static void setKeyTraversalTiles(ZoneMap map, Room room)
   {
      for(int x = room.origin.x; x < room.origin.x + room.size.x; x++)
      {
         if(map.getTile(x, room.origin.y - 1).isPathable())
            map.getTile(x, room.origin.y).setKeyTraversalTile(true);
         if(map.getTile(x, room.origin.y + room.size.y).isPathable())
            map.getTile(x, room.origin.y + room.size.y - 1).setKeyTraversalTile(true);
      }
      for(int y = room.origin.y; y < room.origin.y + room.size.y; y++)
      {
         if(map.getTile(room.origin.x - 1, y).isPathable())
            map.getTile(room.origin.x, y).setKeyTraversalTile(true);
         if(map.getTile(room.origin.x + room.size.x, y).isPathable())
            map.getTile(room.origin.x + room.size.x - 1, y).setKeyTraversalTile(true);
      }
   }
   
   // return a semi-random room, weighted for a higher number of exits
   public static Room getRandomEntranceRoom(ZoneMap map)
   {
      Vector<Room> roomList = new Vector<Room>();
      for(Room r: map.getRoomList())
      {
         roomList.add(new Room(r));
      }
      roomList.sort((a, b) -> countExits(a, map) - countExits(b, map));
      return roomList.elementAt(RNG.nextInt(roomList.size() / 4));
   }
   
   
   // return a semi-random room, weighted for a lower number of exits
   public static Room getRandomExitRoom(ZoneMap map)
   {
      Vector<Room> roomList = new Vector<Room>();
      for(Room r: map.getRoomList())
      {
         roomList.add(new Room(r));
      }
      roomList.sort((a, b) -> countExits(b, map) - countExits(a, map));
      return roomList.elementAt(RNG.nextInt(roomList.size() / 4));
   }
   
   public static void setRandomEntranceAndExit(ZoneMap map, ZoneTile entrance, ZoneTile exit)
   {
      int maxTries = 100;
      Coord inLoc = null;
      Coord outLoc = null;
      int minDist = Math.max(map.getWidth() / 2, map.getHeight() / 2);
      for(int i = 0; i < maxTries; i++)
      {
         inLoc = getRandomFloorTile(map, getRandomEntranceRoom(map));
         outLoc = getRandomFloorTile(map, getRandomExitRoom(map));
         if(EngineTools.getAngbandDistance(inLoc, outLoc) >= minDist)
            break;
      }
      map.setTile(inLoc, entrance);
      map.setTile(outLoc, exit);
   }
}