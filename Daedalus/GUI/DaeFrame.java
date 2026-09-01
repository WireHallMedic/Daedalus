package Daedalus.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;


public class DaeFrame extends JFrame implements ActionListener, ComponentListener, GUIConstants
{
   private Vector<DaePanel> panelList;
   private DaePanel curPanel;
   private TilePalette rectPalette;
   private TilePalette squarePalette;
   private JPanel innerPanel;
   private MainGamePanel mainGamePanel;
   
   public DaeFrame()
   {
      super();
      setSize(1180, 820);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle("Daedalus");
      setLayout(new GridLayout(1, 1));
      
      rectPalette = new TilePalette("Daedalus/res/img/WSFont_8x16.png", 16, 16);
      squarePalette = new TilePalette("Daedalus/res/img/WSFont_16x16.png", 16, 16);
      javax.swing.Timer timer = new javax.swing.Timer(1000 / FRAMES_PER_SECOND, this);
      
      addComponentListener(this);
      
      // panels
      panelList = new Vector<DaePanel>();
      innerPanel = new JPanel();
      innerPanel.setLayout(null);
      innerPanel.setBackground(new Color(BLACK));
      innerPanel.setVisible(true);
      this.add(innerPanel);
      
      mainGamePanel = new MainGamePanel(rectPalette, squarePalette);
      innerPanel.add(mainGamePanel);
      panelList.add(mainGamePanel);
      
      arrangePanels();
      curPanel = mainGamePanel;
      curPanel.setVisible(true);
      setVisible(true);
      timer.start();
   }

   public void arrangePanels()
   {
      for(DaePanel p: panelList)
      {
         p.setSize(innerPanel.getSize());
         p.setLocation(0, 0);
      }
   }
   
   public void actionPerformed(ActionEvent ae)
   {
      curPanel.actionPerformed(ae);
   }
   
   public void componentHidden(ComponentEvent ce){}
   public void componentShown(ComponentEvent ce){arrangePanels();}
   public void componentMoved(ComponentEvent ce){}
   public void componentResized(ComponentEvent ce)
   {
      arrangePanels();
   }
   
}