package Daedalus.Ability;

import WidlerSuite.Coord;
import WidlerSuite.StraightLine;
import Daedalus.Zone.*;
import Daedalus.Engine.*;
import java.util.*;

public class Ability implements AbilityConstants
{
	private String name;
	private TargetingType targetingType;
	private int range;


	public String getName(){return name;}
	public TargetingType getTargetingType(){return targetingType;}
	public int getRange(){return range;}


	public void setName(String n){name = n;}
	public void setTargetingType(TargetingType t){targetingType = t;}
	public void setRange(int r){range = r;}

   public Ability(String n)
   {
      name = n;
      targetingType = TargetingType.POINT;
      range = 10;
   }
   
   public Vector<Coord> getAffectedTiles(Coord origin, Coord target)
   {
      Vector<Coord> tileList = new Vector<Coord>();
      if(targetingType == TargetingType.POINT)
      {
         tileList.add(EngineTools.getAffectedPoint(origin, target, getRange()));
      }
      else if(targetingType == TargetingType.BLAST)
      {
         for(Coord c: EngineTools.getAffectedBlast(origin, target, getRange()))
            tileList.add(c);
      }
      else if(targetingType == TargetingType.RING)
      {
         for(Coord c: EngineTools.getAffectedRing(origin, getRange()))
            tileList.add(c);
      }
      else if(targetingType == TargetingType.CONE)
      {
         for(Coord c: EngineTools.getAffectedCone(origin, target, getRange()))
            tileList.add(c);
      }
      else if(targetingType == TargetingType.BEAM)
      {
         for(Coord c: EngineTools.getAffectedBeam(origin, target, getRange()))
            tileList.add(c);
      }
      return tileList;
   }
}