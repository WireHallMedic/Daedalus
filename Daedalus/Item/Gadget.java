package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Combat.*;
import java.util.*;

public class Gadget extends ChargeItem implements Equippable, ItemConstants, GUIConstants
{

   public Gadget(String name)
   {
      super(name, ItemBase.GADGET);
   }
   
   
   public Vector<String> getDescriptionList()
   {
      Vector<String> list = new Vector<String>();
      list.add(getName());
      return list;
   }
}