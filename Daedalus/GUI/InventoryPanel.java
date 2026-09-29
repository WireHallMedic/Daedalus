package Daedalus.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import Daedalus.AI.*;
import Daedalus.Item.*;
import Daedalus.Zone.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;


public class InventoryPanel extends SelectionPanel implements ActionListener, GUIConstants, KeyListener
{
   private Inventory inventory;
   private static final int DESCRIPTION_COLUMN_X_START = PANEL_WIDTH_TILES / 3;
   private static final int EQUIPPED_COLUMN_X_START = (PANEL_WIDTH_TILES / 3) * 2;
   private static final int COLUMN_WIDTH = (PANEL_WIDTH_TILES / 3) - 1;
   
   public InventoryPanel()
   {
      super();
      inventory = null;
      setHeader("Inventory");
      setFooter("[D]rop, [ENTER] to Equip or Use, [ESC] to exit");
      write(listStartX, 3, "       Stored", UI_FG_COLOR, UI_BG_COLOR, COLUMN_WIDTH, 1);
      write(DESCRIPTION_COLUMN_X_START, 3, "       Selected", UI_FG_COLOR, UI_BG_COLOR, COLUMN_WIDTH, 1);
      write(EQUIPPED_COLUMN_X_START, 3, "       Equipped", UI_FG_COLOR, UI_BG_COLOR, COLUMN_WIDTH, 1);
   }
   
   @Override
   public void setVisible(boolean v)
   {
      if(v)
      {
         setList();
         curIndex = 0;
      }
      super.setVisible(v);
   }
   
   private void setList()
   {
      if(Game.getPlayer() != null)
         inventory = Game.getPlayer().getInventory();
      if(inventory != null)
      {
         itemList.clear();
         fgColorList.clear();
         for(Item item : inventory.getItemList())
         {
            itemList.add("  " + item.getName());
            fgColorList.add(item.getFGColor());
         }
      }
   }
   
   @Override
   public void updateVisuals()
   {
      if(inventory != null)
      {
         for(int i = 0; i < inventory.size(); i++)
         {
            Item item = inventory.getItemList().elementAt(i);
            setTile(listStartX, listStartY + i, item.getTileIndex(), item.getFGColor(), item.getBGColor());
         }
         setDescription();
         setEquippedDescription();
      }
   }
   
   private void setDescription()
   {
      if(inventory != null)
      {
         writeItem(inventory.getItem(curIndex), DESCRIPTION_COLUMN_X_START);
      }
   }
   
   
   private void setEquippedDescription()
   {
      if(inventory != null)
      {
         if(inventory.getItem(curIndex) instanceof Weapon)
            writeItem(Game.getPlayer().getCurWeapon(), EQUIPPED_COLUMN_X_START);
         if(inventory.getItem(curIndex) instanceof Shield)
            writeItem(Game.getPlayer().getShield(), EQUIPPED_COLUMN_X_START);
         if(inventory.getItem(curIndex) instanceof Armor)
            writeItem(Game.getPlayer().getArmor(), EQUIPPED_COLUMN_X_START);
      }
   }
   
   private void writeItem(Item item, int colStartX)
   {
      Vector<String> descList = new Vector<String>();
      int nameColor = WHITE;
      if(item != null)
      {
         nameColor = item.getFGColor();
         if(item instanceof Equippable)
         {
            descList = ((Equippable)item).getDescriptionList();
         }
      }
      
      if(descList.size() > 0)
      {
         write(colStartX, 4, descList.elementAt(0), nameColor, UI_BG_COLOR, COLUMN_WIDTH, 1);
      }
      else
      {
         write(colStartX, 4, "", nameColor, UI_BG_COLOR, COLUMN_WIDTH, 1);
      }
      for(int i = 1; i < PANEL_HEIGHT_TILES - 6; i++)
      {
         if(i < descList.size())
            write(colStartX, i + 4, descList.elementAt(i), WHITE, UI_BG_COLOR, COLUMN_WIDTH, 1);
         else
            write(colStartX, i + 4, "", WHITE, UI_BG_COLOR, COLUMN_WIDTH, 1);
      }
   }
   
   @Override
   public void keyPressed(KeyEvent ke)
   {
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_ESCAPE:
            DaeFrame.setActivePanel(MainGamePanel.class);
            break;
         case KeyEvent.VK_D:
            if(inventory.getItemList().size() > 0)
            {
               Game.getPlayer().getAI().setPendingAction(AIConstants.ActorAction.DROP);
               Game.getPlayer().getAI().setPendingIndex(curIndex);
               Game.getPlayer().getAI().setPendingTarget(ZoneConstants.Direction.ORIGIN);
               DaeFrame.setActivePanel(MainGamePanel.class);
            }
            break;
         case KeyEvent.VK_ENTER:
            if(inventory.getItemList().size() > 0)
            {
               if(inventory.getItem(curIndex) instanceof Equippable)
               {
                  Game.getPlayer().getAI().setPendingAction(AIConstants.ActorAction.EQUIP);
                  Game.getPlayer().getAI().setPendingIndex(curIndex);
                  Game.getPlayer().getAI().setPendingTarget(ZoneConstants.Direction.ORIGIN);
                  DaeFrame.setActivePanel(MainGamePanel.class);
               }
               else
                  System.out.println("Non-equippable item.");
            }
            break;
         default :
            super.keyPressed(ke);
      }
   }
   
   @Override
   public void setTiles()
   {
      super.setTiles();
      if(inventory != null)
      {
         String str = ((char)FontConstants.CENT_TILE) + " " + inventory.getCredits().getValue();
         write(listStartX, listStartY - 2, str, CREDIT_COLOR, UI_BG_COLOR, 12, 1);
      }
   }
}