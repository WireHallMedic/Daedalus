package Daedalus.GUI;

import WidlerSuite.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;


public class DaePanel extends JPanel implements ActionListener
{
   
	private Daedalus.GUI.TilePalette palette;
	private ImageTile[][] imageTileArr;
	private int tilesWide;
	private int tilesTall;
   private Vector<UnboundTile> unboundTileList;
   
   public Vector<UnboundTile> getUnboundTileList(){return unboundTileList;}
   
   public DaePanel(int columns, int rows, Daedalus.GUI.TilePalette tilePalette)
   {  
      super();
      tilesWide = columns;
      tilesTall = rows;
      palette = tilePalette;
      imageTileArr = new ImageTile[tilesWide][tilesTall];
      unboundTileList = new Vector<UnboundTile>();
      setAll('.', Color.WHITE.getRGB(), Color.BLACK.getRGB());
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
   
   public void paint(Graphics g)
   {
      super.paint(g);
      Graphics2D g2d = (Graphics2D)g;
      
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
      
      // scale up, center, and draw
      double scaling = getScaling();
      Image scaledImage = unscaledImage.getScaledInstance((int)(unscaledImage.getWidth() * scaling),
                                 (int)(unscaledImage.getHeight() * scaling), Image.SCALE_SMOOTH);
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
      ut.setScale(2.0);
      ut.setTileLoc(1, 1);
      panel.addUnboundTile(ut);
      ut.setExpired(true);
      panel.cleanUnboundTileList();
      
      panel.repaint();
   }
}