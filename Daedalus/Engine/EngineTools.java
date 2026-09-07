package Daedalus.Engine;

import WidlerSuite.Coord;
import WidlerSuite.StraightLine;
import Daedalus.Zone.*;
import Daedalus.Actor.*;
import java.util.*;

public class EngineTools
{
   // stop if hits actor or wall
   public static Coord getAffectedPoint(Coord origin, Coord target)
   {
      if(origin.equals(target))
         return origin.copy();
      Coord affected = target.copy();
      Vector<Coord> lineList = StraightLine.findLine(origin, target, StraightLine.REMOVE_ORIGIN);
      for(int i = 0; i < lineList.size(); i++)
      {
         if(!Game.getCurZone().getTile(lineList.elementAt(i)).isHighPassable() ||
            Game.isActorAt(lineList.elementAt(i)))
         {
            affected = lineList.elementAt(i);
            break;
         }
      }
      return affected;
   }
   
   
   // stop if hits actor or one short of wall
   public static Vector<Coord> getAffectedBlast(Coord origin, Coord target)
   {
      Coord blastCenter = target.copy();
      Vector<Coord> lineList = StraightLine.findLine(origin, target, StraightLine.REMOVE_ORIGIN);
      for(int i = 0; i < lineList.size() - 1; i++)
      {
         if(!Game.getCurZone().getTile(lineList.elementAt(i + 1)).isHighPassable() ||
            Game.isActorAt(lineList.elementAt(i)))
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
}