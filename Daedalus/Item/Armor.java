package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Combat.*;
import java.util.*;

public class Armor extends ChargeItem implements Equippable, ItemConstants, GUIConstants, CombatConstants
{
   public enum BaseType
   {
      UTILITY_HARNESS,  // just a place for gadgets
      SCOUT,            // no damage protection, but extra gadgets
      STANDARD,         // physical protection
      ASSAULT;          // physical and energy protection, fewer gadgets
   }
   
   public static final int DEFAULT_GADGET_SLOTS = 3;
   
	private Damage damageProtection;
	private Vector<Gadget> gadgetList;
	private int maxGadgets;


	public Damage getDamageProtection(){return damageProtection;}
	public Vector<Gadget> getGadgetList(){return gadgetList;}
	public int getMaxGadgets(){return maxGadgets;}


	public void setDamageProtection(Damage d){damageProtection = d;}
	public void setGadgetList(Vector<Gadget> g){gadgetList = g;}
	public void setMaxGadgets(int m){maxGadgets = m;}


   public Armor(String name)
   {
      super(name, ItemBase.ARMOR);
      damageProtection = new Damage();
      gadgetList = new Vector<Gadget>();
      maxGadgets = DEFAULT_GADGET_SLOTS;
   }
   
   public int getGadgetListSize(){return gadgetList.size();}
   
   public Gadget getGadget(int i)
   {
      if(i < gadgetList.size())
         return gadgetList.elementAt(i);
      return null;
   }
   
   public Gadget takeGadget(int i)
   {
      Gadget gadget = getGadget(i);
      gadgetList.removeElementAt(i);
      return gadget;
   }
   
   public void addGadget(Gadget gadget)
   {
      gadgetList.add(gadget);
   }
   
   public boolean canAddGadget()
   {
      return gadgetList.size() < maxGadgets;
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
      // null protection as this is called from super.constructor()
      if(gadgetList != null)
         for(Gadget gadget: gadgetList)
         {
            gadget.fullyCharge();
         }
   }
   
   @Override
   public void charge()
   {
      for(Gadget gadget: gadgetList)
      {
         gadget.charge();
      }
   }
   
   @Override
   public void fullyDischarge()
   {
      for(Gadget gadget: gadgetList)
      {
         gadget.fullyDischarge();
      }
   }
   
   
   public Vector<String> getDescriptionList()
   {
      Vector<String> list = new Vector<String>();
      list.add(getName());
      
      list.add("Protection:");
      for(DamageType damageType: DamageType.values())
      {
         int dmg = damageProtection.getValue(damageType);
         if(dmg != 0)
         {
            String str = " " + damageType.name + " ";
            while(str.length() < 14)
               str += " ";
            str += dmg;
            list.add(str);
         }
      }
      list.add("Gadget Slots: " + getMaxGadgets());
      return list;
   }
}