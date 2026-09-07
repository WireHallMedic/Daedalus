package Daedalus.GUI;

import WidlerSuite.Coord;
import Daedalus.Engine.Game;
import Daedalus.Actor.*;
import Daedalus.Zone.*;
import java.awt.*;
import java.util.*;
import java.awt.image.*;

public class BoardPanel extends DaePanel implements GUIConstants
{
   private Coord cornerLoc;
   private double xInset;
   private double yInset;
   private MainGamePanel parentPanel;
   
   public BoardPanel(TilePalette tilePalette, MainGamePanel pp, DaeFrame pFrame)
   {  
      super(BOARD_SIZE_TILES + 2, BOARD_SIZE_TILES + 2, tilePalette, pFrame);
      parentPanel = pp;
      setAll('#', WHITE, BLACK);
      cornerLoc = new Coord(0, 0);
      xInset = 0.0;
      yInset = 0.0;
      AnimationManager.setBoardPanel(this);
   }
   
   // as we generate an oversized image for scrolling, we clip it down to size here
   @Override
   public BufferedImage getUnscaledImage()
   {
      BufferedImage oversizedImage = super.getUnscaledImage();
      
      int tileWidth = palette.getTileWidth();
      int tileHeight = palette.getTileHeight();
      
      int xOrigin = tileWidth + (int)(tileWidth * xInset);
      int yOrigin = tileHeight + (int)(tileHeight * yInset);
      
      // screen shake
      if(AnimationManager.isShakingScreen())
      {
         xOrigin += (int)(tileWidth * AnimationManager.getScreenShakeX());
         yOrigin += (int)(tileWidth * AnimationManager.getScreenShakeY());
         
         // bind to range so we don't try and read outside the image
         xOrigin = Math.max(0, Math.min(2 * tileWidth, xOrigin));
         yOrigin = Math.max(0, Math.min(2 * tileHeight, yOrigin));
      }
      
      return oversizedImage.getSubimage(xOrigin, yOrigin, tileWidth * BOARD_SIZE_TILES, tileHeight * BOARD_SIZE_TILES);
   }
   
   // draw from map, not held tiles
   @Override
   protected void drawImageTiles(Graphics2D g2dUnscaled, int xStep, int yStep)
   {
      // storing these values needs to be as close to drawing as possible to avoid juttering,
      // which is why this is not in updatVisuals()
      if(Game.getPlayer() != null)
      {
         cornerLoc.x = Game.getPlayer().getTileLoc().x - (tilesWide / 2);
         cornerLoc.y = Game.getPlayer().getTileLoc().y - (tilesTall / 2);
         xInset = Game.getPlayer().getXOffset();
         yInset = Game.getPlayer().getYOffset();
      }
      ZoneMap map = Game.getCurZone();
      BufferedImage curTileImage = null;
      for(int x = 0; x < tilesWide; x++)
      for(int y = 0; y < tilesTall; y++)
      {
         curTileImage = Game.getCurZone().getImage(x + cornerLoc.x, y + cornerLoc.y);
         g2dUnscaled.drawImage(curTileImage, xStep * x, yStep * y, null);
      }
      
      // overwrite tile bgs that are being targeted
      if(parentPanel.getMode() == MainGamePanel.TARGETING_MODE)
      {
         Vector<Coord> affectedList = parentPanel.getAffectedList();
         if(affectedList != null)
         {
            for(int i = 0; i < affectedList.size(); i++)
            {
               Coord c = affectedList.elementAt(i);
               ZoneTile zt = Game.getCurZone().getTile(c);
               ImageTile it = new ImageTile(SQUARE_PALETTE, zt.getTileIndex(), zt.getFGColor(), TARGETING_BG_COLOR);
               g2dUnscaled.drawImage(it.getImage(), xStep * (c.x - cornerLoc.x), yStep * (c.y - cornerLoc.y), null);
            }
         }
      }
   }
   
   @Override
   protected void drawUnboundTiles(Graphics2D g2dUnscaled)
   {
      Vector<Actor> actorList = Game.getActorList();
      if(actorList != null)
      {
         for(int i = 0; i < actorList.size(); i++)
            actorList.elementAt(i).drawToImage(g2dUnscaled, palette, cornerLoc);
      }
      for(int i = 0; i < unboundTileList.size(); i++)
         unboundTileList.elementAt(i).drawToImage(g2dUnscaled, palette, cornerLoc);
          
      // occlude tiles outsize the player's FoV
      BufferedImage blackSquare = palette.getTile(' ');
      for(int x = 0; x < tilesWide; x++)
      for(int y = 0; y < tilesTall; y++)
      {
         if(!Game.getPlayer().canSee(x + cornerLoc.x, y + cornerLoc.y))
            g2dUnscaled.drawImage(Game.getCurZone().getLastSeen(x + cornerLoc.x, y + cornerLoc.y), 
                                  palette.getTileWidth() * x, palette.getTileHeight() * y, null);
      }
      
      // draw cursor if needed
      if(AnimationManager.getMediumBlink() && parentPanel.getMode() != MainGamePanel.ACT_MODE)
      {
         int cursorColor = WHITE;
         if(parentPanel.getMode() == MainGamePanel.LOOK_MODE)
            cursorColor = LOOK_CURSOR_COLOR;
         if(parentPanel.getMode() == MainGamePanel.TARGETING_MODE)
            cursorColor = TARGETING_CURSOR_COLOR;
         
         BufferedImage cursorTile = palette.getTile('X', cursorColor, TRANSPARENT);
         int cursorLocX = (parentPanel.getCursorLoc().x - cornerLoc.x) * palette.getTileWidth();
         int cursorLocY = (parentPanel.getCursorLoc().y - cornerLoc.y) * palette.getTileHeight();
         g2dUnscaled.drawImage(cursorTile, cursorLocX, cursorLocY, null);
      }
   }

}