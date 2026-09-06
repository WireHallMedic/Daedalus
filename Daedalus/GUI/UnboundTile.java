package Daedalus.GUI;

import WidlerSuite.WSFontConstants;
import WidlerSuite.Coord;
import java.awt.*;
import java.awt.image.*;

public class UnboundTile extends ImageTile
{
	protected double xOffset;
	protected double yOffset;
	protected double nonTrackingXOffset;
	protected double nonTrackingYOffset;
	protected double scale;
	protected Coord tileLoc;
   protected boolean expired;


	public double getXOffset(){return xOffset;}
	public double getYOffset(){return yOffset;}
	public double getNonTrackingXOffset(){return nonTrackingXOffset;}
	public double getNonTrackingYOffset(){return nonTrackingYOffset;}
	public double getScale(){return scale;}
	public Coord getTileLoc(){return new Coord(tileLoc);}
   public boolean isExpired(){return expired;}


	public void setXOffset(double x){xOffset = x;}
	public void setYOffset(double y){yOffset = y;}
	public void setNonTrackingXOffset(double x){nonTrackingXOffset = x;}
	public void setNonTrackingYOffset(double y){nonTrackingYOffset = y;}
	public void setScale(double s){scale = s; dirty = true;}
	public void setTileLoc(Coord t){setTileLoc(t.x, t.y);}
	public void setTileLoc(int x, int y){tileLoc = new Coord(x, y);}
   public void setExpired(boolean ex){expired = ex;}


   public UnboundTile(TilePalette p)
   {
      this(p, ' ', Color.WHITE.getRGB(), Color.BLACK.getRGB());
   }
   
   public UnboundTile(TilePalette p, int index, int fg, int bg)
   {
      super(p, index, fg, bg);
      tileLoc = new Coord(-1, -1);
      xOffset = 0.0;
      yOffset = 0.0;
      nonTrackingXOffset = 0.0;
      nonTrackingYOffset = 0.0;
      scale = 1.0;
      expired = false;
   }
   
   public UnboundTile(UnboundTile that)
   {
      super(that);
      set(that);
   }
   
   public UnboundTile copy()
   {
      return new UnboundTile(this);
   }
   
   public void set(UnboundTile that)
   {
      super.set(that);
      this.tileLoc = that.tileLoc.copy();
      this.xOffset = that.xOffset;
      this.yOffset = that.yOffset;
      this.nonTrackingXOffset = that.nonTrackingXOffset;
      this.nonTrackingYOffset = that.nonTrackingYOffset;
      this.scale = that.scale;
      this.expired = that.expired;
      this.dirty = true;
   }
   
   public void adjustTileLoc(int x, int y)
   {
      tileLoc.x += x;
      tileLoc.y += y;
   }
   public void adjustTileLoc(Coord c){adjustTileLoc(c.x, c.y);}
   
   @Override
   public void createImage()
   {
      super.createImage();
      int newWidth = (int)(palette.getTileWidth() * scale);
      int newHeight = (int)(palette.getTileHeight() * scale);
      BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
      scaledImage.getGraphics().drawImage(image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
      image = scaledImage;
   }
   
   public void drawToImage(Graphics2D g2d, TilePalette panelPalette, int offsetTilesX, int offsetTilesY)
   {
      // set base location
      int xInset = (tileLoc.x - offsetTilesX) * panelPalette.getTileWidth();
      int yInset = (tileLoc.y - offsetTilesY) * panelPalette.getTileHeight();
      // adjust for offset
      xInset += (int)((xOffset + nonTrackingXOffset) * panelPalette.getTileWidth());
      yInset += (int)((yOffset + nonTrackingYOffset) * panelPalette.getTileHeight());
      // adjust for size
      xInset += (panelPalette.getTileWidth() - getImage().getWidth()) / 2;
      yInset += (panelPalette.getTileHeight() - getImage().getHeight()) / 2;
      g2d.drawImage(getImage(), xInset, yInset, null);
   }
   public void drawToImage(Graphics2D g2d, TilePalette panelPalette){drawToImage(g2d, panelPalette, 0, 0);}
   public void drawToImage(Graphics2D g2d, TilePalette panelPalette, Coord tileOffset){drawToImage(g2d, panelPalette, tileOffset.x, tileOffset.y);}
}

