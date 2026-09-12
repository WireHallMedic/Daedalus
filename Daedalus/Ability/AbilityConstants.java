package Daedalus.Ability;

public interface AbilityConstants
{
   public static final double CONE_ARC = Math.PI * (30.0 / 180.0);  // 30 degrees
   
   public enum TargetingType
   {
      POINT,   // single point in rainge
      CONE,    // 30-degree cone eminating from origin
      BEAM,    // all tiles in line from origin to target
      BLAST,   // radius around and including target
      RING;    // radius around origin
   }
   
   
}