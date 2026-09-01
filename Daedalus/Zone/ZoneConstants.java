package Daedalus.Zone;

import WidlerSuite.WSFontConstants;

public interface ZoneConstants
{
   public enum TileBase
   {
      CLEAR          ("Clear", true, true, true, WSFontConstants.SMALL_BULLET_TILE),
      WALL           ("Wall", false, false, false, '#'),
      LOW_WALL       ("Low Wall", false, true, true, '='),
      BARS           ("Bars", false, false, true, ':'),
      DEEP_LIQUID    ("Liquid", false, true, true, '~'),
      SHALLOW_LIQUID ("Shallow Liquid", true, true, true, '-'),
      DOOR           ("Door", false, false, false, '|'),
      OPEN_DOOR      ("Open Door", true, true, true, '/'),
      SWITCH         ("Switch", false, false, true, '!'),
      FLIPPED_SWITCH ("Flipped Switch", false, false, true, WSFontConstants.INVERTED_EXCLAMATION_TILE),
      CHEST          ("Chest", false, true, true, '?'),
      OPEN_CHEST     ("Open Chest", false, true, true, WSFontConstants.INVERTED_QUESTION_TILE),
      ROUGH          ("Rough", true, true, true, ','),
      TERMINAL       ("Terminal", false, true, true, WSFontConstants.CAPITAL_OMEGA_TILE),
      EXIT           ("Exit", true, true, true, '>'),
      SPECIAL_EXIT   ("Special Exit", true, true, true, '<');
      
      public String name;
      public boolean lowPassable;
      public boolean highPassable;
      public boolean transparent;
      public int tileIndex;
      
      private TileBase(String n, boolean lp, boolean hp, boolean t, int ti)
      {
         name = n;
         lowPassable = lp;
         highPassable = hp;
         transparent = t;
         tileIndex = ti;
      }
   }
}