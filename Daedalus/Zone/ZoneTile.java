package Daedalus.Zone;

import java.awt.*;
import Daedalus.GUI.*;

public class ZoneTile implements ZoneConstants, GUIConstants
{
	private ImageTile imageTile;
	private boolean lowPassable;
	private boolean highPassable;
	private boolean transparent;


	public ImageTile getImageTile(){return imageTile;}
	public boolean isLowPassable(){return lowPassable;}
	public boolean isHighPassable(){return highPassable;}
	public boolean isTransparent(){return transparent;}


	public void setImageTile(ImageTile i){imageTile = i;}
	public void setLowPassable(boolean l){lowPassable = l;}
	public void setHighPassable(boolean h){highPassable = h;}
	public void setTransparent(boolean t){transparent = t;}

   public ZoneTile(TileBase base, TilePalette palette)
   {
      imageTile = new ImageTile(palette, base.tileIndex, WHITE, BLACK);
      lowPassable = base.lowPassable;
      highPassable = base.highPassable;
      transparent = base.transparent;
   }
   
   public ZoneTile(ZoneTile that)
   {
      this.imageTile = that.imageTile.copy();
      this.lowPassable = that.lowPassable;
      this.highPassable = that.highPassable;
      this.transparent = that.transparent;
   }
   
   public ZoneTile copy(ZoneTile that)
   {
      return new ZoneTile(that);
   }
}