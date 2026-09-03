package Daedalus.GUI;

import WidlerSuite.WSFontConstants;
import java.awt.*;
import java.awt.image.*;
import Daedalus.Zone.ZoneTile;

public class ImageTile
{
	protected BufferedImage image;
	protected int fgColor;
	protected int bgColor;
	protected int tileIndex;
   protected int lowerTileIndex;
	protected boolean dirty;
	protected TilePalette palette;


	public int getFGColor(){return fgColor;}
	public int getBGColor(){return bgColor;}
	public int getTileIndex(){return tileIndex;}
   public int getLowerTileIndex(){return lowerTileIndex;}
	public boolean isDirty(){return dirty;}
	public TilePalette getPalette(){return palette;}


	public void setFGColor(int f){fgColor = f; dirty = true;}
	public void setBGColor(int b){bgColor = b; dirty = true;}
	public void setTileIndex(int t){tileIndex = t; dirty = true;}
   public void setLowerTileIndex(int t){lowerTileIndex = t; dirty = true;}   // setting this makes a stacked tile
	public void setDirty(boolean d){dirty = d;}
	public void setPalette(TilePalette p){palette = p;}

   public ImageTile(TilePalette p)
   {
      this(p, ' ', Color.WHITE.getRGB(), Color.BLACK.getRGB());
   }
   
   public ImageTile(TilePalette p, int index, int fg, int bg)
   {
      palette = p;
      tileIndex = index;
      lowerTileIndex = -1;
      fgColor = fg;
      bgColor = bg;
      dirty = true;
   }
   
   public ImageTile(ImageTile that)
   {
      this(that.palette);
      set(that);
   }
   
   public ImageTile copy()
   {
      return new ImageTile(this);
   }
   
   public void set(int index, int fg, int bg)
   {
      tileIndex = index;
      fgColor = fg;
      bgColor = bg;
      dirty = true;
   }
   
   public void set(ImageTile that)
   {
      this.palette = that.palette;
      this.tileIndex = that.tileIndex;
      this.lowerTileIndex = that.lowerTileIndex;
      this.fgColor = that.fgColor;
      this.bgColor = that.bgColor;
      this.dirty = true;
   }
   
   public void set(ZoneTile zoneTile)
   {
      this.tileIndex = zoneTile.getTileIndex();
      this.fgColor = zoneTile.getFGColor();
      this.bgColor = zoneTile.getBGColor();
      this.dirty = true;
   }
   
   public boolean isStackedTile()
   {
      return lowerTileIndex != -1;
   }
   
   
	public BufferedImage getImage()
   {
      if(isDirty())
         createImage();
      return image;
   }
   
   public void createImage()
   {
      if(isStackedTile())
      {
         BufferedImage lowerTile = palette.getTile(lowerTileIndex, bgColor, palette.TRANSPARENT);
         image = palette.layer(lowerTile, tileIndex, fgColor);
      }
      else
         image = palette.getTile(tileIndex, fgColor, bgColor);
      dirty = false;
   }
}