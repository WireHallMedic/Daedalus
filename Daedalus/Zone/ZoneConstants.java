package Daedalus.Zone;

import WidlerSuite.WSFontConstants;
import WidlerSuite.Coord;
import Daedalus.Engine.*;

public interface ZoneConstants
{
   public static final int ITEM_SEARCH_DIAMETER = 15;
   
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
   
   public enum Direction
   {
      ORIGIN      (0, 0),
      NORTH       (0, -1),
      NORTH_EAST  (1, -1),
      EAST        (1, 0),
      SOUTH_EAST  (1, 1),
      SOUTH       (0, 1),
      SOUTH_WEST  (-1, 1),
      WEST        (-1, 0),
      NORTH_WEST  (-1, -1);
      
      public int x;
      public int y;
      
      private Direction(int _x, int _y)
      {
         x = _x;
         y = _y;
      }
      
      public Coord getAsCoord()
      {
         return new Coord(x, y);
      }
      
      public static Direction getFromCoord(Coord c)
      {
         int x = Math.min(1, Math.max(c.x, -1));
         int y = Math.min(1, Math.max(c.y, -1));
         for(Direction dir : Direction.values())
         {
            if(dir.x == x && dir.y == y)
               return dir;
         }
         return null;
      }
      
      // returns a random, non-origin direction
      public static Direction random()
      {
         return Direction.values()[RNG.nextInt(8) + 1];
      }
      
      public Direction nextClockwise()
      {
         int index = this.ordinal() + 1;
         if(index == Direction.values().length)
            index = 1;
         return Direction.values()[index];
      }
      
      public Direction prevClockwise()
      {
         int index = this.ordinal() - 1;
         if(index == 0)
            index = Direction.values().length - 1;
         return Direction.values()[index];
      }
      
      public Direction opposite()
      {
         if(this == ORIGIN)
            return this;
         int index = this.ordinal() + 4;
         if(index >= Direction.values().length)
            index -= 4;
         return Direction.values()[index];
      }
   }
}