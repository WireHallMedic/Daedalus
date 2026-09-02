package Daedalus.GUI;

import WidlerSuite.Coord;
import Daedalus.Engine.Game;
import java.awt.*;

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
      if(Game.getPlayer() != null)
      {
         Game.getPlayer().drawToImage(g2dUnscaled, cornerLoc);
      }
      for(UnboundTile ut: unboundTileList)
         ut.drawToImage(g2dUnscaled, cornerLoc);
   }
}