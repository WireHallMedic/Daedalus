package Daedalus.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import Daedalus.AI.*;
import Daedalus.Item.*;
import Daedalus.Zone.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;


public class MapPanel extends SelectionPanel implements ActionListener, GUIConstants, KeyListener
{
   private ZoneMap map;
   private BufferedImage mapImage;
   private BufferedImage[] playerImage;
   private static final int mapHeight = 70;
   private static final int mapWidth = 100;
   
   public MapPanel()
   {
      super();
      map = null;
      mapImage = null;
      playerImage = new BufferedImage[2];
      setHeader("Map");
      setFooter("[ENTER], [ESC], or [SPACE] to exit");
   }
   
   
   @Override
   public void setVisible(boolean v)
   {
      if(v)
      {
         setHeader(Game.getCurMap().getName());
         createMapImage();
      }
      super.setVisible(v);
   }
   
   @Override
   public BufferedImage getUnscaledImage()
   {
      BufferedImage baseUnscaledImage = super.getUnscaledImage();
      
      if(mapImage != null)
      {
         int playerLocIndex = 0;
         if(AnimationManager.getSlowBlink())
            playerLocIndex = 1;
         Graphics2D g2dMap = (Graphics2D)(mapImage.getGraphics());
         g2dMap.drawImage(playerImage[playerLocIndex], (mapWidth / 2) * SQUARE_PALETTE.getTileWidth(), (mapHeight / 2) * SQUARE_PALETTE.getTileHeight(), null);
         
         int unscaledMapSizePixels = mapWidth * palette.getTileWidth();
         // use of tileWidth() on y axis is intentional
         Image mapImageSemiscaled = mapImage.getScaledInstance(mapWidth * palette.getTileWidth(), mapHeight * palette.getTileWidth(), scaleStyle);
         int xInset = (baseUnscaledImage.getWidth() - mapImageSemiscaled.getWidth(null)) / 2;
         int yInset = 2 * palette.getTileHeight();
         Graphics2D g2d = (Graphics2D)(baseUnscaledImage.getGraphics());
         g2d.drawImage(mapImageSemiscaled, xInset, yInset, null);
      }
      
      return baseUnscaledImage;
   }
   
   @Override
   public void keyPressed(KeyEvent ke)
   {
      // single-key actions need to set pendingTarget after seting pendingAction.
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_ENTER:
         case KeyEvent.VK_SPACE:
         case KeyEvent.VK_ESCAPE:
            DaeFrame.setActivePanel(MainGamePanel.class);
            break;
         default :
            super.keyPressed(ke);
      }
   }
   
   private void createMapImage()
   {
      mapImage = new BufferedImage(SQUARE_PALETTE.getTileWidth() * mapWidth, SQUARE_PALETTE.getTileHeight() * mapHeight, BufferedImage.TYPE_INT_ARGB);
      if(Game.getCurMap() != null && Game.getPlayer() != null)
      {
         ZoneMap map = Game.getCurMap();
         Coord cornerTile = Game.getPlayer().getTileLoc();
         cornerTile.x = cornerTile.x - (mapWidth / 2);
         cornerTile.y = cornerTile.y - (mapHeight / 2);
         Graphics2D g2d = (Graphics2D)(mapImage.getGraphics());
         for(int x = 0; x < mapWidth; x++)
         for(int y = 0; y < mapHeight; y++)
         {
            g2d.drawImage(map.getExplored(cornerTile.x + x, cornerTile.y + y), x * SQUARE_PALETTE.getTileWidth(), y * SQUARE_PALETTE.getTileHeight(), null);
         }
         playerImage[0] = SQUARE_PALETTE.getTile(' ', WHITE, YELLOW);
         playerImage[1] = map.getExplored(Game.getPlayer().getTileLoc());
      }
   }
   
}