package Daedalus.Engine;

import java.util.*;

public class RNG
{
   private static java.util.Random rng = new java.util.Random(System.currentTimeMillis());
   
   public static void seed(long s){rng.setSeed(s);}
   public static int nextInt(){return rng.nextInt();}
   public static int nextInt(int bound){return rng.nextInt(bound);}
   public static double nextDouble(){return rng.nextDouble();}
   public static boolean nextBoolean(){return rng.nextBoolean();}
   
   public static TableItem roll(TableItem[] itemList, int level)
   {
      int maxRoll = 0;
      for(TableItem curItem: itemList)
         if(curItem.getMinLevel() <= level && curItem.getMaxLevel() >= level)
            maxRoll += curItem.getWeight();
      int roll = nextInt(maxRoll);
      for(TableItem curItem: itemList)
      {
         if(curItem.getMinLevel() <= level && curItem.getMaxLevel() >= level)
         {
            if(curItem.getWeight() > roll)
               return curItem;
            else
               roll -= curItem.getWeight();
         }
         else
            continue;
      }
      return null;
   }
   
   // hand-roll the array because lists of interfaces are weird
   public static TableItem roll(Vector<? extends TableItem> itemList, int level)
   {
      TableItem[] itemArr = new TableItem[itemList.size()];
      for(int i = 0; i < itemList.size(); i++)
         itemArr[i] = itemList.elementAt(i);
      return roll(itemArr, level);
   }
   
}