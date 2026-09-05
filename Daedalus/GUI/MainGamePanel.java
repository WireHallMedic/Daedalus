package Daedalus.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import Daedalus.AI.*;
import Daedalus.Zone.*;
import Daedalus.Item.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;


public class MainGamePanel extends DaePanel implements GUIConstants, AIConstants, ZoneConstants
{
   private BoardPanel boardPanel;
   private static String messagePanelMessage = "";
   private static int messageCount = 0;
   private static boolean dimMessage = false;
   private static boolean persistMessage = false;
   private static final int MESSAGE_PANEL_X_START = (BOARD_SIZE_TILES * 2) + 2;
   private static final int MESSAGE_PANEL_Y_START = 1;
   private static final int MESSAGE_PANEL_WIDTH = PANEL_WIDTH_TILES - MESSAGE_PANEL_X_START - 1;
   private static final int MESSAGE_PANEL_HEIGHT = 4;
   
   public MainGamePanel(TilePalette rectPalette, TilePalette squarePalette)
   {
      super(PANEL_WIDTH_TILES, PANEL_HEIGHT_TILES, rectPalette);
      boardPanel = new BoardPanel(squarePalette);
      showFPS = true;
      clearMessage();
   }
   
   public static void addMessage(String m, boolean waitingForPlayer)
   {
      // all current messages are old, clear for new
      if(dimMessage)
      {
         dimMessage = false;
         messagePanelMessage = "";
      }
      messagePanelMessage = messagePanelMessage + m + " ";
      messageCount++;
      if(!waitingForPlayer)
         persistMessage = true;
   }
   public static void addMessage(String m){addMessage(m, false);}
   
   public static void clearMessage()
   {
      persistMessage = false;
      dimMessage = false;
      messagePanelMessage = "";
      messageCount = 0;
   }
   
   // messages dim the turn after they arrive. There's some finesse here
   // due to some messages arriving while waiting for the player to act; 
   // we'd like them to stick around one turn
   public static void incrementMessagePanel()
   {
      if(persistMessage)
         persistMessage = false;
      else if(!dimMessage && messageCount > 0)
      {
         dimMessage = true;
         messageCount = 0;
      }
   }
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      boardPanel.actionPerformed(ae);
      super.actionPerformed(ae);
   }
   
   @Override
   public void updateVisuals()
   {
      super.updateVisuals();
      
      int messagePanelFGColor = WHITE;
      if(dimMessage)
         messagePanelFGColor = LIGHT_GREY;
      if(messagePanelMessage.length() > 0)
      {
         write(MESSAGE_PANEL_X_START, MESSAGE_PANEL_Y_START, messagePanelMessage, 
               messagePanelFGColor, UI_BG_COLOR, MESSAGE_PANEL_WIDTH, MESSAGE_PANEL_HEIGHT);
      }
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
   protected int[][] getBorderArray()
   {
      int[][] borderArr = super.getBorderArray();
      // section off hud
      for(int x = 0; x < tilesWide; x++)
      {
         borderArr[x][BOARD_SIZE_TILES + 1] = 1;
      }
      // dividing line between board and right side
      for(int y = 0; y < BOARD_SIZE_TILES + 2; y++)
      {
         borderArr[(BOARD_SIZE_TILES * 2) + 1][y] = 1;
      }
      // message panel
      for(int x = 0; x < MESSAGE_PANEL_WIDTH; x++)
      {
         borderArr[MESSAGE_PANEL_X_START + x][MESSAGE_PANEL_Y_START + MESSAGE_PANEL_HEIGHT] = 1;
      }
      return borderArr;
   }
      
   public void keyPressed(KeyEvent ke)
   {
      // single-key actions need to set pendingTarget after seting pendingAction.
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_NUMPAD1:
            Game.getPlayer().getAI().setPendingTarget(Direction.SOUTH_WEST);
            break;
         case KeyEvent.VK_NUMPAD2:
            Game.getPlayer().getAI().setPendingTarget(Direction.SOUTH);
            break;
         case KeyEvent.VK_NUMPAD3:
            Game.getPlayer().getAI().setPendingTarget(Direction.SOUTH_EAST);
            break;
         case KeyEvent.VK_NUMPAD4:
            Game.getPlayer().getAI().setPendingTarget(Direction.WEST);
            break;
         case KeyEvent.VK_NUMPAD5:
            Game.getPlayer().getAI().setPendingAction(ActorAction.DELAY);
            Game.getPlayer().getAI().setPendingTarget(Direction.ORIGIN);
            break;
         case KeyEvent.VK_NUMPAD6:
            Game.getPlayer().getAI().setPendingTarget(Direction.EAST);
            break;
         case KeyEvent.VK_NUMPAD7:
            Game.getPlayer().getAI().setPendingTarget(Direction.NORTH_WEST);
            break;
         case KeyEvent.VK_NUMPAD8:
            Game.getPlayer().getAI().setPendingTarget(Direction.NORTH);
            break;
         case KeyEvent.VK_NUMPAD9:
            Game.getPlayer().getAI().setPendingTarget(Direction.NORTH_EAST);
            break;
         case KeyEvent.VK_ESCAPE:
            clearMessage();
            MainGamePanel.addMessage("Action cancelled.", true);
            Game.getPlayer().getAI().clearPlan();
            break;
         case KeyEvent.VK_U:
            clearMessage();
            MainGamePanel.addMessage("Select target to interact with.", true);
            Game.getPlayer().getAI().setPendingAction(ActorAction.INTERACT);
            break;
         case KeyEvent.VK_G:
            Game.getPlayer().getAI().setPendingAction(ActorAction.PICK_UP);
            Game.getPlayer().getAI().setPendingTarget(Direction.ORIGIN);
            break;
         case KeyEvent.VK_I:
            DaeFrame.setActivePanel(InventoryPanel.class);
            break;
         case KeyEvent.VK_SPACE:
            AnimationScriptFactory.addExplosion(Game.getPlayer().getTileLoc().x - 2, Game.getPlayer().getTileLoc().y);
            break;
      }
   }
   public void keyReleased(KeyEvent ke){}
   public void keyTyped(KeyEvent ke){}
}