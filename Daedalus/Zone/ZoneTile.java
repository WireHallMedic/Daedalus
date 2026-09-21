package Daedalus.Zone;

import java.awt.*;
import Daedalus.GUI.*;

public class ZoneTile extends ImageTile implements ZoneConstants, GUIConstants
{
	protected boolean lowPassable;
	protected boolean highPassable;
	protected boolean transparent;
   protected int decorationType;


	public boolean isLowPassable(){return lowPassable;}
	public boolean isHighPassable(){return highPassable;}
	public boolean isTransparent(){return transparent;}
   public int getDecorationType(){return decorationType;}


	public void setLowPassable(boolean l){lowPassable = l;}
	public void setHighPassable(boolean h){highPassable = h;}
	public void setTransparent(boolean t){transparent = t;}
   public void setDecorationType(int d){decorationType = d;}

   public ZoneTile(TileBase base)
   {
      super(SQUARE_PALETTE);
      set(base, WHITE, BLACK);
      decorationType = 0;
   }
   
   public ZoneTile(ZoneTile that)
   {
      super(that);
      this.lowPassable = that.lowPassable;
      this.highPassable = that.highPassable;
      this.transparent = that.transparent;
      this.decorationType = that.decorationType;
   }
   
   public ZoneTile copy()
   {
      return new ZoneTile(this);
   }
   
   public void set(TileBase base, int fg, int bg, int dt)
   {
      set(base);
      fgColor = fg;
      bgColor = bg;
      decorationType = dt;
   }
   public void set(TileBase base, int fg, int bg){set(base, fg, bg, 0);}
   
   public void set(TileBase base)
   {
      setTileIndex(base.tileIndex);
      lowPassable = base.lowPassable;
      highPassable = base.highPassable;
      transparent = base.transparent;
   }
   
   public boolean isPathable()
   {
      return isLowPassable() || this instanceof Door;
   }

}