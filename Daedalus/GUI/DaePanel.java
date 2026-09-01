package Daedalus.GUI;

import WidlerSuite.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;


public class DaePanel extends JPanel implements ActionListener, GUIConstants
{
   
	protected Daedalus.GUI.TilePalette palette;
	protected ImageTile[][] imageTileArr;
	protected int tilesWide;
	protected int tilesTall;
   protected Vector<UnboundTile> unboundTileList;
   protected static int scaleStyle = Image.SCALE_SMOOTH;
   
   public static int getScaleStyle(){return scaleStyle;}
   
   public static void setScaleStyle(int ss){scaleStyle = ss;}
   
   public Vector<UnboundTile> getUnboundTileList(){return unboundTileList;}
   
   public DaePanel(int columns, int rows, Daedalus.GUI.TilePalette tilePalette)
   {  
      super();
      tilesWide = columns;
      tilesTall = rows;
      palette = tilePalette;
      imageTileArr = new ImageTile[tilesWide][tilesTall];
      unboundTileList = new Vector<UnboundTile>();
      setAll('.', WHITE, BLACK);
      setBackground(BLACK);
   }
   
   public void setAll(int tileIndex, int fgColor, int bgColor)
   {
      for(int x = 0; x < tilesWide; x++)
      for(int y = 0; y < tilesTall; y++)
      {
         imageTileArr[x][y] = new ImageTile(palette, tileIndex, fgColor, bgColor);
      }
   }
   
   public void clearUnboundTileList()
   {
      unboundTileList = new Vector<UnboundTile>();
   }
   
   public void addUnboundTile(UnboundTile ut)
   {
      unboundTileList.add(ut);
   }
   
   public boolean isInBounds(Coord loc){return isInBounds(loc.x, loc.y);}
   public boolean isInBounds(int x, int y)
   {
      if(x >= 0 && y >= 0 && x < tilesWide && y < tilesTall)
         return true;
      return false;
   } 
   
   public boolean isMostlyInBounds(Coord loc){return isMostlyInBounds(loc.x, loc.y);}
   public boolean isMostlyInBounds(int x, int y)
   {
      if(x >= -1 && y >= -1 && x <= tilesWide && y <= tilesTall)
         return true;
      return false;
   } 
   
   // set panel background (only seen outside of tile array)
   public void setBackground(int panelBGColor)
   {
      super.setBackground(new Color(panelBGColor));
   }

   
   // getters
   //////////////////////////////////////////////////////////////////
   
   // get foreground color
   public int getFGColor(Coord c){return getFGColor(c.x, c.y);}
   public int getFGColor(int x, int y)
   {
      if(!isInBounds(x, y))
         return -1;
      return imageTileArr[x][y].getFGColor();
   }
   
   // get background color
   public int getBGColor(Coord c){return getBGColor(c.x, c.y);}
   public int getBGColor(int x, int y)
   {
      if(!isInBounds(x, y))
         return -1;
      return imageTileArr[x][y].getBGColor();
   }
   
   // get icon index
   public int getTileIndex(Coord c){return getTileIndex(c.x, c.y);}
   public int getTileIndex(int x, int y)
   {
      if(!isInBounds(x, y))
         return -1;
      return imageTileArr[x][y].getTileIndex();
   }
   
      
   // setters
   //////////////////////////////////////////////////////////////////
   
   // set all values of a tile location, using RGB values
   public void setTile(Coord c, int tileIndex, int fg, int bg){setTile(c.x, c.y, tileIndex, fg, bg);}
   public void setTile(int x, int y, int tileIndex, int fg, int bg)
   {
      if(!isInBounds(x, y))
         return;
      imageTileArr[x][y].setFGColor(fg);
      imageTileArr[x][y].setBGColor(bg);
      imageTileArr[x][y].setTileIndex(tileIndex);
   }
   
   // set all values of a tile location, using colors
   public void setTile(Coord c, int tileIndex, Color fg, Color bg){setTile(c.x, c.y, tileIndex, fg, bg);}
   public void setTile(int x, int y, int tileIndex, Color fg, Color bg)
   {
      setTile(x, y, tileIndex, fg.getRGB(), bg.getRGB());
   }
   
   // set foreground color of a tile
   public void setFGColor(Coord c, int fg){setFGColor(c.x, c.y, fg);}
   public void setFGColor(int x, int y, int fg)
   {
      if(!isInBounds(x, y))
         return;
      imageTileArr[x][y].setFGColor(fg);
   }
   
   // set background color of a tile
   public void setBGColor(Coord c, int bg){setBGColor(c.x, c.y, bg);}
   public void setBGColor(int x, int y, int bg)
   {
      if(!isInBounds(x, y))
         return;
      imageTileArr[x][y].setBGColor(bg);
   }
   
   // set tile index of a tile
   public void setTileIndex(Coord c, int tileIndex){setTileIndex(c.x, c.y, tileIndex);}
   public void setTileIndex(int x, int y, int tileIndex)
   {
      if(!isInBounds(x, y))
         return;
      imageTileArr[x][y].setTileIndex(tileIndex);
   }
   
   // return the multiplier to get image to full frame (while maintaining width/height ratio)
   private double getScaling()
   {
      double scale = (double)this.getWidth() / (palette.getTileWidth() * tilesWide);
      if((int)(palette.getTileHeight() * tilesTall * scale) > this.getHeight())
         scale = (double)this.getHeight() / (palette.getTileHeight() * tilesTall);
      return scale;
   }
   
   private void cleanUnboundTileList()
   {
      for(int i = 0; i < unboundTileList.size(); i++)
      {
         if(unboundTileList.elementAt(i).isExpired())
         {
            unboundTileList.removeElementAt(i);
            i--;
         }
      }
   }
   
   public BufferedImage getUnscaledImage()
   {
      // set the unscaled image
      int xStep = palette.getTileWidth();
      int yStep = palette.getTileHeight();
      BufferedImage unscaledImage = new BufferedImage(xStep * tilesWide, yStep * tilesTall, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2dUnscaled = (Graphics2D)(unscaledImage.getGraphics());
      for(int x = 0; x < tilesWide; x++)
      for(int y = 0; y < tilesTall; y++)
      {
         g2dUnscaled.drawImage(imageTileArr[x][y].getImage(), xStep * x, yStep * y, null);
      }
      
      // draw unbound tiles to unscaled image
      for(UnboundTile ut: unboundTileList)
         ut.drawToImage(g2dUnscaled, this);
      
      return unscaledImage;
   }
   
   public void paint(Graphics g)
   {
      super.paint(g);
      Graphics2D g2d = (Graphics2D)g;
      BufferedImage unscaledImage = getUnscaledImage();
      
      // scale up, center, and draw
      double scaling = getScaling();
      Image scaledImage = unscaledImage.getScaledInstance((int)(unscaledImage.getWidth() * scaling),
                                 (int)(unscaledImage.getHeight() * scaling), scaleStyle);
      int xInset = (this.getWidth() - scaledImage.getWidth(null)) / 2;
      int yInset = (this.getHeight() - scaledImage.getHeight(null)) / 2;
      g2d.drawImage(scaledImage, xInset, yInset, null);
   }

   // kicked by timer
   public void actionPerformed(ActionEvent ae)
   {
      cleanUnboundTileList();
      this.repaint();
   }
   
   // write the string in a box, with the passed foreground and background colors
   public void write(Coord loc, String s, int fgColor, int bgColor, Coord box){write(loc.x, loc.y, s, fgColor, bgColor, box.x, box.y);}
   public void write(int x, int y, String s, int fgColor, int bgColor, int w, int h)
   {
      int xLoc = 0; 
      int yLoc = 0;
      char[][] charArr = new char[w][h];
      String[] stringArr = s.split(" ");
      Vector<String> stringVect = new Vector<String>();
      // initialize array just in case tile 0x00 isn't blank
      for(int xx = 0; xx < w; xx++)
      for(int yy = 0; yy < h; yy++)
         charArr[xx][yy] = ' ';
      // copy over to vector, breaking up words that are too long
      for(String str : stringArr)
      {
         if(str.length() <= w)
            stringVect.add(str);
         else
         {
            while(str.length() > 0)
            {
               int cutPoint = Math.min(str.length(), w);
               stringVect.add(str.substring(0, cutPoint));
               str = str.substring(cutPoint);
            }
         }
      }
      // copy the characters to the character array
      for(String str : stringVect)
      {
         // carriage return if needed
         if(w - xLoc < str.length())
         {
            xLoc = 0;
            yLoc++;
            if(yLoc >= h)
               break;
         }
         // set the characters
         for(int j = 0; j < str.length(); j++)
         {
            if(str.charAt(j) == '\n')
            {
               xLoc = 0;
               yLoc++;
            }
            else
            {
               charArr[xLoc][yLoc] = str.charAt(j);
               xLoc++;
            }
         }
         // space
         xLoc++;
      }
      // actually set the tiles
      for(int xx = 0; xx < w; xx++)
      for(int yy = 0; yy < h; yy++)
      {
         setTile(x + xx, y + yy, charArr[xx][yy], fgColor, bgColor);
      }
   }

    
   public static void main(String[] args)
   {
      JFrame frame = new JFrame();
      frame.setSize(1200, 800);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setTitle("DaePanel");
      TilePalette palette = new TilePalette("Daedalus/res/img/WSFont_8x16.png", 16, 16);
      
      DaePanel panel = new DaePanel(120, 40, palette);
      frame.add(panel);
      
      frame.setVisible(true);
      
      UnboundTile ut = new UnboundTile(palette, 'X', Color.BLACK.getRGB(), Color.WHITE.getRGB());
      ut.setLowerTileIndex(2);
      ut.setScale(2.0);
      ut.setTileLoc(1, 1);
      panel.addUnboundTile(ut);
      
      String str = "Sphinx of black quartz, judge my vow!"; 
      panel.write(5, 5, str, Color.CYAN.getRGB(), new Color(64, 64, 64).getRGB(), 10, 10);
      
      panel.repaint();
   }
}