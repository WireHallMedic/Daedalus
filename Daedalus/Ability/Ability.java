package Daedalus.Ability;

import Daedalus.Zone.*;
import Daedalus.Engine.*;
import java.util.*;

public class Ability implements AbilityConstants
{
	private String name;
	private TargetingType targetingType;
	private int range;
   private String hitVerb;
   private StatusEffect statusEffect;
   private ImpactEffect impactEffect;


	public String getName(){return name;}
	public TargetingType getTargetingType(){return targetingType;}
	public int getRange(){return range;}
   public String getHitVerb(){return hitVerb;}
   public StatusEffect getStatusEffect(){return statusEffect;}
   public ImpactEffect getImpactEffect(){return impactEffect;}


	public void setName(String n){name = n;}
	public void setTargetingType(TargetingType t){targetingType = t;}
	public void setRange(int r){range = r;}
   public void setHitVerb(String h){hitVerb = h;}
   public void setStatusEffect(StatusEffect se){statusEffect = se.copy();}
   public void setImpactEffect(ImpactEffect ie){impactEffect = ie;}

   public Ability(String n)
   {
      name = n;
      targetingType = TargetingType.POINT;
      range = 10;
      hitVerb = "affects";
      statusEffect = null;
      impactEffect = null;
   }
   
   public Vector<String> getDescriptionList()
   {
      Vector<String> list = new Vector<String>();
      list.add(getName());
      list.add("Range:        " + getRange());
      list.add("Targeting:    " + getTargetingType().name);
      if(statusEffect != null)
         list.add("Effect:       " + getStatusEffect().getName());
      return list;
   }
   
   // origin for visual effects and knockback
   public Coord getEffectOrigin(Coord origin, Coord target, int range)
   {
      if(targetingType == TargetingType.BLAST)
         return EngineTools.getBlastOrigin(origin, target, getRange());
      return origin;
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