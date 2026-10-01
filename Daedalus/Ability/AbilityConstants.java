package Daedalus.Ability;

public interface AbilityConstants
{
   public static final double CONE_ARC = Math.PI * (30.0 / 180.0);  // 30 degrees
   
   public enum TargetingType
   {
      POINT ("Point"),   // single point in rainge
      CONE  ("Cone"),    // 30-degree cone eminating from origin
      BEAM  ("Beam"),    // all tiles in line from origin to target
      BLAST ("Blast"),   // radius around and including target
      RING  ("Ring");    // radius around origin
   
      public String name;
      
      private TargetingType(String n)
      {
         name = n;
      }
   }
   
   public enum StatusEffectTag
   {
      HEALING;
   }
   
   public enum ImpactEffect
   {
      EXPLOSION,
      SPLASH;
   }
   
   public enum SpecialEffect
   {
      SMOKE,
      TELEPORT;
   }
}