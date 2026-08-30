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
   
   private void updateImage(int x, int y)
   {
      imageArr[x][y] = palette.getTile(tileIndexArr[x][y], fgColorArr[x][y], bgColorArr[x][y]);
      dirtyArr[x][y] = false;
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
      
      int xInset = (this.getWidth() - unscaledImage.getWidth()) / 2;
      int yInset = (this.getHeight() - unscaledImage.getHeight()) / 2;
      g2d.drawImage(unscaledImage, xInset, yInset, null);
   }
   
   public boolean isInBounds(Coord loc){return isInBounds(loc.x, loc.y);}
   public boolean isInBounds(int x, int y)
   {
      if(x >= 0 && y >= 0 && x < tilesWide && y < tilesTall)
         return true;
      return false;
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