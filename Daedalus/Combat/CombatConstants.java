package Daedalus.Combat;

public interface CombatConstants
{
   public static final int KNOCKBACK_THRESHOLD = 10;
   
   public enum DamageType
   {
      CONCUSSION  ("Concussion", 1.5),
      PIERCE      ("Pierce", 1.0),
      FIRE        ("Thermal", 0.25),
      COLD        ("Cryo", 0.25),
      CORROSION   ("Corrosion", 0.5),
      ELECTRIC    ("Electric", 1.0);
      
      public String name;
      public double knockbackMultiplier;
      
      private DamageType(String n, double km)
      {
         name = n;
         knockbackMultiplier = km;
      }
   }
   
   
}