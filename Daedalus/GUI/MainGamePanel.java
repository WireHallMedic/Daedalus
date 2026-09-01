package Daedalus.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;


public class MainGamePanel extends DaePanel implements GUIConstants
{
   private BoardPanel boardPanel;
   
   public MainGamePanel(TilePalette rectPalette, TilePalette squarePalette)
   {
      super(PANEL_WIDTH_TILES, PANEL_HEIGHT_TILES, rectPalette);
      boardPanel = new BoardPanel(squarePalette);
      
      boolean toggle = false;
      for(int x = 0; x < PANEL_WIDTH_TILES; x++)
      {
         toggle = !toggle;
         for(int y = 0; y < PANEL_HEIGHT_TILES; y++)
         {
            toggle = !toggle;
            if(toggle)
               setBGColor(x, y, CYAN);
            else
               setBGColor(x, y, ORANGE);
         }
      }      
      
      for(int x = 0; x < BOARD_SIZE_TILES; x++)
      {
         for(int y = 0; y < BOARD_SIZE_TILES; y++)
         {
            toggle = !toggle;
            if(toggle)
               boardPanel.setBGColor(x, y, RED);
            else
               boardPanel.setBGColor(x, y, BEIGE);
         }
      }
         
   }
   
   public BufferedImage getUnscaledImage()
   {
      BufferedImage unscaledImage = super.getUnscaledImage();
      Graphics2D g2dUnscaled = (Graphics2D)(unscaledImage.getGraphics());
      g2dUnscaled.drawImage(boardPanel.getUnscaledImage(), palette.getTileWidth(), palette.getTileHeight(), null);
      return unscaledImage;
   }
}