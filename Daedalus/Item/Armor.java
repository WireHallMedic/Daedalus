package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Combat.*;

public class Armor extends Item implements ItemConstants, GUIConstants
{
	private Damage damageProtection;


	public Damage getDamageProtection(){return damageProtection;}


	public void setDamageProtection(Damage d){damageProtection = d;}

   public Armor(String name)
   {
      super(name, ItemBase.ARMOR);
      damageProtection = new Damage();
   }
   
   public int getDamageProtection(CombatConstants.DamageType type)
   {
      return damageProtection.getValue(type);
   }
   
   public void setDamageProtection(CombatConstants.DamageType type, int value)
   {
      damageProtection.setValue(type, value);
   }
}