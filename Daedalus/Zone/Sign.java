package Daedalus.Zone;

import java.awt.*;
import Daedalus.GUI.*;

public class Sign extends ZoneTile implements ZoneConstants, GUIConstants
{
	private String text;


	public String getText(){return text;}


	public void setText(String t){text = t;}


   public Sign()
   {
      this("");
   }
   
   public Sign(String str)
   {
      super(TileBase.SIGN);
      setText(str);
   }

   
   public Sign(Sign that)
   {
      this(that.getText());
   }
   
   public Sign copy()
   {
      return new Sign(this);
   }
   
   public void setWall(boolean wall)
   {
      setHighPassable(!wall);
   	setTransparent(!wall);
   }
   
   public void setText(int[][] tileIndexArray)
   {
      String s = " ";
      for(int y = 0; y < tileIndexArray[0].length; y++)
      {
         for(int x = 0; x < tileIndexArray.length; x++)
         {
            s += (char)tileIndexArray[x][y];
         }
         s += "\n ";
      }
      setText(s);
   }
      
}