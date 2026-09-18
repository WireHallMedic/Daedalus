package Daedalus.Zone;

import java.awt.*;
import Daedalus.GUI.*;
import WidlerSuite.WSFontConstants;

public class Exit extends ZoneTile implements ZoneConstants, GUIConstants
{
	private char exitDirection;
   private Exit mate;


	public char getExitDirection(){return exitDirection;}
   public Exit getMate(){return mate;}
   
   public void setMage(Exit e){mate = e;}


   public Exit()
   {
      super(TileBase.EXIT);
      setExitDirection('N');
      mate = null;
   }

   
   public Exit(Exit that)
   {
      super(that);
      this.exitDirection = that.exitDirection;
      this.mate = that.mate;
   }
   
   public Exit copy()
   {
      return new Exit(this);
   }
   
   public static Exit getComplement(Exit that)
   {
      Exit exit = new Exit();
      exit.setExitDirectionFromOpposite(that.getExitDirection());
      exit.pair(that);
      return exit;
   }
   
   public void pair(Exit that)
   {
      this.mate = that;
      that.mate = this;
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


	public void setExitDirectionFromOpposite(char e)
   {
      switch(e)
      {
         case 'N':   setExitDirection('S');
                     break;
         case 'E':   setExitDirection('W');
                     break;
         case 'S':   setExitDirection('N');
                     break;
         case 'W':   setExitDirection('E');
                     break;
         case 'U':   setExitDirection('D');
                     break;
         case 'D':   setExitDirection('U');
                     break;
         default :   throw new Error("Invalid direction for exit: " + exitDirection);
      }
   }
}