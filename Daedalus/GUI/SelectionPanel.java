package Daedalus.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;


public class SelectionPanel extends DaePanel implements ActionListener, GUIConstants, KeyListener
{
   protected Vector<String> itemList;
   protected int curIndex;
   protected int listStartX;
   protected int listStartY;
   protected int maxListLen;
   protected int maxStringWidth;
   
   public SelectionPanel()
   {
      super(PANEL_WIDTH_TILES, PANEL_HEIGHT_TILES, RECT_PALETTE);
      curIndex = 0;
      itemList = new Vector<String>();
      listStartX = 4;
      listStartY = 4;
      maxListLen = 20;
      maxStringWidth = 40;
   }
   
   @Override
   public void setVisible(boolean v)
   {
      if(v)
         setTiles();
      super.setVisible(v);
   }
   
   public void keyPressed(KeyEvent ke)
   {
      // single-key actions need to set pendingTarget after seting pendingAction.
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_DOWN:
         case KeyEvent.VK_NUMPAD2:
            incrementCurIndex();
            break;
         case KeyEvent.VK_UP:
         case KeyEvent.VK_NUMPAD8:
            decrementCurIndex();
            break;
      }
   }
   public void keyReleased(KeyEvent ke){}
   public void keyTyped(KeyEvent ke){}

   public void incrementCurIndex()
   {
      curIndex++;
      if(curIndex >= itemList.size())
         curIndex = 0;
      setCursor();
   }

   public void decrementCurIndex()
   {
      curIndex--;
      if(curIndex < 0)
         curIndex = itemList.size() - 1;
      setCursor();
   }
   
   public void setTiles()
   {
      String str;
      for(int i = 0; i < maxListLen; i++)
      {
         str = "";
         if(i < itemList.size())
            str = itemList.elementAt(i);
         write(listStartX, listStartY + i, str, UI_FG_COLOR, UI_BG_COLOR, maxStringWidth, 1);
      }
      setCursor();
   }
   
   protected void setCursor()
   {
      for(int i = 0; i < maxListLen; i++)
      {
         setTileIndex(listStartX - 2, listStartY + i, ' ');
      }
      
      if(itemList.size() > 0)
      {
         setTileIndex(listStartX - 2, listStartY + curIndex, CURSOR_ICON_INDEX);
      }
   }
}
