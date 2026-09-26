/*******************************************************************************************

An implementation of a shadow casting FoV algorithm. This is my translation of Bjorn Bergstrom's
Python implementation. Made to be persistent; just call calcFoV() with a new origin, or after
updating the transparency map.

To be honest, while I understand the idea, the actual implementation is a little dense for me. Don't
ask me to pick this one apart for you.

Description:
http://roguebasin.roguelikedevelopment.org/index.php?title=FOV_using_recursive_shadowcasting

Original Implementation:
http://roguebasin.roguelikedevelopment.org/index.php?title=PythonShadowcastingImplementation

Copyright 2019 Michael Widler
Free for private or public use. No warranty is implied or expressed.

*******************************************************************************************/

package Daedalus.Actor;

import WidlerSuite.Coord;

public class ShadowFoV
{
   // for arc casting
   private final double EIGHTH_CIRCLE = 2.0 * Math.PI / 8.0;
   
   protected boolean[][] transparencyMap;    // which tiles are visible
   protected int[][] visibilityMap;          // which tiles can be seen from most recent calcFoV()
   protected int[][] addedMap;               // which tiles have been added to processList
   protected int width;
   protected int height;
   protected int flag;                       // used to avoid unnecessary reassignment to refresh visibilityMap
   
   // multipliers for transforming octants
   private static int[][] multipliers = {{1,  0,  0, -1, -1,  0,  0,  1},
                                         {0,  1, -1,  0,  0, -1,  1,  0},
                                         {0,  1,  1,  0,  0, -1, -1,  0},
                                         {1,  0,  0,  1, -1,  0,  0, -1}};
   
   // constructor
   public ShadowFoV(boolean[][] transpMap)
   {
      reset(transpMap);
   }
   
   
   // generally only needs to be called if a different transparency map is needed
   public void reset(boolean[][] transpMap)
   {
      transparencyMap = transpMap;                // intentional shallow copy
      width = transparencyMap.length;
      height = transparencyMap[0].length;
      visibilityMap = new int[width][height];
      flag = 0;
   }
   
   // checks if a location is in the map bounds
   public boolean isInBounds(Coord loc){return isInBounds(loc.x, loc.y);}
   public boolean isInBounds(int x, int y)
   {
      return x >= 0 && y >= 0 && x < width && y < height;
   }
   
   // checks if a square blocks LoS
   public boolean blocksLoS(Coord loc){return blocksLoS(loc.x, loc.y);}
   public boolean blocksLoS(int x, int y)
   {
      if(isInBounds(x, y))
         return !transparencyMap[x][y];
      return false;
   }
   
   // checks if a square is visible
   public boolean isVisible(Coord loc){return isVisible(loc.x, loc.y);}
   public boolean isVisible(int x, int y)
   {
      return isInBounds(x, y) && visibilityMap[x][y] == flag;
   }
   
   // returns the visibility array in the rectangle passed
   public boolean[][] getArray(int startX, int startY, int w, int h)
   {
      boolean[][] visArr = new boolean[w][h];
      for(int x = 0; x < w; x++)
      for(int y = 0; y < h; y++)
      {
         visArr[x][y] = isVisible(startX + x, startY + y);
      }
      return visArr;
   }
   
   protected void incrementFlag()
   {
      flag += 1;
      if(flag == Integer.MAX_VALUE)
      {
         reset(transparencyMap);
      }
   }

   
   // Calculate visible squares from a given location and radius
   public void calcFoV(int xLoc, int yLoc, int radius)
   {
      incrementFlag();
      for(int oct = 0; oct < 8; oct += 1)
      {
         castLightInOctant(xLoc, yLoc, oct, radius);
      }
      visibilityMap[xLoc][yLoc] = flag;
   }
   
   private void castLightInOctant(int xLoc, int yLoc, int oct, int radius)
   {
      castLight(xLoc, yLoc,         // starting coordinates
                1,                  // row number
                1.0, 0.0, radius,   // bounding slopes and radius
                multipliers[0][oct], multipliers[1][oct], multipliers[2][oct], multipliers[3][oct]); // octant multipliers
   }
   
   // sets a square as visible using the current flag
   private void setVisible(int x, int y)
   {
      if(isInBounds(x, y))
         visibilityMap[x][y] = flag;
   }
   
   // casts light
   private void castLight(int cx, int cy,                       // starting coordinates
                          int row,                              // row number
                          double start, double end, int radius, // terminal slopes, and max light radius
                          int xx, int xy, int yx, int yy)       // multipliers for octant
   {
      if(start < end)
         return;
      
      int RADIUS_SQUARED = radius * radius;
      
      // main loop; iterates out from observer
      for(int j = row; j < radius + 1; j += 1)
      {
         int dx = -j - 1;
         int dy = -j;
         boolean blocked = false;
         double newStart = 0.0;
         
         while(dx <= 0)
         {
            dx += 1;
            
            // translate the dx, dy coordinates into map coordinates
            int x = cx + dx * xx + dy * xy;
            int y = cy + dx * yx + dy * yy;
            
            // calculate the left and right slopes of the square under consideration
            double leftSlope = (dx - .5) / (dy + .5);
            double rightSlope = (dx + .5) / (dy - .5);
            
            if(start < rightSlope) // not in beam yet
               continue;
            else if (end > leftSlope) // beyond beam
               break;
            else
            {
               // observer has LoS to the square; mark accordingly
               if(dx * dx + dy * dy < RADIUS_SQUARED)
               {
                  setVisible(x, y);
               }
               if(blocked) // we're scanning a row of blocked squares
               {
                  if(blocksLoS(x, y))
                  {
                     newStart = rightSlope;
                     continue;
                  }
                  else
                  {
                     blocked = false;
                     start = newStart;
                  }
               }
               else    // have been scanning transparent squares
               {
                  if(blocksLoS(x, y) && j < radius) // start a child
                  {
                     blocked = true;
                     castLight(cx, cy, j + 1, start, leftSlope, radius, xx, xy, yx, yy);
                     newStart = rightSlope;
                  }
               }
            }
         } // end while loop
         if(blocked)
            break;
      }
   }  // end castLight()
   
   // calculates light only in the octant containing the target
   public void calcCone(int originX, int originY, int radius, int targetX, int targetY)
   {
      calcCone(new Coord(originX, originY), radius, new Coord(targetX, targetY));
   }
   public void calcCone(Coord origin, int radius, Coord target)
   {
      double angleTo = origin.getAngleTo(target);
      int octant = 5;
      if(angleTo <= EIGHTH_CIRCLE)
         octant = 2;
      else if(angleTo <= EIGHTH_CIRCLE * 2)
         octant = 3;
      else if(angleTo <= EIGHTH_CIRCLE * 3)
         octant = 0;
      else if(angleTo <= EIGHTH_CIRCLE * 4)
         octant = 1;
      else if(angleTo <= EIGHTH_CIRCLE * 5)
         octant = 6;
      else if(angleTo <= EIGHTH_CIRCLE * 6)
         octant = 7;
      else if(angleTo <= EIGHTH_CIRCLE * 7)
         octant = 4;
      incrementFlag();
      castLightInOctant(origin.x, origin.y, octant, radius);
      visibilityMap[origin.x][origin.y] = flag;
   }
   
}
