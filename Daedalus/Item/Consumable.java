package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Engine.*;

public class Consumable extends Item implements ItemConstants, GUIConstants
{
   public enum BaseType
   {
      MED_PATCH,
      SMOKE_GRENADE,
      GRENADE;
   }
   
   public Consumable(String name)
   {
      super(name, ItemBase.CONSUMABLE);
   }

}