package Daedalus.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import Daedalus.AI.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.WSFontConstants;


public class InventoryPanel extends SelectionPanel implements ActionListener, GUIConstants, KeyListener
{
   private Inventory inventory;
   
   public InventoryPanel()
   {
      super();
      inventory = null;
   }
   
   @Override
   public void setVisible(boolean v)
   {
      if(v)
      {
         setList();
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
         for(Item item : inventory.getItemList())
         {
            itemList.add("  " + item.getName());
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
      }
   }
   
   @Override
   public void keyPressed(KeyEvent ke)
   {
      // single-key actions need to set pendingTarget after seting pendingAction.
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_ESCAPE:
            DaeFrame.setActivePanel(MainGamePanel.class);
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
         String str = ((char)WSFontConstants.CENT_TILE) + " " + inventory.getCredits().getValue();
         write(listStartX, listStartY - 2, str, UI_FG_COLOR, UI_BG_COLOR, 12, 1);
      }
   }
}