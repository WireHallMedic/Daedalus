package Daedalus.Zone;

import Daedalus.Engine.*;
import Daedalus.GUI.*;

public interface ZoneConstants
{
   public static final int ITEM_SEARCH_DIAMETER = 15;
   // adjacency lists: x, y, stepCost
   public static final int[][] RECT_ORTHO = {{-1, 0, 10}, {0, -1, 10}, {1, 0, 10}, {0, 1, 10}};
   public static final int[][] RECT_DIAG = {{-1, 0, 10}, {0, -1, 10}, {1, 0, 10}, {0, 1, 10},
                                            {-1, -1, 14}, {-1, 1, 14}, {1, -1, 14}, {1, 1, 14}};
   
   public enum TileBase
   {
      CLEAR          ("Clear", true, true, true, FontConstants.SMALL_BULLET_TILE),
      PATH           ("Path", true, true, true, FontConstants.BULLET_TILE),
      WALL           ("Wall", false, false, false, '#'),
      LOW_WALL       ("Low Wall", false, true, true, '='),
      BARS           ("Bars", false, false, true, ':'),
      DEEP_LIQUID    ("Liquid", false, true, true, '~'),
      SHALLOW_LIQUID ("Shallow Liquid", true, true, true, '-'),
      DOOR           ("Door", false, false, false, '|'),
      OPEN_DOOR      ("Open Door", true, true, true, '/'),
      SWITCH         ("Switch", false, false, true, '!'),
      FLIPPED_SWITCH ("Flipped Switch", false, false, true, FontConstants.INVERTED_EXCLAMATION_TILE),
      CHEST          ("Chest", false, true, true, '?'),
      OPEN_CHEST     ("Open Chest", false, true, true, FontConstants.INVERTED_QUESTION_TILE),
      ROUGH          ("Rough", true, true, true, ','),
      TERMINAL       ("Terminal", false, true, true, FontConstants.CAPITAL_OMEGA_TILE),
      SIGN           ("Sign", false, true, true, FontConstants.IDENTICAL_TO_TILE),
      EXIT           ("Exit", true, true, true, FontConstants.INTERSECTION_TILE);
      
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
      
      public static TileBase getByTileIndex(int tileIndex)
      {
         for(TileBase base: TileBase.values())
         {
            if(tileIndex == base.tileIndex)
               return base;
         }
         return null;
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
      
      public static Direction getDirectionTo(Coord origin, Coord target)
      {
         Coord c = new Coord(target.x - origin.x, target.y - origin.y);
         return getFromCoord(c);
      }
      
      // returns a random, non-origin direction
      public static Direction random()
      {
         return Direction.values()[RNG.nextInt(8) + 1];
      }
      
      public Direction nextClockwise()
      {
         if(this == ORIGIN)
            return this;
         int index = this.ordinal() + 1;
         if(index == Direction.values().length)
            index = 1;
         return Direction.values()[index];
      }
      
      public Direction prevClockwise()
      {
         if(this == ORIGIN)
            return this;
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
            index -= 8;
         return Direction.values()[index];
      }
   }
   
   public enum ExitDirection
   {
      NORTH ('N', FontConstants.UP_TRIANGLE_TILE),
      EAST  ('E', FontConstants.RIGHT_TRIANGLE_TILE),
      SOUTH ('S', FontConstants.DOWN_TRIANGLE_TILE),
      WEST  ('W', FontConstants.LEFT_TRIANGLE_TILE),
      UP    ('U', FontConstants.UP_ARROW_TILE),
      DOWN  ('D', FontConstants.DOWN_ARROW_TILE);
      
      public char character;
      public int tileIndex;
      
      private ExitDirection(char ch, int ti)
      {
         character = ch;
         tileIndex = ti;
      }
      
      public static ExitDirection getByChar(char ch)
      {
         ch = Character.toUpperCase(ch);
         for(ExitDirection dir : ExitDirection.values())
            if(dir.character == ch)
               return dir;
         throw new Error("Invalid direction for exit: " + ch);
      }
      
      public ExitDirection getOpposite()
      {
         switch(this)
         {
            case NORTH  :  return SOUTH;
            case EAST   :  return WEST;
            case SOUTH  :  return NORTH;
            case WEST   :  return EAST;
            case UP     :  return DOWN;
            case DOWN   :  return UP;
         }
         return null;
      }
   }
   
   public enum Durability
   {
      FRAGILE,
      STANDARD,
      UNBREAKABLE;
   }

}