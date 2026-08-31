package Daedalus.GUI;

import WidlerSuite.WSFontConstants;
import java.awt.*;
import java.awt.image.*;

public class ImageTile
{
	protected BufferedImage image;
	protected int fgColor;
	protected int bgColor;
	protected int tileIndex;
	protected boolean dirty;
	protected TilePalette palette;


	public int getFGColor(){return fgColor;}
	public int getBGColor(){return bgColor;}
	public int getTileIndex(){return tileIndex;}
	public boolean isDirty(){return dirty;}
	public TilePalette getPalette(){return palette;}


	public void setImage(BufferedImage i){image = i;}
	public void setFGColor(int f){fgColor = f;}
	public void setBGColor(int b){bgColor = b;}
	public void setTileIndex(int t){tileIndex = t;}
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
      fgColor = fg;
      bgColor = bg;
      dirty = true;
   }
   
	public BufferedImage getImage()
   {
      if(isDirty())
         createImage();
      return image;
   }
   
   public void createImage()
   {
      image = palette.getTile(tileIndex, fgColor, bgColor);
      dirty = false;
   }
}