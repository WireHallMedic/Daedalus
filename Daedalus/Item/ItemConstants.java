package Daedalus.Item;

import WidlerSuite.WSFontConstants;
import Daedalus.GUI.*;

public interface ItemConstants
{
   public static final int MAX_INVENTORY_SIZE = 20;
   
   public enum ItemBase
   {
      CREDITS     (WSFontConstants.CENT_TILE, WSFontConstants.CENT_TILE),
      WEAPON      ('}', '{'),
      SHIELD      (')', '{'),
      MOD         (']', '{'),
      GADGET      ('&', '&'),
      CONSUMABLE  ('*', '*'),
      POWER_CELL  (WSFontConstants.PLUS_MINUS_TILE, WSFontConstants.PLUS_MINUS_TILE);
      
      public int tileIndex;
      public int specialTileIndex;  // used for unique versions
      
      private ItemBase(int ti, int sti)
      {
         tileIndex = ti;
         specialTileIndex = sti;
      }
   }
}