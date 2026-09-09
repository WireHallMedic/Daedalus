package Daedalus.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import Daedalus.AI.*;
import Daedalus.Zone.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import Daedalus.Combat.*;
import Daedalus.Ability.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;


public class MainGamePanel extends DaePanel implements GUIConstants, AIConstants, ZoneConstants
{
   public static final int ACT_MODE = 1;
   public static final int TARGETING_MODE = 2;
   public static final int LOOK_MODE = 3;
   
   private static final int MESSAGE_PANEL_X_START = (BOARD_SIZE_TILES * 2) + 2;
   private static final int MESSAGE_PANEL_Y_START = 1;
   private static final int MESSAGE_PANEL_WIDTH = PANEL_WIDTH_TILES - MESSAGE_PANEL_X_START - 1;
   private static final int MESSAGE_PANEL_HEIGHT = 4;
   private static final int SURROUNDINGS_PANEL_X_START = MESSAGE_PANEL_X_START;
   private static final int SURROUNDINGS_PANEL_Y_START = MESSAGE_PANEL_Y_START + MESSAGE_PANEL_HEIGHT + 1;
   private static final int SURROUNDINGS_PANEL_WIDTH = MESSAGE_PANEL_WIDTH;
   private static final int SURROUNDINGS_PANEL_HEIGHT = BOARD_SIZE_TILES - MESSAGE_PANEL_HEIGHT - 1;
   private static final int HUD_PANEL_X_START = 1;
   private static final int HUD_PANEL_Y_START = BOARD_SIZE_TILES + 2;
   private static final int HUD_PANEL_WIDTH = PANEL_WIDTH_TILES - 2;
   private static final int HUD_PANEL_HEIGHT = PANEL_HEIGHT_TILES - BOARD_SIZE_TILES - 3;
   
   private BoardPanel boardPanel;
   private static String messagePanelMessage = "";
   private static int messageCount = 0;
   private static boolean dimMessage = false;
   private static boolean persistMessage = false;
   private int mode;
   private Coord cursorLoc;
   private Vector<Coord> affectedList;
   private Ability pendingAbility;
   
   public int getMode(){return mode;}
   public Coord getCursorLoc(){return cursorLoc.copy();}
   public Vector<Coord> getAffectedList(){return affectedList;}
   public Ability getPendingAbility(){return pendingAbility;}
   
   public MainGamePanel(TilePalette rectPalette, TilePalette squarePalette)
   {
      super(PANEL_WIDTH_TILES, PANEL_HEIGHT_TILES, rectPalette);
      boardPanel = new BoardPanel(squarePalette, this);
      showFPS = true;
      mode = ACT_MODE;
      cursorLoc = new Coord();
      affectedList = null;
      pendingAbility = null;
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
      
      setSurroundingsPanel();
      setHUDPanel();
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
   
   
   // surrounds panel
   //////////////////////////////////////////////////////////////////
   
   public void setSurroundingsPanel()
   {
      Vector<Actor> nearbyActors = getActorsForSurroundingsPanel();
      int row = SURROUNDINGS_PANEL_Y_START;
      for(int i = 0; i < nearbyActors.size() && i < SURROUNDINGS_PANEL_HEIGHT; i++)
      {
         Actor a = nearbyActors.elementAt(i);
         setTile(SURROUNDINGS_PANEL_X_START + 1, row, a.getTileIndex(), a.getFGColor(), a.getBGColor());
         write(SURROUNDINGS_PANEL_X_START + 3, row, "[      ]", HEALTH_COLOR, BLACK, 8, 1);
         int[] barArr = GUITools.getBar(a.getCurHealth(), a.getMaxHealth(), 6);
         for(int j = 0; j < barArr.length; j++)
            setTileIndex(SURROUNDINGS_PANEL_X_START + 4 + j, row, barArr[j]);
         write(SURROUNDINGS_PANEL_X_START + 12, row, a.getName(), WHITE, BLACK, SURROUNDINGS_PANEL_WIDTH - 12, 1);
         write(SURROUNDINGS_PANEL_X_START, row + 1, "", WHITE, BLACK, SURROUNDINGS_PANEL_WIDTH, 1);
         row += 2;
      }
      while(row < SURROUNDINGS_PANEL_Y_START + SURROUNDINGS_PANEL_HEIGHT)
      {
         write(SURROUNDINGS_PANEL_X_START, row, "", WHITE, BLACK, SURROUNDINGS_PANEL_WIDTH, 1);
         row++;
      }
      
   }
   
   
   private Vector<Actor> getActorsForSurroundingsPanel()
   {
      Vector<Actor> actorList = new Vector<Actor>();
      if(Game.getActorList() != null)
      {
         for(int i = 0; i < Game.getActorList().size(); i++)
         {
            Actor a = Game.getActorList().elementAt(i);
            if(a != Game.getPlayer() &&
               Game.getPlayer().canSee(a))
               actorList.add(a);
         }
      }
      return actorList;
   }
   
   
   // HUD panel
   //////////////////////////////////////////////////////////////////
   
   public void setHUDPanel()
   {
      Actor a = Game.getPlayer();
      if(a == null)
         return;
      int barWidth = 6;
      int row = HUD_PANEL_Y_START;
      setTile(HUD_PANEL_X_START + 1, row, a.getTileIndex(), a.getFGColor(), a.getBGColor());
      for(int i = 0; i < barWidth + 2; i++)
      {
         setFGColor(HUD_PANEL_X_START + 3, row, HEALTH_COLOR);
         setFGColor(HUD_PANEL_X_START + 5 + barWidth, row, SHIELD_COLOR);
      }
      int[] healthBarArr = GUITools.getBar(a.getCurHealth(), a.getMaxHealth(), barWidth);
      int[] shieldBarArr = GUITools.getBar(a.getCurShield(), a.getMaxShield(), barWidth);
      setTileIndex(HUD_PANEL_X_START + 3, row, '[');
      setTileIndex(HUD_PANEL_X_START + 5 + barWidth, row, '[');
      setTileIndex(HUD_PANEL_X_START + 4 + barWidth, row, ']');
      setTileIndex(HUD_PANEL_X_START + 6 + barWidth + barWidth, row, ']');
      setFGColor(HUD_PANEL_X_START + 3, row, HEALTH_COLOR);
      setFGColor(HUD_PANEL_X_START + 5 + barWidth, row, SHIELD_COLOR);
      setFGColor(HUD_PANEL_X_START + 4 + barWidth, row, HEALTH_COLOR);
      setFGColor(HUD_PANEL_X_START + 6 + barWidth + barWidth, row, SHIELD_COLOR);
      for(int j = 0; j < barWidth; j++)
      {
         setTileIndex(HUD_PANEL_X_START + 4 + j, row, healthBarArr[j]);
         setFGColor(HUD_PANEL_X_START + 4 + j, row, HEALTH_COLOR);
         setTileIndex(HUD_PANEL_X_START + 6 + barWidth + j, row, shieldBarArr[j]);
         setFGColor(HUD_PANEL_X_START + 6 + barWidth + j, row, SHIELD_COLOR);
      }
      write(HUD_PANEL_X_START + 20, row, a.getName(), WHITE, BLACK, HUD_PANEL_WIDTH - 20, 1);
      
      // fill rest empty
      row++;
      write(HUD_PANEL_X_START, row, "", WHITE, BLACK, HUD_PANEL_WIDTH, HUD_PANEL_HEIGHT - (row - HUD_PANEL_Y_START));
   }


   // key input
   //////////////////////////////////////////////////////

   private void actModeKeyPressed(KeyEvent ke)
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
         case KeyEvent.VK_L:
            cursorLoc = Game.getPlayer().getTileLoc().copy();
            mode = LOOK_MODE;
            break;
         case KeyEvent.VK_F:
            if(Game.getPlayer().getCurWeapon() != null)
            {
               cursorLoc = Game.getPlayer().getTileLoc().copy();
               mode = TARGETING_MODE;
               setTargetingValues();
               clearMessage();
               Game.getPlayer().getAI().setPendingAction(ActorAction.BASIC_ATTACK);
               MainGamePanel.addMessage("Select target.", true);
            }
            else
            {
               clearMessage();
               MainGamePanel.addMessage("You are unarmed.", true);
            }
            break;
         case KeyEvent.VK_SPACE:
//             AnimationScriptFactory.addExplosion(Game.getPlayer().getTileLoc());
//             AnimationManager.setScreenRumble();
//             AnimationScriptFactory.addTestEffect();
            AnimationScript as = AnimationScriptFactory.getMeleeAttack(Game.getPlayer(), Direction.EAST);
            AnimationManager.addLocking(as);
            as = AnimationScriptFactory.getMeleeImpact(Game.getActorList().elementAt(1), Direction.WEST);
            AnimationManager.addLocking(as);
            AnimationManager.setScreenRumble(AnimationScriptFactory.MELEE_IMPACT_DELAY);
            break;
      }
   }
   
   private void lookModeKeyPressed(KeyEvent ke)
   {
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_NUMPAD1:
            cursorLoc.x--;
            cursorLoc.y++;
            break;
         case KeyEvent.VK_NUMPAD2:
            cursorLoc.y++;
            break;
         case KeyEvent.VK_NUMPAD3:
            cursorLoc.x++;
            cursorLoc.y++;
            break;
         case KeyEvent.VK_NUMPAD4:
            cursorLoc.x--;
            break;
         case KeyEvent.VK_NUMPAD6:
            cursorLoc.x++;
            break;
         case KeyEvent.VK_NUMPAD7:
            cursorLoc.x--;
            cursorLoc.y--;
            break;
         case KeyEvent.VK_NUMPAD8:
            cursorLoc.y--;
            break;
         case KeyEvent.VK_NUMPAD9:
            cursorLoc.x++;
            cursorLoc.y--;
            break;
         case KeyEvent.VK_ESCAPE:
            mode = ACT_MODE;
            clearMessage();
            MainGamePanel.addMessage("", true);
            break;
      }
   }
   
   private void targetingModeKeyPressed(KeyEvent ke)
   {
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_NUMPAD1:
            cursorLoc.x--;
            cursorLoc.y++;
            break;
         case KeyEvent.VK_NUMPAD2:
            cursorLoc.y++;
            break;
         case KeyEvent.VK_NUMPAD3:
            cursorLoc.x++;
            cursorLoc.y++;
            break;
         case KeyEvent.VK_NUMPAD4:
            cursorLoc.x--;
            break;
         case KeyEvent.VK_NUMPAD6:
            cursorLoc.x++;
            break;
         case KeyEvent.VK_NUMPAD7:
            cursorLoc.x--;
            cursorLoc.y--;
            break;
         case KeyEvent.VK_NUMPAD8:
            cursorLoc.y--;
            break;
         case KeyEvent.VK_NUMPAD9:
            cursorLoc.x++;
            cursorLoc.y--;
            break;
         case KeyEvent.VK_ESCAPE:
            mode = ACT_MODE;
            clearMessage();
            MainGamePanel.addMessage("Attack Cancelled", true);
            Game.getPlayer().getAI().clearPlan();
            setNonTargetingValues();
            break;
         case KeyEvent.VK_ENTER:
            if(Game.getPlayer().getAI().getPendingAction() == ActorAction.BASIC_ATTACK)
            {
               Game.getPlayer().getAI().setPendingTarget(cursorLoc);
            }
            // TODO: non-attack abilities
            mode = ACT_MODE;
            setNonTargetingValues();
            break;
      }
      updateAffectedList();
   }
      
   public void keyPressed(KeyEvent ke)
   {
      if(mode == ACT_MODE)
         actModeKeyPressed(ke);
      else if(mode == LOOK_MODE)
         lookModeKeyPressed(ke);
      else if(mode == TARGETING_MODE)
         targetingModeKeyPressed(ke);
   }
   public void keyReleased(KeyEvent ke){}
   public void keyTyped(KeyEvent ke){}
   
   private void setTargetingValues()
   {
      pendingAbility = Game.getPlayer().getBasicAttack();
      affectedList = pendingAbility.getAffectedTiles(Game.getPlayer().getTileLoc(), cursorLoc);
   }
   
   private void updateAffectedList()
   {
      if(mode == TARGETING_MODE)
         affectedList = pendingAbility.getAffectedTiles(Game.getPlayer().getTileLoc(), cursorLoc);
      else
         setNonTargetingValues();
   }
   
   private void setNonTargetingValues()
   {
      pendingAbility = null;
      affectedList = null;
   }
}