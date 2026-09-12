package Daedalus.Ability;

import Daedalus.Combat.*;
import Daedalus.Engine.*;

public class Attack extends Ability implements AbilityConstants, CombatConstants
{
	private Damage baseDamage;
	private Damage randomDamage;
	private boolean melee;


	public Damage getBaseDamage(){return baseDamage;}
	public Damage getRandomDamage(){return randomDamage;}
	public boolean isMelee(){return melee;}


	public void setBaseDamage(Damage b){baseDamage = b;}
	public void setRandomDamage(Damage r){randomDamage = r;}
	public void setMelee(boolean m){melee = m;}


   public Attack(String n)
   {
      super(n);
      baseDamage = new Damage();
      randomDamage = new Damage();
      melee = false;
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