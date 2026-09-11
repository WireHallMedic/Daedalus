package Daedalus.Ability;

import Daedalus.Combat.*;

public class Attack extends Ability implements AbilityConstants, CombatConstants
{
	private Damage damage;
   private boolean melee;


	public Damage getDamage(){return damage;}
   public boolean isMelee(){return melee;}


	public void setDamage(Damage d){damage = d;}
   public void setMelee(boolean m){melee = m;}


   public Attack(String n)
   {
      super(n);
      damage = new Damage();
      melee = false;
   }
   
   public static Attack getMock()
   {
      Attack attack = new Attack("Test Attack");
      Damage damage = new Damage(DamageType.PIERCE, 6);
      attack.setDamage(damage);
      attack.setTargetingType(TargetingType.CONE);
      attack.setRange(7);
      return attack;
   }
}