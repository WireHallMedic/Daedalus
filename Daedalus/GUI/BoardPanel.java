package Daedalus.GUI;


public class BoardPanel extends DaePanel implements GUIConstants
{
   
   public BoardPanel(TilePalette tilePalette)
   {  
      super(BOARD_SIZE_TILES, BOARD_SIZE_TILES, tilePalette);
      setAll('#', WHITE, BLACK);
   }
}