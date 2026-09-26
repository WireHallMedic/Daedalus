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
import Daedalus.Engine.*;

public class ShadowFoV
{
   // for arc casting
   private final double EIGHTH_CIRCLE = 2.0 * Math.PI / 8.0;
   
   protected boolean[][] transparencyMap;    // which tiles are visible
   protected boolean[][] visibilityMap;      // which tiles can be seen from most recent calcFoV()
   protected boolean[][] newVisibilityMap;   // used to calc new FoV so we can use table swapping
   protected int[][] addedMap;               // which tiles have been added to processList
   protected int width;
   protected int height;
   protected int visionRadius;
   protected int visionDiameter;
   protected Coord observerPosition;
   
   // multipliers for transforming octants
   private static int[][] multipliers = {{1,  0,  0, -1, -1,  0,  0,  1},
                                         {0,  1, -1,  0,  0, -1,  1,  0},
                                         {0,  1,  1,  0,  0, -1, -1,  0},
                                         {1,  0,  0,  1, -1,  0,  0, -1}};
   
   // constructor
   public ShadowFoV(boolean[][] transpMap, int visionRange, Coord observer)
   {
      reset(transpMap, visionRange, observer);
   }
   
   public ShadowFoV(boolean[][] transpMap, Actor a)
   {
      this(transpMap, a.getVisionRadius(), a.getTileLoc());
   }
   
   private Coord translateToLocal(Coord position, Coord observer)
   {
      return new Coord(translateToLocal(position.x, observer.x), translateToLocal(position.y, observer.y));
   }
   
   private int translateToLocal(int position, int observer)
   {
      return observer - position + visionRadius;
   }
   
   
   // generally only needs to be called if a different transparency map is needed
   public void reset(boolean[][] transpMap, int visionRange, Coord observerPos)
   {
      transparencyMap = transpMap;                // intentional shallow copy
      width = transparencyMap.length;
      height = transparencyMap[0].length;
      visionRadius = visionRange;
      visionDiameter = visionRange + visionRange + 1;
      visibilityMap = new boolean[visionDiameter][visionDiameter];
      newVisibilityMap = new boolean[visionDiameter][visionDiameter];
      observerPosition = observerPos;
   }
   public void reset(boolean[][] transpMap, Actor a){reset(transpMap, a.getVisionRadius(), a.getTileLoc());}
   
   // checks if a location is in the transparency map bounds
   public boolean isInTransparentMapBounds(Coord loc){return isInTransparentMapBounds(loc.x, loc.y);}
   public boolean isInTransparentMapBounds(int x, int y)
   {
      return x >= 0 && y >= 0 && x < width && y < height;
   }
   
   // checks if a location is in the transparency map bounds
   public boolean isInVisionMapBounds(Coord loc){return isInVisionMapBounds(loc.x, loc.y);}
   public boolean isInVisionMapBounds(int x, int y)
   {
      x = translateToLocal(x, observerPosition.x);
      y = translateToLocal(y, observerPosition.y);
      return x >= 0 && y >= 0 && x < visionDiameter && y < visionDiameter;
   }
   
   // checks if a square blocks LoS
   public boolean blocksLoS(Coord loc){return blocksLoS(loc.x, loc.y);}
   public boolean blocksLoS(int x, int y)
   {
      if(isInTransparentMapBounds(x, y))
         return !transparencyMap[x][y];
      return true;
   }
   
   // checks if a square is visible
   public boolean isVisible(Coord loc){return isVisible(loc.x, loc.y);}
   public boolean isVisible(int x, int y)
   {
      return isInVisionMapBounds(x, y) && getVisible(x, y);
   }
   
   private boolean getVisible(Coord loc){return getVisible(loc.x, loc.y);}
   private boolean getVisible(int x, int y)
   {
      x = translateToLocal(x, observerPosition.x);
      y = translateToLocal(y, observerPosition.y);
      return visibilityMap[x][y];
   }
   
   private void setVisible(Coord loc, boolean v){setVisible(loc.x, loc.y, v);}
   private void setVisible(int x, int y, boolean v)
   {
      x = translateToLocal(x, observerPosition.x);
      y = translateToLocal(y, observerPosition.y);
      newVisibilityMap[x][y] = v;
   }

   
   // Calculate visible squares from a given location and radius
   public void calcFoV(int xLoc, int yLoc, int radius)
   {
      observerPosition.x = xLoc;
      observerPosition.y = yLoc;
      visionRadius = radius;
      visionDiameter = radius + radius + 1;
      newVisibilityMap = new boolean[visionDiameter][visionDiameter];
      for(int oct = 0; oct < 8; oct += 1)
      {
         castLightInOctant(xLoc, yLoc, oct, visionRadius);
      }
      setVisible(observerPosition, true);
      visibilityMap = newVisibilityMap;
   }
   public void calcFoV(Actor a)
   {calcFoV(a.getTileLoc().x, a.getTileLoc().y, a.getVisionRadius());}
   
   private void castLightInOctant(int xLoc, int yLoc, int oct, int radius)
   {
      castLight(xLoc, yLoc,         // starting coordinates
                1,                  // row number
                1.0, 0.0, radius,   // bounding slopes and radius
                multipliers[0][oct], multipliers[1][oct], multipliers[2][oct], multipliers[3][oct]); // octant multipliers
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
                  setVisible(x, y, true);
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
      newVisibilityMap = new boolean[visionDiameter][visionDiameter];
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
      castLightInOctant(origin.x, origin.y, octant, radius);
      setVisible(origin, true);
      visibilityMap = newVisibilityMap;
   }
   
   
   public static void main(String[] args)
   {
      int size = 20;
      int vision = 5;
      int visionDiameter = vision + vision + 1;
      int playerPosX = 10;
      int playerPosY = 5;
      char[][] charMap = new char[size][size];
      boolean[][] boolMap = new boolean[size][size];
      for(int x = 0; x < size; x++)
      for(int y = 0; y < size; y++)
      {
         if(x == 0 || y == 0 || x == size - 1 || y == size - 1)
            charMap[x][y] = '#';
         else
            charMap[x][y] = '.';
      }
      charMap[playerPosX - 2][playerPosY] = '#';
      charMap[playerPosX][playerPosY] = '@';
      for(int x = 0; x < size; x++)
      for(int y = 0; y < size; y++)
         if(charMap[x][y] != '#')
            boolMap[x][y] = true;
      
      ShadowFoV fov = new ShadowFoV(boolMap, vision, new Coord(10, 10));
      fov.calcFoV(playerPosX, playerPosY, 5);
      
      for(int y = playerPosY - vision; y <= playerPosY + vision; y++)
      {
         for(int x = playerPosX - vision; x <= playerPosX + vision; x++)
         {
               System.out.print(charMap[x][y] + "");
         }
         System.out.println("");
      }
      
      for(int y = playerPosY - vision; y <= playerPosY + vision; y++)
      {
         for(int x = playerPosX - vision; x <= playerPosX + vision; x++)
         {
            if(fov.isVisible(x, y))
               System.out.print(charMap[x][y] + "");
            else
               System.out.print(" ");
         }
         System.out.println("");
      }
   }
}
