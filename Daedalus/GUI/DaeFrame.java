package Daedalus.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import Daedalus.Engine.*;


public class DaeFrame extends JFrame implements ActionListener, ComponentListener, GUIConstants, KeyListener, Runnable
{
   private Vector<DaePanel> panelList;
   private DaePanel curPanel;
   private JPanel innerPanel;
   private MainGamePanel mainGamePanel;
   private InventoryPanel inventoryPanel;
   private static Class pendingPanelClass = null;
   
   
   public DaeFrame()
   {
      super();
      setSize(1180, 820);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle("Daedalus");
      setLayout(new GridLayout(1, 1));
      
      addComponentListener(this);
      this.addKeyListener(this);
      
      // panels
      panelList = new Vector<DaePanel>();
      innerPanel = new JPanel();
      innerPanel.setLayout(null);
      innerPanel.setBackground(new Color(BLACK));
      //innerPanel.setFocusable(true);
      this.setFocusTraversalKeysEnabled(false);
      this.add(innerPanel);
      
      mainGamePanel = new MainGamePanel(RECT_PALETTE, SQUARE_PALETTE);
      innerPanel.add(mainGamePanel);
      panelList.add(mainGamePanel);
      
      inventoryPanel = new InventoryPanel();
      innerPanel.add(inventoryPanel);
      panelList.add(inventoryPanel);
      
      arrangePanels();
      curPanel = mainGamePanel;
      curPanel.setVisible(true);
      setVisible(true);
      toFront();
      
      // needs to happen after everything is set up
      //SwingUtilities.invokeLater(() -> innerPanel.requestFocusInWindow());
      new Thread(this).start();
      
   }
   
   public static void setActivePanel(Class panelClass)
   {
      pendingPanelClass = panelClass;
   }
   
   private void activeatePanel()
   {
      for(DaePanel p: panelList)
      {
         if(p.getClass() == pendingPanelClass)
         {
            p.setVisible(true);
            curPanel = p;
         }
         else
         {
            p.setVisible(false);
         }
      }
      pendingPanelClass = null;
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
      if(pendingPanelClass != null)
         activeatePanel();
      curPanel.actionPerformed(ae);
   }
   
   public void componentHidden(ComponentEvent ce){}
   public void componentShown(ComponentEvent ce){arrangePanels();}
   public void componentMoved(ComponentEvent ce){}
   public void componentResized(ComponentEvent ce)
   {
      arrangePanels();
   }
   
   public void keyPressed(KeyEvent ke){curPanel.keyPressed(ke);}
   public void keyReleased(KeyEvent ke){curPanel.keyReleased(ke);}
   public void keyTyped(KeyEvent ke){curPanel.keyTyped(ke);}
   
   // animation loop
   public void run()
   {
      int millisPerLoop = 1000 / FRAMES_PER_SECOND;
      long lastLoop = System.currentTimeMillis();
      while(true)
      {
         if(System.currentTimeMillis() - lastLoop >= millisPerLoop)
         {
            lastLoop = System.currentTimeMillis();
            AnimationManager.update();
            this.actionPerformed(new ActionEvent(this, 1, "Timer kick"));
         }
         Thread.yield();
      }
   
   }
   
}