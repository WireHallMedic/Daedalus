package Daedalus.Item;

import WidlerSuite.WSFontConstants;
import Daedalus.GUI.*;

public interface ItemConstants
{
   public static final int MAX_INVENTORY_SIZE = 20;
   public static final int STANDARD_CHARGE_PER_TURN = 60;
   
   public enum ItemBase
   {
      CREDITS     (WSFontConstants.CENT_TILE, WSFontConstants.CENT_TILE),
      WEAPON      ('}', '{'),
      SHIELD      (')', '('),
      ARMOR       (']', '['),
      MOD         ('"', '"'),
      GADGET      ('&', '&'),
      CONSUMABLE  ('*', '*');
      
      public int tileIndex;
      public int specialTileIndex;  // used for unique versions
      
      private ItemBase(int ti, int sti)
      {
         tileIndex = ti;
         specialTileIndex = sti;
      }
   }
}