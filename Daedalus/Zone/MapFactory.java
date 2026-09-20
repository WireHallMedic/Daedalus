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