package Daedalus.Ability;

import Daedalus.Combat.*;

public class Attack extends Ability implements AbilityConstants, CombatConstants
{
	private Damage damage;


	public Damage getDamage(){return damage;}


	public void setDamage(Damage d){damage = d;}


   public Attack(String n)
   {
      super(n);
      damage = new Damage();
   }
   
   public static Attack getMock()
   {
      Attack attack = new Attack("Test Attack");
      Damage damage = new Damage(DamageType.PIERCE, 6);
      attack.setDamage(damage);
      return attack;
   }
}