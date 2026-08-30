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
	private BufferedImage[][] imageArr;
   private int[][] fgColorArr;
   private int[][] bgColorArr;
   private int[][] tileIndexArr;
   private boolean[][] dirtyArr;
	private int tilesWide;
	private int tilesTall;
   
   public DaePanel(int columns, int rows, Daedalus.GUI.TilePalette tilePalette)
   {  
      super();
      tilesWide = columns;
      tilesTall = rows;
      palette = tilePalette;
      imageArr = new BufferedImage[tilesWide][tilesTall];
      fgColorArr = new int[tilesWide][tilesTall];
      bgColorArr = new int[tilesWide][tilesTall];
      tileIndexArr = new int[tilesWide][tilesTall];
      dirtyArr = new boolean[tilesWide][tilesTall];
      setAll('.', Color.WHITE.getRGB(), Color.BLACK.getRGB());
   }
   
   public void setAll(int tileIndex, int fgColor, int bgColor)
   {
      for(int x = 0; x < tilesWide; x++)
      for(int y = 0; y < tilesTall; y++)
      {
         tileIndexArr[x][y] = tileIndex;
         fgColorArr[x][y] = fgColor;
         bgColorArr[x][y] = bgColor;
         updateImage(x, y);
      }
   }
   
   public boolean isInBounds(Coord loc){return isInBounds(loc.x, loc.y);}
   public boolean isInBounds(int x, int y)
   {
      if(x >= 0 && y >= 0 && x < tilesWide && y < tilesTall)
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
      return fgColorArr[x][y];
   }
   
   // get background color
   public int getBGColor(Coord c){return getBGColor(c.x, c.y);}
   public int getBGColor(int x, int y)
   {
      if(!isInBounds(x, y))
         return -1;
      return bgColorArr[x][y];
   }
   
   // get icon index
   public int getIcon(Coord c){return getIcon(c.x, c.y);}
   public int getIcon(int x, int y)
   {
      if(!isInBounds(x, y))
         return -1;
      return tileIndexArr[x][y];
   }
   
      
   // setters
   //////////////////////////////////////////////////////////////////
   
   // set all values of a tile location, using RGB values
   public void setTile(Coord c, int tileIndex, int fg, int bg){setTile(c.x, c.y, tileIndex, fg, bg);}
   public void setTile(int x, int y, int tileIndex, int fg, int bg)
   {
      if(!isInBounds(x, y))
         return;
      fgColorArr[x][y] = fg;
      bgColorArr[x][y] = bg;
      tileIndexArr[x][y] = tileIndex;
      dirtyArr[x][y] = true;
   }
   
   // set all values of a tile location, using colors
   public void setTile(Coord c, int tileIndex, Color fg, Color bg){setTile(c.x, c.y, tileIndex, fg, bg);}
   public void setTile(int x, int y, int tileIndex, Color fg, Color bg)
   {
      setTile(x, y, tileIndex, fg.getRGB(), bg.getRGB());
      dirtyArr[x][y] = true;
   }
   
   // set foreground color of a tile
   public void setFGColor(Coord c, int fg){setFGColor(c.x, c.y, fg);}
   public void setFGColor(int x, int y, int fg)
   {
      if(!isInBounds(x, y))
         return;
      fgColorArr[x][y] = fg;
      dirtyArr[x][y] = true;
   }
   
   // set background color of a tile
   public void setBGColor(Coord c, int bg){setBGColor(c.x, c.y, bg);}
   public void setBGColor(int x, int y, int bg)
   {
      if(!isInBounds(x, y))
         return;
      bgColorArr[x][y] = bg;
      dirtyArr[x][y] = true;
   }
   
   // set icon of a tile
   public void setIcon(Coord c, int tileIndex){setIcon(c.x, c.y, tileIndex);}
   public void setIcon(int x, int y, int tileIndex)
   {
      if(!isInBounds(x, y))
         return;
      tileIndexArr[x][y] = tileIndex;
      dirtyArr[x][y] = true;
   }


   
   private void updateImage(int x, int y)
   {
      imageArr[x][y] = palette.getTile(tileIndexArr[x][y], fgColorArr[x][y], bgColorArr[x][y]);
      dirtyArr[x][y] = false;
   }
   
   private double getScaling()
   {
      double scale = (double)this.getWidth() / (imageArr[0][0].getWidth() * tilesWide);
      if((int)(imageArr[0][0].getHeight() * tilesTall * scale) > this.getHeight())
         scale = (double)this.getHeight() / (imageArr[0][0].getHeight() * tilesTall);
      return scale;
   }
   
   public void paint(Graphics g)
   {
      super.paint(g);
      Graphics2D g2d = (Graphics2D)g;
      
      int xStep = imageArr[0][0].getWidth();
      int yStep = imageArr[0][0].getHeight();
      BufferedImage unscaledImage = new BufferedImage(xStep * tilesWide, yStep * tilesTall, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2dUnscaled = (Graphics2D)(unscaledImage.getGraphics());
      for(int x = 0; x < tilesWide; x++)
      for(int y = 0; y < tilesTall; y++)
      {
         g2dUnscaled.drawImage(imageArr[x][y], xStep * x, yStep * y, null);
      }
      
      double scaling = getScaling();
      Image scaledImage = unscaledImage.getScaledInstance((int)(unscaledImage.getWidth() * scaling),
                                 (int)(unscaledImage.getHeight() * scaling), Image.SCALE_SMOOTH);
      int xInset = (this.getWidth() - scaledImage.getWidth(null)) / 2;
      int yInset = (this.getHeight() - scaledImage.getHeight(null)) / 2;
      System.out.println(scaling + ", " + scaledImage.getWidth(null) + ", " + scaledImage.getHeight(null));
      g2d.drawImage(scaledImage, xInset, yInset, null);
   }

   public void actionPerformed(ActionEvent ae)
   {
//       char newChar = (char)('A' + (int)(Math.random() * 26));
//       int x = (int)(Math.random() * columns());
//       int y = (int)(Math.random() * rows());
//       setIcon(x, y, newChar);
//       super.actionPerformed(ae);
   }
//    
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
      
      panel.repaint();
   }
}