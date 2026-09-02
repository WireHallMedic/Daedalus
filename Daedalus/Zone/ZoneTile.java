package Daedalus.Zone;

import java.awt.*;
import Daedalus.GUI.*;

public class ZoneTile implements ZoneConstants, GUIConstants
{
	protected int fgColor;
	protected int bgColor;
	protected int tileIndex;
	protected boolean lowPassable;
	protected boolean highPassable;
	protected boolean transparent;


	public int getFGColor(){return fgColor;}
	public int getBGColor(){return bgColor;}
	public int getTileIndex(){return tileIndex;}
	public boolean isLowPassable(){return lowPassable;}
	public boolean isHighPassable(){return highPassable;}
	public boolean isTransparent(){return transparent;}


	public void setFGColor(int f){fgColor = f;}
	public void setBGColor(int b){bgColor = b;}
	public void setTileIndex(int t){tileIndex = t;}
	public void setLowPassable(boolean l){lowPassable = l;}
	public void setHighPassable(boolean h){highPassable = h;}
	public void setTransparent(boolean t){transparent = t;}

   public ZoneTile(TileBase base)
   {
      set(base, WHITE, BLACK);
   }
   
   public ZoneTile(ZoneTile that)
   {
      this.fgColor = that.fgColor;
      this.bgColor = that.bgColor;
      this.tileIndex = that.tileIndex;
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
      fgColor = fg;
      bgColor = bg;
      set(base);
   }
   
   public void set(TileBase base)
   {
      tileIndex = base.tileIndex;
      lowPassable = base.lowPassable;
      highPassable = base.highPassable;
      transparent = base.transparent;
   }
}