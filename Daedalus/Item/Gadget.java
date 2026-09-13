package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Combat.*;

public class Gadget extends ChargeItem implements Equippable, ItemConstants, GUIConstants
{

   public Gadget(String name)
   {
      super(name, ItemBase.GADGET);
   }
   
   // equippable
   public String getSummaryString()
   {
      return "Gadget Description";
   }
   
   public String getComparisonString(Equippable that)
   {
      return "Gadget Comparison";
   }
}