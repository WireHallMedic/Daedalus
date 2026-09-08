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
   
   public void setFGColor(int f)
   {
      if(f != fgColor)
         dirty = true;
      fgColor = f; 
   }
   
	public void setBGColor(int b)
   {
      if(b != bgColor)
         dirty = true;
      bgColor = b; 
   }
   
	public void setTileIndex(int t)
   {
      if(t != tileIndex)
         dirty = true;
      tileIndex = t; 
   }
   
   public void setLowerTileIndex(int t)   // setting this to not -1 makes a stacked tile
   {
      if(t != lowerTileIndex)
         dirty = true;
      lowerTileIndex = t; 
   }
   
   
   public ImageTile copy()
   {
      return new ImageTile(this);
   }
   
   public void set(int index, int fg, int bg, int lti)
   {
      setTileIndex(index);
      setFGColor(fg);
      setBGColor(bg);
      setLowerTileIndex(lti);
   }
   public void set(int index, int fg, int bg){set(index, fg, bg, -1);}
   
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
      setTileIndex(zoneTile.getTileIndex());
      setFGColor(zoneTile.getFGColor());
      setBGColor(zoneTile.getBGColor());
      setLowerTileIndex(-1);
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