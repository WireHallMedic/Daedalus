package Daedalus.Item;

public class LootFactory
{
   private static final double POW = 2.5;
   private static final double X1 = 1;
   private static final double Y1 = 10;
   private static final double X2 = Math.pow(20, POW);
   private static final double Y2 = 10000;
   private static final double M = (Y2 - Y1) / (X2 - X1);
   private static final double B = Y1 - (M * X1);
   
   public static double getMaxCredits(int level)
   {
      return (M * Math.pow(level, POW)) + B;
   }
   
   public static void main(String[] args)
   {
      for(int i = 0; i < 6; i++)
         System.out.println(i + " " + getMaxCredits(i));
   }
}