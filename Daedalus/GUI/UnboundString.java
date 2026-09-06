package Daedalus.GUI;

import java.awt.*;
import java.awt.image.*;

public class UnboundString extends UnboundTile implements GUIConstants
{
   protected String str;
   private ImageTile[] tileArr;
   
   public String getString(){return str;}
   
   public UnboundString(String s)
   {
      super(RECT_PALETTE, 'X', WHITE, TRANSPARENT);
      tileArr = null;
      setString(s);
   }
   
   public void setString(String s)
   {
      str = s;
      tileArr = new ImageTile[str.length()];
      for(int i = 0; i < str.length(); i++)
      {
         tileArr[i] = new ImageTile(palette, str.charAt(i), fgColor, bgColor);
      }
      dirty = true;
   }
   
   @Override
	public void setFGColor(int f)
   {
      super.setFGColor(f);
      for(int i = 0; i < str.length(); i++)
      {
         tileArr[i].setFGColor(f);
      }
   }
   
   @Override
	public void setBGColor(int b)
   {
      super.setBGColor(b);
      for(int i = 0; i < str.length(); i++)
      {
         tileArr[i].setBGColor(b);
      }
   }
   
   @Override
	public void setTileIndex(int t)
   {
      super.setTileIndex(t);
      for(int i = 0; i < str.length(); i++)
      {
         tileArr[i].setTileIndex(t);
      }
   }
   
   @Override
   public void createImage()
   {
      BufferedImage newImg = new BufferedImage(palette.getTileWidth() * str.length(), palette.getTileHeight(), 
                                                BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = (Graphics2D)(newImg.getGraphics());
      for(int i = 0; i < str.length(); i++)
      {
         g2d.drawImage(tileArr[i].getImage(), i * palette.getTileWidth(), 0, null);
      }
      image = newImg;
      dirty = false;
   }
}