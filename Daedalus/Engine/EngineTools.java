package Daedalus.Engine;

import WidlerSuite.Coord;
import WidlerSuite.StraightLine;
import Daedalus.Zone.*;
import Daedalus.Actor.*;
import java.util.*;

public class EngineTools
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