package Daedalus.Zone;

import java.awt.*;
import Daedalus.GUI.*;
import WidlerSuite.WSFontConstants;

public class Exit extends ZoneTile implements ZoneConstants, GUIConstants
{
	private ExitDirection exitDirection;
   private Exit mate;


	public ExitDirection getExitDirection(){return exitDirection;}
   public Exit getMate(){return mate;}
   
   public void setMate(Exit e){mate = e;}


   public Exit()
   {
      super(TileBase.EXIT);
      setExitDirection('N');
      mate = null;
   }
   
   public Exit(char ed)
   {
      this();
      setExitDirection(ed);
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
      exit.setExitDirection(that.exitDirection.getOpposite());
      exit.pair(that);
      return exit;
   }
   
   public void pair(Exit that)
   {
      this.mate = that;
      if(that != null)
         that.mate = this;
   }
   
   public void setExitDirection(char dir)
   {
      setExitDirection(ExitDirection.getByChar(dir));
   }
   
   public void setExitDirection(ExitDirection dir)
   {
      exitDirection = dir;
      setTileIndex(exitDirection.tileIndex);
   }

   
}