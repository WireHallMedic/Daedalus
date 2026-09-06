package Daedalus.Zone;

import java.awt.*;
import Daedalus.GUI.*;

public class Exit extends ZoneTile implements ZoneConstants, GUIConstants
{

   public Exit(TileBase base)
   {
      super(TileBase.EXIT);
   }
   
   public Exit(ZoneTile that)
   {
      super(that);
      this.lowPassable = that.lowPassable;
      this.highPassable = that.highPassable;
      this.transparent = that.transparent;
   }
   
   public Exit copy()
   {
      return new Exit(this);
   }
}