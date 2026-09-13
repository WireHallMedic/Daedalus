package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Combat.*;

public class Armor extends ChargeItem implements Equippable, ItemConstants, GUIConstants
{
   public static final int DEFAULT_GADGET_SLOTS = 3;
   
	private Damage damageProtection;
   private Gadget[] gadgetList;


	public Damage getDamageProtection(){return damageProtection;}
   public Gadget[] getGadgetList(){return gadgetList;}

	public void setDamageProtection(Damage d){damageProtection = d;}
   public void setGadgetList(Gadget[] gl){gadgetList = gl;}

   public Armor(String name)
   {
      super(name, ItemBase.ARMOR);
      damageProtection = new Damage();
      setGadgetListSize(DEFAULT_GADGET_SLOTS);
   }
   
   // note that this clobbers any existing installed gadgets
   public void setGadgetListSize(int s)
   {
      gadgetList = new Gadget[s];
      for(int i = 0; i < s; i++)
         gadgetList[i] = null;
   }
   
   public int getGadgetListSize(){return gadgetList.length;}
   
   public Gadget getGadget(int i)
   {
      return gadgetList[i];
   }
   
   public Gadget takeGadget(int i)
   {
      Gadget gadget = gadgetList[i];
      gadgetList[i] = null;
      return gadget;
   }
   
   public void setGadget(int i, Gadget gadget)
   {
      gadgetList[i] = gadget;
   }
   
   public int getDamageProtection(CombatConstants.DamageType type)
   {
      return damageProtection.getValue(type);
   }
   
   public void setDamageProtection(CombatConstants.DamageType type, int value)
   {
      damageProtection.setValue(type, value);
   }
   
   public Damage absorbDamage(Damage d)
   {
      d = d.copy();
      d.subtract(getDamageProtection());
      return d;
   }
   
   @Override
   public void fullyCharge()
   {
      for(int i = 0; i < getGadgetListSize(); i++)
      {
         if(gadgetList[i] != null)
            gadgetList[i].fullyCharge();
      }
   }
   
   @Override
   public void charge()
   {
      for(int i = 0; i < getGadgetListSize(); i++)
      {
         if(gadgetList[i] != null)
            gadgetList[i].charge();
      }
   }
   
   // equippable
   public String getSummaryString()
   {
      return "Armor Description";
   }
   
   public String getComparisonString(Equippable that)
   {
      return "Armor Comparison";
   }
}