/*******************************************************************************************

Creates a straight line between two points on a rectangular grid.
This is an implementation of Bresenham's Line. Can be called directly or through
StraightLine when StraightLine is in Rect Mode. Static.

Copyright 2019 Michael Widler
Free for private or public use. No warranty is implied or expressed.

*******************************************************************************************/

package Daedalus.Engine;
import java.util.*;

public class StraightLine
{
   public static final int REMOVE_ORIGIN = 1;
   public static final int REMOVE_TARGET = 2;
   public static final int REMOVE_ORIGIN_AND_TARGET = 3;
   protected static boolean roundToEven = true;                // either round to nearest even number or nearest int
   
   public static void setRoundToEven(boolean r){roundToEven = r;}

   
   // removes the origin and/or target, as determined by the arguments
   protected static void trim(Vector<Coord> line, int arguments)
   {
      // take out the target if requested
      if(arguments == REMOVE_TARGET || arguments == REMOVE_ORIGIN_AND_TARGET && line.size() > 1)
         line.removeElementAt(line.size() - 1);
      
      // take out the origin if requested
      if(arguments == REMOVE_ORIGIN || arguments == REMOVE_ORIGIN_AND_TARGET && line.size() > 0)
         line.removeElementAt(0);
   }
   
   // returns a new Vector which is the sum of the two, in sequence
   protected static Vector<Coord> combine(Vector<Coord> to, Vector<Coord> from)
   {
      Vector<Coord> list = new Vector<Coord>();
      for(Coord c : to)
         list.add(c);
      for(int i = from.size() - 1; i >= 0; i--)
         list.add(from.elementAt(i));
      return list;
   }
   
   // returns the line between two points, subject to arguments
   public static Vector<Coord> findLine(Coord origin, Coord target){return findLine(origin, target, 0);}
   public static Vector<Coord> findLine(Coord origin, Coord target, int arguments)
   {
      double xMove = (double)(target.x - origin.x);
      double yMove = (double)(target.y - origin.y);
      int steps = (int)Math.max(Math.abs(xMove), Math.abs(yMove));
      double xStep = (double)xMove / (double)steps;
      double yStep = (double)yMove / (double)steps;
      
      Vector<Coord> list;
      list = rectLoop(origin, steps, xStep, yStep);
      trim(list, arguments);
      
      return list;
   }
   
   // the work function
   protected static Vector<Coord> rectLoop(Coord origin, int steps, double xStep, double yStep)
   {
      Vector<Coord> list = new Vector<Coord>();
      for(int i = 0; i <= steps; i++)
      {
         Coord c = new Coord();
         if(roundToEven)
         {
            c.y = EngineTools.roundToEven(i * yStep);
            c.x = EngineTools.roundToEven(i * xStep);
         }
         else
         {
            c.y = (int)Math.round(i * yStep);
            c.x = (int)Math.round(i * xStep);
         }
         c.x += origin.x;
         c.y += origin.y;
         list.add(c);
      }
      return list;
   }
}
