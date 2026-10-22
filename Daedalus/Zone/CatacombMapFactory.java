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
            Room r = curRoom.copy();
            // make smaller copy
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
      for(Room curRoom: roomList)
         if(!curRoom.isParent)
            bigRoomList.add(curRoom);
      bigRoomList.sort((a, b) -> (a.size.x * a.size.y) - (b.size.x * b.size.y));
      int roomsToCheck = Math.min((int)(bigRoomList.size() * extraConnectivity), bigRoomList.size());
      for(int i = 0; i < roomsToCheck; i++)
      {
         Room curRoom = bigRoomList.elementAt(i);
         Vector<Coord> doorProspect = new Vector<Coord>();
      }
      
      map.setRoomList(subroomList);
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
      Vector<Coord> straightConnectionList = new Vector<Coord>();
      int xStart = roomB.origin.x;
      for(int y = roomA.origin.y; y < roomA.origin.y + roomA.size.y; y++)
      {
         boolean foundLeft = false;
         boolean foundRight = false;
         for(int x = xStart; x > roomA.origin.x; x--)
         {
            if(map.getTile(x, y).isLowPassable())
            {
               foundLeft = true;
               break;
            }
         }
         for(int x = xStart; x < roomB.origin.x + roomB.size.x; x++)
         {
            if(map.getTile(x, y).isLowPassable())
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
         Room subroomA = null;
         Room subroomB = null;
         for(Room prospectiveChild: subroomList)
         {
            if(roomA.contains(prospectiveChild.origin))
               subroomA = prospectiveChild;
            if(roomB.contains(prospectiveChild.origin))
               subroomB = prospectiveChild;
         }
         connectWithZ(subroomA, subroomB, map, true);
      }
   }

   
   private static void addVerticalTunnel(Room roomA, Room roomB, ZoneMap map, Vector<Room> subroomList)
   {
      Vector<Coord> straightConnectionList = new Vector<Coord>();
      int yStart = roomB.origin.y;
      for(int x = roomA.origin.x; x < roomA.origin.x + roomA.size.x; x++)
      {
         boolean foundUpper = false;
         boolean foundLower = false;
         for(int y = yStart; y > roomA.origin.y; y--)
         {
            if(map.getTile(x, y).isLowPassable())
            {
               foundUpper = true;
               break;
            }
         }
         for(int y = yStart; y < roomB.origin.y + roomB.size.y; y++)
         {
            if(map.getTile(x, y).isLowPassable())
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
            tunnelLoc.y++;
         }
      }
      else //needs z-connection
      {
         Room subroomA = null;
         Room subroomB = null;
         for(Room prospectiveChild: subroomList)
         {
            if(roomA.contains(prospectiveChild.origin))
               subroomA = prospectiveChild;
            if(roomB.contains(prospectiveChild.origin))
               subroomB = prospectiveChild;
         }
         connectWithZ(subroomA, subroomB, map, false);
      }
   }
   
   private static void connectWithZ(Room subroomA, Room subroomB, ZoneMap map, boolean horizontal)
   {
      if(horizontal)
      {
         int tunnelXStart = subroomA.origin.x + subroomA.size.x;
         int connectingColumn = subroomA.origin.x + subroomA.size.x + 1;
         connectingColumn += RNG.nextInt(subroomB.origin.x - (subroomA.origin.x + subroomA.size.x) - 2);
         int tunnelXEnd = subroomB.origin.x - 1;
         int yA = subroomA.origin.y + 1 + RNG.nextInt(subroomA.size.y - 2);
         int yB = subroomB.origin.y + 1 + RNG.nextInt(subroomA.size.y - 2);
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
         int tunnelYStart = subroomA.origin.y + subroomA.size.y;
         int connectingRow = subroomA.origin.y + subroomA.size.y + 1;
         connectingRow += RNG.nextInt(subroomB.origin.y - (subroomA.origin.y + subroomA.size.y) - 2);
         int tunnelYEnd = subroomB.origin.y - 1;
         int xA = subroomA.origin.x + 1 + RNG.nextInt(subroomA.size.x - 2);
         int xB = subroomB.origin.x + 1 + RNG.nextInt(subroomA.size.x - 2);
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
}