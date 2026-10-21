package Daedalus.Zone;

import Daedalus.GUI.*;
import Daedalus.Engine.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;


public class ZoneViewer extends DaePanel implements MouseListener, GUIConstants, ZoneConstants
{
   private ZoneMap zoneMap;
   
   public ZoneViewer()
   {
      super(75, 50, SQUARE_PALETTE);
      generateMap();
      addMouseListener(this);
   }
   
   public void update()
   {
      for(int x = 0; x < tilesWide; x++)
      for(int y = 0; y < tilesTall; y++)
      {
         if(zoneMap != null &&
            x < zoneMap.getWidth() &&
            y < zoneMap.getHeight())
         {
            setTileIndex(x, y, zoneMap.getTile(x, y).getTileIndex());
            setFGColor(x, y, zoneMap.getTile(x, y).getFGColor());
            setBGColor(x, y, zoneMap.getTile(x, y).getBGColor());
         }
         else
         {
            setTileIndex(x, y, ' ');
            setFGColor(x, y, WHITE);
            setBGColor(x, y, BLACK);
         }
      }
   }
   
   public void printToConsole()
   {
      System.out.println();
      for(int y = 0; y < zoneMap.getHeight(); y++)
      {
         for(int x = 0; x < zoneMap.getWidth(); x++)
         {
            System.out.print((char)zoneMap.getTile(x, y).getTileIndex() + "");
         }
         System.out.println();
      }
   }
   
   private void generateMap()
   {
//       zoneMap = WastelandMapFactory.getCrashedShipMap(1, BITMASK_EAST + BITMASK_WEST + BITMASK_SOUTH);
//       for(int y = 0; y < zoneMap.getHeight(); y++)
//       {
//          if(zoneMap.getTile(0, y) instanceof Exit)
//             MapFactory.dropSign(zoneMap, 0, y, "Test Sign");
//       }
      zoneMap = BSPMapFactory.getCatacombMap(60, 40, 5, 15, .25);
      update();
      this.repaint();
   }
   
   public void mousePressed(MouseEvent me){}
   public void mouseReleased(MouseEvent me){}
   public void mouseEntered(MouseEvent me){}
   public void mouseExited(MouseEvent me){}
   public void mouseClicked(MouseEvent me)
   {
      //RNG.setSeed(1792253081619L);
      generateMap();
      //printToConsole();
   }
   
   public static void main(String[] args)
   {
      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(1000, 800);
      ZoneViewer zv = new ZoneViewer();
      frame.add(zv);
      frame.setVisible(true);
   }
}