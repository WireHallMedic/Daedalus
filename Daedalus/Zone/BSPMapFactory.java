package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;

public class BSPMapFactory extends MapFactory
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
      setKeyTraversalTiles(map, map.getRoomList());

      return map;
   }
   public static ZoneMap getBSPMap(int width, int height, int minRoomDiameter, int maxRoomDiameter)
   {
      return getBSPMap(width, height, minRoomDiameter, maxRoomDiameter, .5);
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
         if(map.getTile(x, room.origin.y).isPathable())
            map.getTile(x, room.origin.y + 1).setKeyTraversalTile(true);
         if(map.getTile(x, room.origin.y + room.size.y).isPathable())
            map.getTile(x, room.origin.y + room.size.y - 1).setKeyTraversalTile(true);
      }
      for(int y = room.origin.y; y < room.origin.y + room.size.y; y++)
      {
         if(map.getTile(room.origin.x, y).isPathable())
            map.getTile(room.origin.x + 1, y).setKeyTraversalTile(true);
         if(map.getTile(room.origin.x + room.size.x, y).isPathable())
            map.getTile(room.origin.x + room.size.x - 1, y).setKeyTraversalTile(true);
      }
   }
   
   
   public static int countExits(Room r, ZoneMap map)
   {
      int exits = 0;
      for(int x = r.origin.x; x <= r.origin.x + r.size.x; x++)
      {
         if(map.getTile(x, r.origin.y).isPathable())
            exits++;
         if(map.getTile(x, r.origin.y + r.size.y).isPathable())
            exits++;
      }
      for(int y = r.origin.y; y <= r.origin.y + r.size.y; y++)
      {
         if(map.getTile(r.origin.x, y).isPathable())
            exits++;
         if(map.getTile(r.origin.x + r.size.x, y).isPathable())
            exits++;
      }
      return exits;
   }
   
   
   public static Room getRandomRoom(ZoneMap map)
   {
      return map.getRoomList().elementAt(RNG.nextInt(map.getRoomList().size()));
   }
   
   
   // returns a tile that is low-passable and not a keyTraversalTile
   public static Coord getRandomFloorTile(ZoneMap map, Room r)
   {
      Vector<Coord> prospectList = new Vector<Coord>();
      for(int x = r.origin.x; x < r.origin.x + r.size.x; x++)
      for(int y = r.origin.y; y < r.origin.y + r.size.y; y++)
      {
         if(map.getTile(x, y).isLowPassable() && 
            !map.getTile(x, y).isKeyTraversalTile())
            prospectList.add(new Coord(x, y));
      }
      return prospectList.elementAt(RNG.nextInt(prospectList.size()));
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
}