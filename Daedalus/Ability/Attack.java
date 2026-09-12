package Daedalus.Ability;

import Daedalus.Combat.*;
import Daedalus.Engine.*;

public class Attack extends Ability implements AbilityConstants, CombatConstants
{
	private Damage baseDamage;
	private Damage randomDamage;
	private boolean melee;
   private boolean damageDropoff;      // damage reduces over distance


	public Damage getBaseDamage(){return baseDamage;}
	public Damage getRandomDamage(){return randomDamage;}
	public boolean isMelee(){return melee;}
   public boolean hasDamageDropoff(){return damageDropoff;}


	public void setBaseDamage(Damage b){baseDamage = b;}
	public void setRandomDamage(Damage r){randomDamage = r;}
	public void setMelee(boolean m){melee = m;}
   public void setDamageDropoff(boolean dd){damageDropoff = dd;}


   public Attack(String n)
   {
      super(n);
      baseDamage = new Damage();
      randomDamage = new Damage();
      melee = false;
      damageDropoff = false;
   }
   
   public Damage rollDamage()
   {
      Damage d = baseDamage.copy();
      
      for(DamageType type: DamageType.values())
      {
         int val = randomDamage.getValue(type);
         if(val > 0)
            d.add(type, RNG.nextInt(val + 1));
      }
      return d;
   }
   
   public void setDamage(Damage base, Damage random)
   {
      baseDamage = base;
      randomDamage = random;
   }
   
   public static Attack getMock()
   {
      Attack attack = new Attack("Test Attack");
      attack.setBaseDamage(new Damage(DamageType.PIERCE, 4));
      attack.setRandomDamage(new Damage(DamageType.PIERCE, 2));
      attack.setTargetingType(TargetingType.CONE);
      attack.setRange(7);
      return attack;
   }
}