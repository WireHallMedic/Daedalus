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
}