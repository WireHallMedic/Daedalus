package Daedalus.Ability;

public interface AbilityConstants
{
   public static final double CONE_ARC = Math.PI * (30.0 / 180.0);  // 30 degrees
   
   public enum TargetingType
   {
      POINT,
      CONE,
      BEAM,
      BLAST,
      RING;
   }
   
   
}