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
   
   public BoardPanel(TilePalette tilePalette)
   {  
      super(BOARD_SIZE_TILES + 2, BOARD_SIZE_TILES + 2, tilePalette);
      setAll('#', WHITE, BLACK);
      cornerLoc = new Coord(0, 0);
      xInset = 0.0;
      yInset = 0.0;
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
      BufferedImage curTile = null;
      for(int x = 0; x < tilesWide; x++)
      for(int y = 0; y < tilesTall; y++)
      {
         curTile = Game.getCurZone().getTile(x + cornerLoc.x, y + cornerLoc.y).getImage();
         g2dUnscaled.drawImage(curTile, xStep * x, yStep * y, null);
      }
   }
   
   @Override
   protected void drawUnboundTiles(Graphics2D g2dUnscaled)
   {
      Vector<Actor> actorList = Game.getActorList();
      if(actorList != null)
      {
         for(Actor a : actorList)
            a.drawToImage(g2dUnscaled, cornerLoc);
      }
      for(UnboundTile ut: unboundTileList)
         ut.drawToImage(g2dUnscaled, cornerLoc);
   }

}