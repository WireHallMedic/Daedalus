package Daedalus.Ability;

import Daedalus.GUI.*;

public interface AbilityConstants
{
   public static final double CONE_ARC = Math.PI * (30.0 / 180.0);  // 30 degrees
   
   public static final String KNOCKBACK_TAG = "@KnockbackTag"; // dunno if checking is by address or value, this works for both
   
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
   
   // any status effect that can be combined requires at least one tag
   public enum StatusEffectTag
   {
      HEALING     (FontConstants.HEART_TILE, GUIConstants.RED),
      HASTED      (FontConstants.UP_TRIANGLE_TILE, GUIConstants.LIGHT_BLUE),
      ENTANGLED   ('&', GUIConstants.RED),
      SLOWED      (FontConstants.DOWN_TRIANGLE_TILE, GUIConstants.VIVID_RED),
      BURNING     ('^', GUIConstants.ORANGE),                     // hightest, not additive
      VULNERABLE  (FontConstants.HEART_TILE, GUIConstants.ACID);  // hightest, not additive
      
      public int tileIndex;
      public int color;
      
      private StatusEffectTag(int ti, int c)
      {
         tileIndex = ti;
         color = c;
      }
   }
   
   public enum ImpactEffect
   {
      EXPLOSION,
      SPLASH;
   }
   
   public enum SpecialEffect
   {
      SMOKE,
      TELEPORT,
      DECOY;
   }
}