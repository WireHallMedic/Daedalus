package Daedalus.Engine;

import WidlerSuite.Coord;
import WidlerSuite.StraightLine;
import Daedalus.Zone.*;
import Daedalus.Actor.*;
import java.util.*;

public class EngineTools
{
   public static Coord getAffectedPoint(Coord origin, Coord target)
   {
      if(origin.equals(target))
         return origin.copy();
      Coord affected = target.copy();
      Vector<Coord> lineList = StraightLine.findLine(origin, target);
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
}