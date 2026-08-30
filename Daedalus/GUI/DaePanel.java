package Daedalus.GUI;

import javax.swing.*;
import WidlerSuite.*;
import java.awt.*;
import java.awt.event.*;

public class DaePanel extends RogueTilePanel implements ComponentListener
{
   
   
   public DaePanel(int tiles_wide, int tiles_tall, TilePalette palette)
   {
      super(tiles_wide, tiles_tall, palette);
   }
   
   public void componentResized(ComponentEvent ce)
   {
      double baseWidth = columns() * getPalette().getTileWidth();
      double baseHeight = rows() * getPalette().getTileHeight();
      // try width-dominant scaling first
      double scaling = this.getWidth() / baseWidth;
      if(this.getHeight() > baseHeight * scaling)
         scaling = this.getHeight() / baseHeight;
      setSizeMultiplier(scaling);
      super.componentResized(ce);
      this.repaint();
   }
   
   public void actionPerformed(ActionEvent ae)
   {
      char newChar = (char)('A' + (int)(Math.random() * 26));
      int x = (int)(Math.random() * columns());
      int y = (int)(Math.random() * rows());
      setIcon(x, y, newChar);
      super.actionPerformed(ae);
   }
   
   public static void main(String[] args)
   {
      JFrame frame = new JFrame();
      frame.setSize(1200, 600);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setTitle("DaePanel");
      TilePalette palette = new TilePalette("Daedalus/res/img/WSFont_8x16.png", 16, 16);
      
      DaePanel panel = new DaePanel(120, 40, palette);
      panel.setAll('.', Color.WHITE.getRGB(), Color.BLACK.getRGB());
      frame.add(panel);
      frame.addComponentListener(panel);
      
      frame.setVisible(true);
      
      javax.swing.Timer timer = new javax.swing.Timer(1000 / 60, null);
      timer.addActionListener(panel);
      timer.start();
   }
}