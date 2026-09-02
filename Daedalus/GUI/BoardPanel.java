package Daedalus.GUI;

import WidlerSuite.Coord;
import Daedalus.Engine.Game;
import Daedalus.Actor.*;
import java.awt.*;
import java.util.*;

public class BoardPanel extends DaePanel implements GUIConstants
{
   private Coord cornerLoc;
   public BoardPanel(TilePalette tilePalette)
   {  
      super(BOARD_SIZE_TILES, BOARD_SIZE_TILES, tilePalette);
      setAll('#', WHITE, BLACK);
      cornerLoc = new Coord(0, 0);
   }
   
   @Override
   public void update()
   {
      super.update();
      if(Game.getPlayer() != null)
      {
         cornerLoc.x = Game.getPlayer().getTileLoc().x - (BOARD_SIZE_TILES / 2);
         cornerLoc.y = Game.getPlayer().getTileLoc().y - (BOARD_SIZE_TILES / 2);
      }
      if(Game.getCurZone() != null)
      {
         for(int x = 0; x < BOARD_SIZE_TILES; x++)
         for(int y = 0; y < BOARD_SIZE_TILES; y++)
         {
            imageTileArr[x][y].set(Game.getCurZone().getTile(x + cornerLoc.x, y + cornerLoc.y));
         }
      }
   }
   
   @Override
   protected void drawUnboundTiles(Graphics2D g2dUnscaled)
   {
      Vector<Actor> actorList = Game.getActorList();
      // as the player location can change between determining the corner tile and drawing the sprite,
      // causing juttering, we'll just always display it in the exact center
      Actor player = Game.getPlayer();
      if(player != null)
      {
         g2dUnscaled.drawImage(player.getImage(), (BOARD_SIZE_TILES / 2) * palette.getTileWidth(), 
                               (BOARD_SIZE_TILES / 2) * palette.getTileHeight(), null);
      }
      if(actorList != null)
      {
         for(Actor a : actorList)
            if(a != Game.getPlayer())
               a.drawToImage(g2dUnscaled, cornerLoc);
      }
      for(UnboundTile ut: unboundTileList)
         ut.drawToImage(g2dUnscaled, cornerLoc);
   }

}