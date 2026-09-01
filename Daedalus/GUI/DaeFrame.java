package Daedalus.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;


public class DaeFrame extends JFrame implements ActionListener, ComponentListener
{
   private Vector<DaePanel> panelList;
   private TilePalette rectPalette;
   private TilePalette squarePalette;
   private DaePanel testPanel;
   
   public DaeFrame()
   {
      super();
      setSize(1200, 800);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle("Daedalus");
      setLayout(null);
      
      rectPalette = new TilePalette("Daedalus/res/img/WSFont_8x16.png", 16, 16);
      squarePalette = new TilePalette("Daedalus/res/img/WSFont_16x16.png", 16, 16);
      
      setVisible(true);
   }

   public void arrangePanels()
   {
   
   }
   
   public void actionPerformed(ActionEvent ae)
   {
   
   }
   
   public void componentHidden(ComponentEvent ce){}
   public void componentShown(ComponentEvent ce){}
   public void componentMoved(ComponentEvent ce){}
   public void componentResized(ComponentEvent ce)
   {
      arrangePanels();
   }
   
   
   public void main(String[] args)
   {
      DaeFrame frame = new DaeFrame();
   }
}