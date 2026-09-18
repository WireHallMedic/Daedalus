package Daedalus.Zone;

import java.awt.*;
import Daedalus.GUI.*;
import WidlerSuite.WSFontConstants;

public class Exit extends ZoneTile implements ZoneConstants, GUIConstants
{
	private char exitDirection;


	public char getExitDirection(){return exitDirection;}


   public Exit()
   {
      super(TileBase.EXIT);
      setExitDirection('N');
   }

   
   public Exit(Exit that)
   {
      super(that);
      this.exitDirection = that.exitDirection;
   }
   
   public Exit copy()
   {
      return new Exit(this);
   }


	public void setExitDirection(char e)
   {
      exitDirection = Character.toUpperCase(e);
      switch(exitDirection)
      {
         case 'N':   setTileIndex(WSFontConstants.UP_TRIANGLE_TILE);
                     break;
         case 'E':   setTileIndex(WSFontConstants.RIGHT_TRIANGLE_TILE);
                     break;
         case 'S':   setTileIndex(WSFontConstants.DOWN_TRIANGLE_TILE);
                     break;
         case 'W':   setTileIndex(WSFontConstants.LEFT_TRIANGLE_TILE);
                     break;
         case 'U':   setTileIndex(WSFontConstants.UP_ARROW_TILE);
                     break;
         case 'D':   setTileIndex(WSFontConstants.DOWN_ARROW_TILE);
                     break;
         default :   throw new Error("Invalid direction for exit: " + exitDirection);
      }
   }
}