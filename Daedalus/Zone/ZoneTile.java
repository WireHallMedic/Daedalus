package Daedalus.Zone;

import java.awt.*;
import Daedalus.GUI.*;

public class ZoneTile extends ImageTile implements ZoneConstants, GUIConstants
{
	protected boolean lowPassable;
	protected boolean highPassable;
	protected boolean transparent;


	public boolean isLowPassable(){return lowPassable;}
	public boolean isHighPassable(){return highPassable;}
	public boolean isTransparent(){return transparent;}


	public void setLowPassable(boolean l){lowPassable = l;}
	public void setHighPassable(boolean h){highPassable = h;}
	public void setTransparent(boolean t){transparent = t;}

   public ZoneTile(TileBase base)
   {
      super(SQUARE_PALETTE);
      set(base, WHITE, BLACK);
   }
   
   public ZoneTile(ZoneTile that)
   {
      super(that);
      this.lowPassable = that.lowPassable;
      this.highPassable = that.highPassable;
      this.transparent = that.transparent;
   }
   
   public ZoneTile copy()
   {
      return new ZoneTile(this);
   }
   
   public void set(TileBase base, int fg, int bg)
   {
      set(base);
      fgColor = fg;
      bgColor = bg;
   }
   
   public void set(TileBase base)
   {
      setTileIndex(base.tileIndex);
      lowPassable = base.lowPassable;
      highPassable = base.highPassable;
      transparent = base.transparent;
   }
}