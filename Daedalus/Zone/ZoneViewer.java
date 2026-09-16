package Daedalus.Zone;

import Daedalus.GUI.*;
import WidlerSuite.Coord;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;


public class ZoneViewer extends DaePanel implements KeyListener, GUIConstants
{
   private ZoneMap zoneMap;
   
   public ZoneViewer(ZoneMap map)
   {
      super(75, 50, SQUARE_PALETTE);
      zoneMap = map;
   }
   
   public void update()
   {
      for(int x = 0; x < tilesWide; x++)
      for(int y = 0; y < tilesTall; y++)
      {
         setTileIndex(x, y, zoneMap.getTile(x, y).getTileIndex());
         setFGColor(x, y, zoneMap.getTile(x, y).getFGColor());
         setBGColor(x, y, zoneMap.getTile(x, y).getBGColor());
      }
   }
   
   public static void main(String[] args)
   {
      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(1000, 800);
      
      ZoneViewer zv = new ZoneViewer(ZoneMap.getTestMap());
      zv.update();
      frame.add(zv);
      frame.setVisible(true);
   }
}