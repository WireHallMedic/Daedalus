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
   
   public static TableItem roll(TableItem[] itemList)
   {
      int maxRoll = 0;
      for(TableItem curItem: itemList)
         maxRoll += curItem.getWeight();
      int roll = nextInt(maxRoll);
      for(TableItem curItem: itemList)
      {
         if(curItem.getWeight() < roll)
            return curItem;
         else
            roll -= curItem.getWeight();
      }
      return null;
   }
   
   public static TableItem roll(Vector<TableItem> itemList){return roll((TableItem[])itemList.toArray());}
}