package Daedalus.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import Daedalus.AI.*;
import Daedalus.Zone.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;


public class MainGamePanel extends DaePanel implements GUIConstants, AIConstants, ZoneConstants
{
   private BoardPanel boardPanel;
   
   public MainGamePanel(TilePalette rectPalette, TilePalette squarePalette)
   {
      super(PANEL_WIDTH_TILES, PANEL_HEIGHT_TILES, rectPalette);
      boardPanel = new BoardPanel(squarePalette);
      showFPS = true;
   }
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      boardPanel.actionPerformed(ae);
      super.actionPerformed(ae);
   }
   
   @Override
   public BufferedImage getUnscaledImage()
   {
      BufferedImage unscaledImage = super.getUnscaledImage();
      Graphics2D g2dUnscaled = (Graphics2D)(unscaledImage.getGraphics());
      g2dUnscaled.drawImage(boardPanel.getUnscaledImage(), palette.getTileWidth(), palette.getTileHeight(), null);
      return unscaledImage;
   }
   
   @Override
   public void update()
   {
      super.update();
   }
   
   @Override
   protected int[][] getBorderArray()
   {
      int[][] borderArr = super.getBorderArray();
      for(int x = 0; x < tilesWide; x++)
      {
         borderArr[x][BOARD_SIZE_TILES + 1] = 1;
      }
      for(int y = 0; y < BOARD_SIZE_TILES + 2; y++)
      {
         borderArr[(BOARD_SIZE_TILES * 2) + 1][y] = 1;
      }
      return borderArr;
   }
      
   public void keyPressed(KeyEvent ke)
   {
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_NUMPAD1:
            Game.getPlayer().getAI().setPendingTarget(Direction.SOUTH_WEST);
            Game.getPlayer().getAI().setPendingAction(ActorAction.STEP);
            break;
         case KeyEvent.VK_NUMPAD2:
            Game.getPlayer().getAI().setPendingTarget(Direction.SOUTH);
            Game.getPlayer().getAI().setPendingAction(ActorAction.STEP);
            break;
         case KeyEvent.VK_NUMPAD3:
            Game.getPlayer().getAI().setPendingTarget(Direction.SOUTH_EAST);
            Game.getPlayer().getAI().setPendingAction(ActorAction.STEP);
            break;
         case KeyEvent.VK_NUMPAD4:
            Game.getPlayer().getAI().setPendingTarget(Direction.WEST);
            Game.getPlayer().getAI().setPendingAction(ActorAction.STEP);
            break;
         case KeyEvent.VK_NUMPAD5:
            Game.getPlayer().getAI().setPendingTarget(Direction.ORIGIN);
            Game.getPlayer().getAI().setPendingAction(ActorAction.DELAY);
            break;
         case KeyEvent.VK_NUMPAD6:
            Game.getPlayer().getAI().setPendingTarget(Direction.EAST);
            Game.getPlayer().getAI().setPendingAction(ActorAction.STEP);
            break;
         case KeyEvent.VK_NUMPAD7:
            Game.getPlayer().getAI().setPendingTarget(Direction.NORTH_WEST);
            Game.getPlayer().getAI().setPendingAction(ActorAction.STEP);
            break;
         case KeyEvent.VK_NUMPAD8:
            Game.getPlayer().getAI().setPendingTarget(Direction.NORTH);
            Game.getPlayer().getAI().setPendingAction(ActorAction.STEP);
            break;
         case KeyEvent.VK_NUMPAD9:
            Game.getPlayer().getAI().setPendingTarget(Direction.NORTH_EAST);
            Game.getPlayer().getAI().setPendingAction(ActorAction.STEP);
            break;
         case KeyEvent.VK_SPACE:
            AnimationManager.addNonLocking(AnimationScriptFactory.getStep(Game.getActorList().elementAt(0), Direction.NORTH_EAST));
            break;
      }
   }
   public void keyReleased(KeyEvent ke){}
   public void keyTyped(KeyEvent ke){}
}