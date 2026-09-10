package Daedalus.GUI;

import WidlerSuite.Coord;
import WidlerSuite.WSFontConstants;

public class GUITools implements GUIConstants, WSFontConstants
{
   public static int[] getBar(int cur, int max, int length)
   {
      cur = Math.max(cur, 0);
      cur = Math.min(cur, max);
      int[] iconArray = new int[length];
      // early exits to avoid int math imprecision
      if(cur == max && max != 0)
      {
         for(int i = 0; i < length; i++)
            iconArray[i] = FULL_BLOCK_TILE;
         return iconArray;
      }
      if(cur == 0)
      {
         for(int i = 0; i < length; i++)
            iconArray[i] = ' ';
         return iconArray;
      }
      
      int calcValue = (cur * (length * 8)) / max;
      calcValue = Math.max(calcValue, 1);
      for(int i = 0; i < length; i++)
      {
         switch(calcValue)
         {
            case 0  : iconArray[i] = ' '; break;
            case 1  : iconArray[i] = BLOCK_ONE_EIGHTH_TILE; calcValue = 0; break;
            case 2  : iconArray[i] = BLOCK_TWO_EIGHTHS_TILE; calcValue = 0; break;
            case 3  : iconArray[i] = BLOCK_THREE_EIGHTHS_TILE; calcValue = 0; break;
            case 4  : iconArray[i] = BLOCK_FOUR_EIGHTHS_TILE; calcValue = 0; break;
            case 5  : iconArray[i] = BLOCK_FIVE_EIGHTHS_TILE; calcValue = 0; break;
            case 6  : iconArray[i] = BLOCK_SIX_EIGHTHS_TILE; calcValue = 0; break;
            case 7  : iconArray[i] = BLOCK_SEVEN_EIGHTHS_TILE; calcValue = 0; break;
            default : iconArray[i] = FULL_BLOCK_TILE; calcValue -= 8; break;
         }
      }
      return iconArray;
   }
   
   
   public static int[] getBarWithBraces(int cur, int max, int length)
   {
      int[] iconArr = new int[length + 2];
      int[] bar = getBar(cur, max, length);
      iconArr[0] = LEFT_BAR_BRACE;
      iconArr[length + 1] = RIGHT_BAR_BRACE;
      for(int i = 0; i < length; i++)
         iconArr[i + 1] = bar[i];
      return iconArr;
   }
   

   public static int[] getDotBar(int cur, int max)
   {
      cur = Math.max(cur, 0);
      cur = Math.min(cur, max);
      int[] iconArray = new int[max];

      for(int i = 0; i < max; i++)
      {
         if(i < cur)
            iconArray[i] = DOT_TILE;
         else
            iconArray[i] = RING_TILE;
      }
      return iconArray;
   }
   
   
   public static int[] getDotBarWithBraces(int cur, int max)
   {
      int[] iconArr = new int[max + 2];
      int[] bar = getDotBar(cur, max);
      iconArr[0] = LEFT_BAR_BRACE;
      iconArr[max + 1] = RIGHT_BAR_BRACE;
      for(int i = 0; i < max; i++)
         iconArr[i + 1] = bar[i];
      return iconArr;
   }

   public static String getPercentageString(int cur, int max)
   {
      if(max == 0)
         return "Err";
         
      int p = (cur * 100) / max;
      return p + "%";
   }
   
   public static String getSignedPercentageString(int cur, int max)
   {
      int p = (cur * 100) / max;
      if(p > 0)
         return "+" + p + "%";
      return p + "%";
   }
   
   public static String getSignedIntString(int i)
   {
      if(i > 0)
         return "+" + i;
      return "" + i;
   }
}