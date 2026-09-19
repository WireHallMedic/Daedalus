package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import WidlerSuite.Coord;
import WidlerSuite.SpiralSearch;

public class MapFactory implements ZoneConstants, GUIConstants
{
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
      
      map.updateSubmaps();
      return map;
   }
}