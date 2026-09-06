package Daedalus.Combat;

public class Damage implements CombatConstants
{
   private int[] values;
   
   public int getValue(DamageType type){return values[type.ordinal()];}
   
   public void setValue(DamageType type, int val){values[type.ordinal()] = val;}

   public Damage()
   {
      values = new int[DamageType.values().length];
   }
   
   public Damage copy()
   {
      Damage d = new Damage();
      d.add(this);
      return d;
   }
   
   public void add(Damage that)
   {
      for(int i = 0; i < DamageType.values().length; i++)
         values[i] += that.values[i];
   }
   
   public void subtract(Damage that)
   {
      for(int i = 0; i < DamageType.values().length; i++)
         values[i] -= that.values[i];
   }
}