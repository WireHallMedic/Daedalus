package Daedalus.GUI;

import WidlerSuite.WSFontConstants;
import java.awt.*;
import java.awt.image.*;

public class UnboundTile extends ImageTile
{
   private double xOffset;
	private double yOffset;
	private double scale;


	public double getXOffset(){return xOffset;}
	public double getYOffset(){return yOffset;}
	public double getScale(){return scale;}


	public void setXOffset(double x){xOffset = x;}
	public void setYOffset(double y){yOffset = y;}
	public void setScale(double s){scale = s;}

   public UnboundTile(TilePalette p)
   {
      this(p, ' ', Color.WHITE.getRGB(), Color.BLACK.getRGB());
   }
   
   public UnboundTile(TilePalette p, int index, int fg, int bg)
   {
      super(p, index, fg, bg);
      xOffset = 0.0;
      yOffset = 0.0;
      scale = 1.0;
   }
}