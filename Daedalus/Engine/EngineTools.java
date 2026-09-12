package Daedalus.Engine;

import WidlerSuite.Vect;
import WidlerSuite.Coord;
import WidlerSuite.StraightLine;
import WidlerSuite.ShadowFoVRect;
import Daedalus.Zone.*;
import Daedalus.Ability.*;
import Daedalus.Actor.*;
import java.util.*;

public class EngineTools implements AbilityConstants
{
   public static int getAngbandDistance(int x1, int y1, int x2, int y2)
   {
      int x = Math.abs(x2 - x1);
      int y = Math.abs(y2 - y1);
      if(x > y)
         return x + (y / 2);
      return y + (x / 2);
   }
   public static int getAngbandDistance(Coord c1, Coord c2){return getAngbandDistance(c1.x, c1.y, c2.x, c2.y);}
   
   // stop if hits actor or wall
   public static Coord getAffectedPoint(Coord origin, Coord target, int range)
   {
      if(origin.equals(target))
         return origin.copy();
      Coord affected = target.copy();
      Vector<Coord> lineList = StraightLine.findLine(origin, target, StraightLine.REMOVE_ORIGIN);
      for(int i = 0; i < lineList.size(); i++)
      {
         if(!Game.getCurZone().getTile(lineList.elementAt(i)).isHighPassable() ||
            Game.isActorAt(lineList.elementAt(i)) ||
            range <= getAngbandDistance(origin, lineList.elementAt(i)))
         {
            affected = lineList.elementAt(i);
            break;
         }
      }
      return affected;
   }
   
   
   // stop if hits actor or one short of wall, returns 3x3 area
   public static Vector<Coord> getAffectedBlast(Coord origin, Coord target, int range)
   {
      Coord blastCenter = target.copy();
      Vector<Coord> lineList = StraightLine.findLine(origin, target, StraightLine.REMOVE_ORIGIN);
      for(int i = 0; i < lineList.size() - 1; i++)
      {
         if(!Game.getCurZone().getTile(lineList.elementAt(i + 1)).isHighPassable() ||
            Game.isActorAt(lineList.elementAt(i)) ||
            range <= getAngbandDistance(origin, lineList.elementAt(i)))
         {
            blastCenter = lineList.elementAt(i);
            break;
         }
      }
      Vector<Coord> blastArea = new Vector<Coord>();
      for(int x = -1; x < 2; x++)
      for(int y = -1; y < 2; y++)
         blastArea.add(new Coord(blastCenter.x + x, blastCenter.y + y));
      return blastArea;
   }
   
   // like a blast, centered on origin, excludes origin
   public static Vector<Coord> getAffectedRing(Coord origin, int radius)
   {
      Vector<Coord> eminationList = getEmination(origin, radius);
      for(int i = 0; i < eminationList.size(); i++)
      {
         if(eminationList.elementAt(i).equals(origin))
         {
            eminationList.removeElementAt(i);
            i--;
         }
      }
      return eminationList;
   }
   
   // return affected area of a cone.
   public static Vector<Coord> getAffectedCone(Coord origin, Coord target, int range)
   {
      Vector<Coord> tileList = new Vector<Coord>();
      if(origin.equals(target))
      {
         tileList.add(target.copy());
         return tileList;
      }
      double minAngle = origin.getAngleTo(target) - (CONE_ARC / 2);
      double maxAngle = origin.getAngleTo(target) + (CONE_ARC / 2);
      Vect minVect = new Vect(minAngle, 20); // use an arbitrarily large number so we don't have
      Vect maxVect = new Vect(maxAngle, 20); // gaps near the end
      minVect.add(origin);
      maxVect.add(origin);
      Vector<Coord> endingLine = StraightLine.findLine(minVect.getAsCoord(), maxVect.getAsCoord());
      for(Coord farTile: endingLine)
      {
         Vector<Coord> perpendicularLine = StraightLine.findLine(origin, farTile, StraightLine.REMOVE_ORIGIN);
         for(Coord curTile: perpendicularLine)
         {
            if(getAngbandDistance(curTile, origin) <= range)
               if(!containsDuplicate(tileList, curTile))
                  tileList.add(curTile);
            if(!Game.getCurZone().getTile(curTile).isHighPassable())
               break;
         }
      }
      return tileList;
   }
   
   // return affected area of a cone.
   public static Vector<Coord> getAffectedBeam(Coord origin, Coord target, int range)
   {
      Vector<Coord> tileList = new Vector<Coord>();
      if(origin.equals(target))
      {
         tileList.add(target.copy());
         return tileList;
      }
      double fireAngle = origin.getAngleTo(target);
      target = new Coord(new Vect(fireAngle, (double)range));
      target.add(origin);
      Vector<Coord> line = StraightLine.findLine(origin, target, StraightLine.REMOVE_ORIGIN);

      for(Coord curTile: line)
      {
         tileList.add(curTile);
         if(!Game.getCurZone().getTile(curTile).isHighPassable())
            break;
      }
      return tileList;
   }
   
   // use shadowcasting to get area affected by blast, ring, etc
   private static Vector<Coord> getEmination(Coord origin, int radius)
   {
      int diameter = radius + 1 + radius;
      int xStart = origin.x - radius;
      int yStart = origin.y - radius;
      
      boolean[][] blockingMap = new boolean[diameter][diameter];
      for(int x = 0; x < diameter; x++)
      for(int y = 0; y < diameter; y++)
      {
         blockingMap[x][y] = Game.getCurZone().getTile(xStart + x, yStart + y).isHighPassable();
      }
      ShadowFoVRect fov = new ShadowFoVRect(blockingMap);
      fov.calcFoV(radius, radius, radius + 1);
      Vector<Coord> areaList = new Vector<Coord>();
      for(int x = 0; x < diameter; x++)
      for(int y = 0; y < diameter; y++)
      {
         if(fov.isVisible(x, y))
            if(EngineTools.getAngbandDistance(x, y, radius, radius) <= radius)
               areaList.add(new Coord(origin.x + x - radius, origin.y + y - radius));
      }
      return areaList;
   }
   
   private static boolean containsDuplicate(Vector<Coord> list, Coord c)
   {
      for(int i = 0; i < list.size(); i++)
         if(list.elementAt(i).equals(c))
            return true;
      return false;
   }
   
   
   // rings radiate out from origin
//    public static Vector<Coord> getAffectedRing(Coord origin, int radius)
//    {
//       
//       fov = new ShadowFoVRect(curZone.getVisibilityMap());
//       fov.calcFoV(getTileLoc().x, getTileLoc().y, getVisionRadius());
//       Coord blastCenter = target.copy();
//       Vector<Coord> lineList = StraightLine.findLine(origin, target, StraightLine.REMOVE_ORIGIN);
//       for(int i = 0; i < lineList.size() - 1; i++)
//       {
//          if(!Game.getCurZone().getTile(lineList.elementAt(i + 1)).isHighPassable() ||
//             Game.isActorAt(lineList.elementAt(i)))
//          {
//             blastCenter = lineList.elementAt(i);
//             break;
//          }
//       }
//       Vector<Coord> blastArea = new Vector<Coord>();
//       for(int x = -1; x < 2; x++)
//       for(int y = -1; y < 2; y++)
//          blastArea.add(new Coord(blastCenter.x + x, blastCenter.y + y));
//       return blastArea;
//    }
}