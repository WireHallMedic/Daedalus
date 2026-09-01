package Daedalus.Zone;

import java.awt.*;
import Daedalus.GUI.*;

public class ZoneTile implements ZoneConstants, GUIConstants
{
	protected int fgColor;
	protected int bgColor;
	protected int tileIndex;
	private boolean lowPassable;
	private boolean highPassable;
	private boolean transparent;


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
      fgColor = WHITE;
      bgColor = BLACK;
      tileIndex = base.tileIndex;
      lowPassable = base.lowPassable;
      highPassable = base.highPassable;
      transparent = base.transparent;
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
   
   public ZoneTile copy(ZoneTile that)
   {
      return new ZoneTile(that);
   }
}