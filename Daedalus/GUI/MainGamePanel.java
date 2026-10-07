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
   private static final int HUD_COLUMN_WIDTH = HUD_PANEL_WIDTH / 3;
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
   
   public void setMode(int m){mode = m;}
   public void setPendingAbility(Ability a){pendingAbility = a;}
   
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
      if(boardPanel != null)
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
      int barWidth = 6;
      if(mode == ACT_MODE)
      {
         for(int i = 0; i < nearbyActors.size() && i < SURROUNDINGS_PANEL_HEIGHT; i++)
         {
            writeActorSummary(nearbyActors.elementAt(i), row, barWidth);
            row += 2;
         }
         Vector<Item> nearbyItems = getItemsForSurroundingsPanel();
         // excludes credits
         for(int i = 0; i < nearbyItems.size(); i++)
         {
            writeItemSummary(nearbyItems.elementAt(i), row);
            row++;
         }
      }
      if(mode == LOOK_MODE || mode == TARGETING_MODE)
      {
         Actor actor = null;
         Item item = null;
         Corpse corpse = null;
         ZoneTile zoneTile = null;
         if(Game.getPlayer().canSee(cursorLoc))
         {
            actor = Game.getActorAt(cursorLoc);
            item = Game.getCurMap().getItemAt(cursorLoc);
            corpse = Game.getCurMap().getCorpseAt(cursorLoc);
            zoneTile = Game.getCurMap().getTile(cursorLoc);
         }
         if(actor != null)
         {
            writeActorSummary(actor, row, barWidth);
            row += 2;
         }
         if(item != null)
         {
            writeItemSummary(item, row);
            row++;
         }
         if(zoneTile != null)
         {
            writeZoneTileSummary(zoneTile, row);
            row++;
         }
         if(corpse != null)
         {
            writeCorpseSummary(corpse, row);
            row++;
         }
      }
      // clear rest of panel
      while(row < SURROUNDINGS_PANEL_Y_START + SURROUNDINGS_PANEL_HEIGHT)
      {
         write(SURROUNDINGS_PANEL_X_START, row, "", WHITE, BLACK, SURROUNDINGS_PANEL_WIDTH, 1);
         row++;
      }
   }
   
   private void writeActorSummary(Actor a, int row, int barWidth)
   {
      setTile(SURROUNDINGS_PANEL_X_START + 1, row, a.getTileIndex(), a.getFGColor(), a.getBGColor());
      if(a.hasShield())
      {
         drawBar(SURROUNDINGS_PANEL_X_START + 3, row, a.getCurShield(), a.getMaxShield(), barWidth, SHIELD_COLOR);
         drawBar(SURROUNDINGS_PANEL_X_START + 3, row + 1, a.getCurHealth(), a.getMaxHealth(), barWidth, HEALTH_COLOR);
      }
      else
      {
         drawBar(SURROUNDINGS_PANEL_X_START + 3, row, a.getCurHealth(), a.getMaxHealth(), barWidth, HEALTH_COLOR);
         write(SURROUNDINGS_PANEL_X_START, row + 1, "", WHITE, BLACK, SURROUNDINGS_PANEL_WIDTH, 1);
      }
      String nameStr = " " +a.getName() + " (" + a.getAI().getAttitude() + ") " + a.getStatusEffectString();
      write(SURROUNDINGS_PANEL_X_START + 11, row, nameStr, WHITE, BLACK, SURROUNDINGS_PANEL_WIDTH - 11, 1);
   }
   
   private void writeItemSummary(Item item, int row)
   {
      setTile(SURROUNDINGS_PANEL_X_START + 1, row, item.getTileIndex(), item.getFGColor(), item.getBGColor());
      write(SURROUNDINGS_PANEL_X_START + 2, row, " " + item.getName(), WHITE, BLACK, SURROUNDINGS_PANEL_WIDTH - 3, 1);
   }
   
   private void writeCorpseSummary(Corpse corpse, int row)
   {
      setTile(SURROUNDINGS_PANEL_X_START + 1, row, corpse.getTileIndex(), corpse.getFGColor(), BLACK);
      write(SURROUNDINGS_PANEL_X_START + 2, row, " " + corpse.getName(), WHITE, BLACK, SURROUNDINGS_PANEL_WIDTH - 3, 1);
   }
   
   private void writeZoneTileSummary(ZoneTile zoneTile, int row)
   {
      setTile(SURROUNDINGS_PANEL_X_START + 1, row, zoneTile.getTileIndex(), zoneTile.getFGColor(), zoneTile.getBGColor());
      write(SURROUNDINGS_PANEL_X_START + 2, row, " " + zoneTile.getName(), WHITE, BLACK, SURROUNDINGS_PANEL_WIDTH - 3, 1);
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
   
   
   private Vector<Item> getItemsForSurroundingsPanel()
   {
      Vector<Item> itemList = new Vector<Item>();
      Actor player = Game.getPlayer();
      if(player == null)
         return itemList;
      int xStart = player.getTileLoc().x - player.getVisionRadius();
      int yStart = player.getTileLoc().y - player.getVisionRadius();
      int diameter = player.getVisionRadius() * 2 + 1;
      for(int x = 0; x < diameter; x++)
      for(int y = 0; y < diameter; y++)
      {
         if(player.canSee(x + xStart, y + yStart))
         {
            if(Game.getCurMap().isItemAt(x + xStart, y + yStart))
            {
               Item item = Game.getCurMap().getItemAt(x + xStart, y + yStart);
               if(!(item instanceof Credits))
                  itemList.add(item);
            }
         }
      }
      return itemList;
   }
   
   
   // HUD panel
   //////////////////////////////////////////////////////////////////
   
   public void setHUDPanel()
   {
      Actor a = Game.getPlayer();
      if(a == null)
         return;
      // health and shield
      int barWidth = 6;
      int row = HUD_PANEL_Y_START;
      setTile(HUD_PANEL_X_START + 1, row, a.getTileIndex(), a.getFGColor(), a.getBGColor());

      if(a.hasShield())
         drawBar(HUD_PANEL_X_START + 3, row, a.getCurShield(), a.getMaxShield(), barWidth, SHIELD_COLOR);
      else
         write(HUD_PANEL_X_START + 3, row, "", SHIELD_COLOR, BLACK, barWidth + 2, 1);
      drawBar(HUD_PANEL_X_START + barWidth + 6, row, a.getCurHealth(), a.getMaxHealth(), barWidth, HEALTH_COLOR);
      String playerNameStr = a.getName();
      if(a.getStatusEffectString().length() > 0)
         playerNameStr += " (" + a.getStatusEffectString(true) + ")";
      write(HUD_PANEL_X_START + 21, row, playerNameStr, WHITE, BLACK, HUD_PANEL_WIDTH - 21, 1);
      row++;
      write(HUD_PANEL_X_START, row, "", WHITE, BLACK, HUD_PANEL_WIDTH, 1);
      row++;
      
      // weapons
      if(a.getCurWeapon() != null)
      {
         Weapon w = a.getCurWeapon();
         write(HUD_PANEL_X_START + 2, row, w.getName(), WHITE, BLACK, HUD_COLUMN_WIDTH - 3, 1);
         drawDotBar(HUD_PANEL_X_START + 2, row + 1, w.getChargedShots(), w.getMaxShots(), WHITE);
         write(HUD_PANEL_X_START + w.getMaxShots() + 4, row + 1, "", WHITE, BLACK, HUD_COLUMN_WIDTH - (w.getMaxShots() + 5), 1);
         row += 3;
      }
      
      if(a.getOffWeapon() != null)
      {
         Weapon w = a.getOffWeapon();
         write(HUD_PANEL_X_START + 2, row, w.getName(), WHITE, BLACK, HUD_COLUMN_WIDTH - 3, 1);
         drawDotBar(HUD_PANEL_X_START + 2, row + 1, w.getChargedShots(), w.getMaxShots(), WHITE);
         write(HUD_PANEL_X_START + w.getMaxShots() + 4, row + 1, "", WHITE, BLACK, HUD_COLUMN_WIDTH - (w.getMaxShots() + 5), 1);
         row += 3;
      }
      
      ActorConstants.ActionSpeed speed = a.getAttackSpeed();
      int color = WHITE;
      switch(speed)
      {
         case ActorConstants.ActionSpeed.SLOW : color = ORANGE; break;
         case ActorConstants.ActionSpeed.NORMAL : color = WHITE; break;
         case ActorConstants.ActionSpeed.FAST : color = LIGHT_BLUE; break;
         case ActorConstants.ActionSpeed.INSTANTANEOUS : color = PURPLE; break;
      }
      write(HUD_COLUMN_WIDTH * 2, HUD_PANEL_Y_START + 2, "Attack Speed:   " + GUITools.turnsToSeconds(speed.increments), color, BLACK, HUD_COLUMN_WIDTH, 1);
      speed = a.getMoveSpeed();
      switch(speed)
      {
         case ActorConstants.ActionSpeed.SLOW : color = ORANGE; break;
         case ActorConstants.ActionSpeed.NORMAL : color = WHITE; break;
         case ActorConstants.ActionSpeed.FAST : color = LIGHT_BLUE; break;
         case ActorConstants.ActionSpeed.INSTANTANEOUS : color = PURPLE; break;
      }
      write(HUD_COLUMN_WIDTH * 2, HUD_PANEL_Y_START + 3, "Move Speed:     " + GUITools.turnsToSeconds(speed.increments), color, BLACK, HUD_COLUMN_WIDTH, 1);
      speed = a.getInteractSpeed();
      switch(speed)
      {
         case ActorConstants.ActionSpeed.SLOW : color = ORANGE; break;
         case ActorConstants.ActionSpeed.NORMAL : color = WHITE; break;
         case ActorConstants.ActionSpeed.FAST : color = LIGHT_BLUE; break;
         case ActorConstants.ActionSpeed.INSTANTANEOUS : color = PURPLE; break;
      }
      write(HUD_COLUMN_WIDTH * 2, HUD_PANEL_Y_START + 4, "Interact Speed: " + GUITools.turnsToSeconds(speed.increments), color, BLACK, HUD_COLUMN_WIDTH, 1);
      
      // fill rest empty
      row++;
      write(HUD_PANEL_X_START, row, "", WHITE, BLACK, HUD_PANEL_WIDTH, HUD_PANEL_HEIGHT - (row - HUD_PANEL_Y_START));
   }
   
   
   private void drawBar(int xStart, int yStart, int curVal, int maxVal, int barWidth, int fgColor)
   {
      int[] barArray = GUITools.getBarWithBraces(curVal, maxVal, barWidth);
      for(int i = 0; i < barWidth + 2; i++)
      {
         setFGColor(xStart + i, yStart, fgColor);
         setTileIndex(xStart + i, yStart, barArray[i]);
      }
   }
   
   
   private void drawDotBar(int xStart, int yStart, int curVal, int maxVal, int fgColor)
   {
      int[] barArray = GUITools.getDotBarWithBraces(curVal, maxVal);
      for(int i = 0; i < barArray.length; i++)
      {
         setFGColor(xStart + i, yStart, fgColor);
         setTileIndex(xStart + i, yStart, barArray[i]);
      }
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
         case KeyEvent.VK_W:
            Game.getPlayer().getAI().setPendingAction(ActorAction.SWAP_WEAPONS);
            Game.getPlayer().getAI().setPendingTarget(Direction.ORIGIN);
            break;
         case KeyEvent.VK_I:
            DaeFrame.setActivePanel(InventoryPanel.class);
            break;
         case KeyEvent.VK_M:
            DaeFrame.setActivePanel(MapPanel.class);
            break;
         case KeyEvent.VK_L:
            cursorLoc = Game.getPlayer().getTileLoc().copy();
            mode = LOOK_MODE;
            break;
         case KeyEvent.VK_ENTER:
            Game.getPlayer().getAI().setPendingAction(ActorAction.CONTEXTUAL);
            Game.getPlayer().getAI().setPendingTarget(Direction.ORIGIN);
            break;
         case KeyEvent.VK_F:
            if(Game.getPlayer().getCurWeapon() != null)
            {
               if(Game.getPlayer().getCurWeapon().getChargedShots() > 0)
               {
                  setTargetingMode();
                  Game.getPlayer().getAI().setPendingAction(ActorAction.BASIC_ATTACK);
               }
               else
               {
                  clearMessage();
                  MainGamePanel.addMessage("That weapon is out of ammo.", true);
               }
            }
            else
            {
               clearMessage();
               MainGamePanel.addMessage("You are unarmed.", true);
            }
            break;
         case KeyEvent.VK_X:
            Game.getPlayer().getAI().setPendingAction(ActorAction.INTERACT);
            Game.getPlayer().getAI().setPendingTarget(Direction.ORIGIN);
            break;
         case KeyEvent.VK_SPACE:
//             AnimationScriptFactory.addExplosion(Game.getPlayer().getTileLoc());
//             AnimationManager.setScreenRumble();
//             AnimationScriptFactory.addTestEffect();
           // AnimationScriptFactory.addShieldParticles(Game.getPlayer());
           Game.getPlayer().addStatusEffect(StatusEffectFactory.getBurning(2));
               Game.getActorList().elementAt(0).addStatusEffect(new StatusEffect("Test"));
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
            MainGamePanel.addMessage("Attack cancelled", true);
            Game.getPlayer().getAI().clearPlan();
            setNonTargetingValues();
            break;
         case KeyEvent.VK_F:
         case KeyEvent.VK_ENTER:
            Game.getPlayer().getAI().setPendingTarget(cursorLoc);
            mode = ACT_MODE;
            setNonTargetingValues();
            break;
      }
      updateAffectedList();
      
      // if the pending ability has a range of 1, don't wait for the enter key
      if(pendingAbility != null && 
         pendingAbility.getRange() == 1 && 
         !Game.getPlayer().getTileLoc().equals(cursorLoc))
      {
         Game.getPlayer().getAI().setPendingTarget(cursorLoc);
         mode = ACT_MODE;
         setNonTargetingValues();
      }
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
   
   
   public void setTargetingMode(Ability ability)
   {
      if(Game.getPlayer().getAI().getClosestEnemy() != null)
         cursorLoc = Game.getPlayer().getAI().getClosestEnemy().getTileLoc();
      else
         cursorLoc = Game.getPlayer().getTileLoc();
      mode = TARGETING_MODE;
      clearMessage();
      if(ability != null)
         pendingAbility = ability;
      else
         pendingAbility = Game.getPlayer().getBasicAttack();
      affectedList = pendingAbility.getAffectedTiles(Game.getPlayer().getTileLoc(), cursorLoc);
      MainGamePanel.addMessage("Select target.", true);
   }
   public void setTargetingMode(){setTargetingMode(null);}
   
   
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