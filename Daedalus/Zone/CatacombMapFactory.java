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
      System.out.println("Subroom count: " + subroomList.size());
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
      Vector<Coord> straightConnectionList = new Vector<Coord>();
      int xStart = roomB.origin.x;
      for(int y = roomA.origin.y; y < roomA.origin.y + roomA.size.y; y++)
      {
         boolean foundLeft = false;
         boolean foundRight = false;
         for(int x = xStart; x > roomA.origin.x; x--)
         {
            if(map.getTile(x, y).isLowPassable() && subroomA.contains(x, y))
            {
               foundLeft = true;
               break;
            }
         }
         for(int x = xStart; x < roomB.origin.x + roomB.size.x; x++)
         {
            if(map.getTile(x, y).isLowPassable() && subroomB.contains(x, y))
            {
               foundRight = true;
               break;
            }
         }
         if(foundRight && foundLeft)
            straightConnectionList.add(new Coord(xStart, y));
      }

      if(straightConnectionList.size() > 0)
      {
         Coord tunnelLoc = straightConnectionList.elementAt(RNG.nextInt(straightConnectionList.size()));
         while(!map.getTile(tunnelLoc).isLowPassable())
            tunnelLoc.x--;
         tunnelLoc.x++;
         while(!map.getTile(tunnelLoc).isLowPassable())
         {
            map.setTile(tunnelLoc, new ZoneTile(TileBase.CLEAR));
            tunnelLoc.x++;
         }
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
      Vector<Coord> straightConnectionList = new Vector<Coord>();
      int yStart = roomB.origin.y - 1;
      for(int x = roomA.origin.x; x < roomA.origin.x + roomA.size.x; x++)
      {
         boolean foundUpper = false;
         boolean foundLower = false;
         for(int y = yStart; y > roomA.origin.y; y--)
         {
            if(map.getTile(x, y).isLowPassable() && subroomA.contains(x, y))
            {
               foundUpper = true;
               break;
            }
         }
         for(int y = yStart; y < roomB.origin.y + roomB.size.y; y++)
         {
            if(map.getTile(x, y).isLowPassable() && subroomB.contains(x, y))
            {
               foundLower = true;
               break;
            }
         }
         if(foundUpper && foundLower)
            straightConnectionList.add(new Coord(x, yStart));
      }
      
      if(straightConnectionList.size() > 0)
      {
         Coord tunnelLoc = straightConnectionList.elementAt(RNG.nextInt(straightConnectionList.size()));
         while(!map.getTile(tunnelLoc).isLowPassable())
            tunnelLoc.y--;
         tunnelLoc.y++;
         while(!map.getTile(tunnelLoc).isLowPassable())
         {
            map.setTile(tunnelLoc, new ZoneTile(TileBase.CLEAR));
            if(map.getTile(tunnelLoc.x - 1, tunnelLoc.y).isPathable() ||
               map.getTile(tunnelLoc.x + 1, tunnelLoc.y).isPathable())
               break;
            tunnelLoc.y++;
         }
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
         int stepDir = 1;
         if(yA > yB)
            stepDir = -1;
         for(int x = tunnelXStart; x <= connectingColumn; x++)
            map.setTile(x, yA, new ZoneTile(TileBase.CLEAR));
         for(int x = connectingColumn; x <= tunnelXEnd; x++)
            map.setTile(x, yB, new ZoneTile(TileBase.CLEAR));
         for(int y = yA; y != yB; y += stepDir)
            map.setTile(connectingColumn, y, new ZoneTile(TileBase.CLEAR));
      }
      else // vertical
      {
         int tunnelYStart = subroomA.origin.y + subroomA.size.y - 1;
         int connectingRow = subroomA.origin.y + subroomA.size.y + 1;
         connectingRow += RNG.nextInt(subroomB.origin.y - (subroomA.origin.y + subroomA.size.y) - 2);
         int tunnelYEnd = subroomB.origin.y;
         int xA = subroomA.origin.x + 1 + RNG.nextInt(subroomA.size.x - 2);
         int xB = subroomB.origin.x + 1 + RNG.nextInt(subroomB.size.x - 2);
         int stepDir = 1;
         if(xA > xB)
            stepDir = -1;
         for(int y = tunnelYStart; y <= connectingRow; y++)
            map.setTile(xA, y, new ZoneTile(TileBase.CLEAR));
         for(int y = connectingRow; y <= tunnelYEnd; y++)
            map.setTile(xB, y, new ZoneTile(TileBase.CLEAR));
         for(int x = xA; x != xB; x += stepDir)
            map.setTile(x, connectingRow, new ZoneTile(TileBase.CLEAR));
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
         if(!hasNorthTunnel)
         for(int y = room.origin.y - 1; y > 0; y--)
         {
            if(map.getTile(x, y).isPathable())
            {
               startList.add(new Coord(x, y + 1));
               endList.add(new Coord(x, room.origin.y - 1));
               break;
            }
         }
         if(!hasSouthTunnel)
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
         if(!hasWestTunnel)
         for(int x = room.origin.x - 1; x > 0; x--)
         {
            if(map.getTile(x, y).isPathable())
            {
               startList.add(new Coord(x + 1, y));
               endList.add(new Coord(room.origin.x - 1, y));
               break;
            }
         }
         if(!hasEastTunnel)
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
         
         if(start.x == end.x) // vertical tunnel
         {
            for(int y = start.y; y <= end.y; y++)
            {
               map.setTile(start.x, y, new ZoneTile(TileBase.CLEAR));
               map.getTile(start.x, y).setBGColor(BLUE);
               if(map.getTile(start.x - 1, y).isPathable() || map.getTile(start.x + 1, y).isPathable())
                  break;
            }
         }
         else  // horizontal tunnel
         {
            for(int x = start.x; x <= end.x; x++)
            {
               map.setTile(x, start.y, new ZoneTile(TileBase.CLEAR));
               map.getTile(x, start.y).setBGColor(BLUE);
               if(map.getTile(x, start.y - 1).isPathable() || map.getTile(x, start.y + 1).isPathable())
                  break;
            }
         }
      }
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
}