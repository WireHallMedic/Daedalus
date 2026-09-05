package Daedalus.Actor;


public interface ActorConstants
{
   public static final int FULLY_CHARGED = 10;
   
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
   
   
}