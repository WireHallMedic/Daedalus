package Daedalus.Item;

import java.util.*;
import Daedalus.Engine.*;

public class LootFactory implements ItemConstants
{
   // good ol' y=mx+b for credit calcs, with non-linear sampling of linear function
   private static final double POW = 2.5;
   private static final double X1 = 1;
   private static final double Y1 = 10;
   private static final double X2 = Math.pow(20, POW);
   private static final double Y2 = 10000;
   private static final double M = (Y2 - Y1) / (X2 - X1);
   private static final double B = Y1 - (M * X1);
   
   public static Vector<? extends TableItem> standardTable = getStandardTable();
   
   
   public static int getMaxCredits(int level)
   {
      return (int)Math.round((M * Math.pow(level, POW)) + B);
   }
   
   public static Credits rollCredits(int level)
   {
      int val = getMaxCredits(level);
      double mod = (RNG.nextDouble() / 2.0) + .5;
      val = (int)(val * mod);
      return new Credits(val);
   }
   
   public static Item roll(int level)
   {
      TableItemWrapper wrapper = (TableItemWrapper)RNG.roll(standardTable, level);
      switch((ItemBase)wrapper.getObject())
      {
         case ItemBase.CREDITS:     return rollCredits(level);
         case ItemBase.WEAPON:      return WeaponFactory.rollWeapon(level);
         case ItemBase.SHIELD:      return ShieldFactory.rollShield(level);
         case ItemBase.ARMOR:       return ArmorFactory.rollArmor(level);
//          case ItemBase.MOD:         return rollCredits(level);
//          case ItemBase.GADGET:      return rollCredits(level);
//          case ItemBase.CONSUMABLE:  return rollCredits(level);
      }
      return null;
   }
   
   private static Vector<TableItemWrapper> getStandardTable()
   {
      Vector<TableItemWrapper> list = new Vector<TableItemWrapper>();
      list.add(new TableItemWrapper(ItemBase.CREDITS,    0,    100,  2.0));
      list.add(new TableItemWrapper(ItemBase.WEAPON,     0,    100,  1.0));
      list.add(new TableItemWrapper(ItemBase.SHIELD,     0,    100,  0.5));
      list.add(new TableItemWrapper(ItemBase.ARMOR,      0,    100,  0.5));
//      list.add(new TableItemWrapper(ItemBase.MOD,        0,    100,  .25));
//      list.add(new TableItemWrapper(ItemBase.GADGET,     0,    100,  .25));
//      list.add(new TableItemWrapper(ItemBase.CONSUMABLE, 0,    100,  1.0));
      return list;
   }
   
   public static void main(String[] args)
   {
      for(int i = 0; i < 21; i++)
         System.out.println(i + " " + getMaxCredits(i));
   }
}