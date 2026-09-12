package Daedalus.Combat;

public interface CombatConstants
{
   public static final int KNOCKBACK_THRESHOLD = 10;
   
   public enum DamageType
   {
      CONCUSSION  ("Concussion", 2.0),
      PIERCE      ("Pierce", 1.0),
      FIRE        ("Thermal", 0.5),
      COLD        ("Cryo", 0.5),
      CORROSION   ("Corrosion", 0.5),
      ELECTRIC    ("Electric", 0.5);
      
      public String name;
      public double knockbackMultiplier;
      
      private DamageType(String n, double km)
      {
         name = n;
         knockbackMultiplier = km;
      }
   }
   
   
}