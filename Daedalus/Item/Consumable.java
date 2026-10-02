package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Ability.*;
import Daedalus.Engine.*;

public class Consumable extends Item implements ItemConstants, GUIConstants
{
   public enum BaseType
   {
      MED_PATCH,
      SMOKE_GRENADE,
      GRENADE,
      DECOY;
   }
   
	private Ability ability;


	public Ability getAbility(){return ability;}


	public void setAbility(Ability a){ability = a;}


   public Consumable(String name)
   {
      super(name, ItemBase.CONSUMABLE);
      ability = null;
   }

}