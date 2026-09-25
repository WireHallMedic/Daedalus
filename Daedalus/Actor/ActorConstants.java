package Daedalus.Actor;

import Daedalus.Engine.*;


public interface ActorConstants
{
   public static final int FULLY_CHARGED = 10;
   public static final int STARTING_CHARGE = 7;
   public static final int BASE_NPC_HEALTH = 10;
   public static final int BASE_PLAYER_HEALTH = 20;
   
   public enum ActionSpeed
   {
      SLOW           ("Slow", 4, 1),
      NORMAL         ("Normal", 2, 0),
      FAST           ("Fast", 1, -1),
      INSTANTANEOUS  ("Instantaneous", 0, 1000);
      
      public String name;
      public int increments;
      public int modifier;
      
      private ActionSpeed(String n, int i, int m)
      {
         name = n;
         increments = i;
         modifier = m;
      }
      
      // we need to track a non-final value to stack speeds, so that
      // we don't lose information by hitting the rails
      public static ActionSpeed getByModifier(int mod)
      {
         if(mod == INSTANTANEOUS.modifier)
            return INSTANTANEOUS;
         if(mod < 0)
            return FAST;
         else if(mod > 0)
            return SLOW;
         return NORMAL;
      }
   }
   
   
   public enum ActorFamily implements TableItem
   {
      JACKAL   (1, 100, TableItem.BASE_WEIGHT),
      ROACH    (1, 100, TableItem.BASE_WEIGHT),
      DRONE    (1, 100, TableItem.BASE_WEIGHT),
      BANDIT   (1, 100, TableItem.BASE_WEIGHT);
      
      private ActorFamily(int min, int max, int wei)
      {
         minLevel = min;
         maxLevel = max;
         weight = wei;
      }
      
   	private int minLevel;
   	private int maxLevel;
   	private int weight;
   
   	public int getMinLevel(){return minLevel;}
   	public int getMaxLevel(){return maxLevel;}
   	public int getWeight(){return weight;}
   
   	public void setMinLevel(int m){minLevel = m;}
   	public void setMaxLevel(int m){maxLevel = m;}
   	public void setWeight(int w){weight = w;}
   }
}