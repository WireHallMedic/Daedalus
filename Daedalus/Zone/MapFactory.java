package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;
import WidlerSuite.FloodFill;
import WidlerSuite.SpiralSearch;

public class MapFactory implements ZoneConstants, GUIConstants
{

   public static void pairExits(ZoneMap z1, ZoneMap z2, ExitDirection dirFromZ1)
   {
      Exit exit1 = z1.getExitByDirection(dirFromZ1);
      Exit exit2 = z2.getExitByDirection(dirFromZ1.getOpposite());
      exit1.pair(exit2);
   }
   
   
   public static void fillUnreachable(ZoneMap map, int xFillStart, int yFillStart, ZoneTile fillTile)
   {
      boolean[][] passableArr = new boolean[map.getWidth()][map.getHeight()];
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         passableArr[x][y] = map.getTile(x, y).isLowPassable();
      }
      boolean[][] reachableArr = FloodFill.fill(passableArr, xFillStart, yFillStart);
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         if(!reachableArr[x][y])
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