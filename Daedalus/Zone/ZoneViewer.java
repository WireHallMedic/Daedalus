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
   private boolean showKeyTraversalTiles;
   
   public ZoneViewer(boolean sktt)
   {
      super(75, 50, SQUARE_PALETTE);
      showKeyTraversalTiles = sktt;
      generateMap();
      addMouseListener(this);
   }
   public ZoneViewer(){this(false);}
   
   
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
            if(showKeyTraversalTiles && zoneMap.getTile(x, y).isKeyTraversalTile())
               setBGColor(x, y, RED);
            else
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
      long seed = System.currentTimeMillis();
      seed = 1792899195176L;
      RNG.setSeed(seed);
      System.out.println("Seed: " + seed);
      zoneMap = CatacombMapFactory.getCatacombMap(60, 50, 15, 20, 5, 12);
      //zoneMap = BSPMapFactory.getBSPMap(60, 50, 5, 20);
      update();
      System.out.println("Full Connectivity: " + zoneMap.hasFullConnectivity());
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
      boolean showKeyTraversalTiles = true;
      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(1000, 800);
      ZoneViewer zv = new ZoneViewer(showKeyTraversalTiles);
      frame.add(zv);
      frame.setVisible(true);
   }
}