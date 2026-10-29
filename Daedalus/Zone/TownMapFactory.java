package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Engine.*;

public class TownMapFactory extends MapFactory
{
   
   public static ZoneMap getTownMap(int width, int height, 
                                    int plotMinDiameter, int plotMaxDiameter, 
                                    int buildingMinDiameter, int buildingMaxDiameter)
   {
      ZoneMap map = new ZoneMap(width, height);
      buildingMaxDiameter = Math.min(buildingMaxDiameter, plotMaxDiameter - 3);
      buildingMinDiameter = Math.min(buildingMinDiameter, plotMinDiameter - 3);
      Vector<Room> plotList = BinarySpacePartitioning.partition(width, height, plotMinDiameter, plotMaxDiameter);
      Vector<Room> buildingList = new Vector<Room>();
      for(Room curRoom: plotList)
      {
         if(!curRoom.isParent)
         {
            // make smaller copy
            Room r = curRoom.copy();
            r.origin.x += 1;
            r.origin.y += 1;
            r.size.x = Math.min(r.size.x, buildingMaxDiameter);
            r.size.y = Math.min(r.size.y, buildingMaxDiameter);
            
            // randomize size and position
            r.size.x -= RNG.nextInt(r.size.x - buildingMinDiameter + 1);
            int xSpace = (curRoom.size.x - 3) - r.size.x;
            r.origin.x += RNG.nextInt(xSpace + 1);
            
            r.size.y -= RNG.nextInt(r.size.y - buildingMinDiameter + 1);
            int ySpace = (curRoom.size.y - 3) - r.size.y;
            r.origin.y += RNG.nextInt(ySpace + 1);

            buildingList.add(r);
         }
      }
      
      // set all walls
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         map.setTile(x, y, new ZoneTile(TileBase.CLEAR));
      
      // carve out rooms
      for(Room r: buildingList)
      {
         for(int x = r.origin.x; x < r.origin.x + r.size.x; x++)
         {
            map.setTile(x, r.origin.y, new ZoneTile(TileBase.WALL));
            map.setTile(x, r.origin.y + r.size.y - 1, new ZoneTile(TileBase.WALL));
         }
         for(int y = r.origin.y; y < r.origin.y + r.size.y; y++)
         {
            map.setTile(r.origin.x, y, new ZoneTile(TileBase.WALL));
            map.setTile(r.origin.x + r.size.x - 1, y, new ZoneTile(TileBase.WALL));
         }
      }
      
      addDoors(map, buildingList);
      map.setRoomList(buildingList);
      setKeyTraversalTiles(map);
      return map;
   }
   
   public static void addDoors(ZoneMap map, Vector<Room> roomList)
   {
      for(Room r: roomList)
      {
         addDoor(map, r);
      }
   }
   
   // buildings close to map border don't face that border
   public static void addDoor(ZoneMap map, Room room)
   {
      Vector<Direction> dirOptionList = new Vector<Direction>();
      if(room.origin.y >= 5)
         dirOptionList.add(Direction.NORTH);
      if(room.origin.x + room.size.x <= map.getWidth() - 5)
         dirOptionList.add(Direction.EAST);
      if(room.origin.y + room.size.y <= map.getHeight() - 5)
         dirOptionList.add(Direction.SOUTH);
      if(room.origin.x >= 5)
         dirOptionList.add(Direction.WEST);
      Direction dir = dirOptionList.elementAt(RNG.nextInt(dirOptionList.size()));
      int x = 1;
      int y = 1;
      switch(dir)
      {
         case NORTH: x = room.origin.x + 1 + RNG.nextInt(room.size.x - 2);
                     y = room.origin.y;
                     break;
         case EAST:  x = room.origin.x + room.size.x - 1;
                     y = room.origin.y + 1 + RNG.nextInt(room.size.y - 2);
                     break;
         case SOUTH: x = room.origin.x + 1 + RNG.nextInt(room.size.x - 2);
                     y = room.origin.y + room.size.y - 1;
                     break;
         case WEST:  x = room.origin.x;
                     y = room.origin.y + 1 + RNG.nextInt(room.size.y - 2);
                     break;
      }
      map.setTile(x, y, new Door());
   }
}