package Daedalus.GUI;

import java.util.*;

public class AnimationManager
{
   private static Vector<AnimationScript> lockingList = new Vector<AnimationScript>();
   private static Vector<AnimationScript> nonLockingList = new Vector<AnimationScript>();
   private static Vector<AnimationScript> semiLockingList = new Vector<AnimationScript>();
   
   public static void addLocking(AnimationScript as){lockingList.add(as);}
   public static void addNonLocking(AnimationScript as){nonLockingList.add(as);}
   public static void addSemiLocking(AnimationScript as){semiLockingList.add(as);}
   
   public static boolean isLocked()
   {
      return lockingList.size() > 0;
   }
   
   public static boolean isSemiLocked()
   {
      return lockingList.size() > 0 || semiLockingList.size() > 0;
   }
   
   public static void update()
   {
      updateList(lockingList);
      updateList(nonLockingList);
      updateList(semiLockingList);
   }
   
   private static void updateList(Vector<AnimationScript> list)
   {
      for(int i = 0; i < list.size(); i++)
      {
         list.elementAt(i).update();
         if(list.elementAt(i).isExpired())
         {
            list.removeElementAt(i);
            i--;
         }
      }
   }
   
   public static void clear()
   {
      lockingList = new Vector<AnimationScript>();
      nonLockingList = new Vector<AnimationScript>();
      semiLockingList = new Vector<AnimationScript>();
   }
   
   public static void removeTargetingScripts(UnboundTile target)
   {
      removeTargetingScriptsFromList(target, lockingList);
      removeTargetingScriptsFromList(target, nonLockingList);
      removeTargetingScriptsFromList(target, semiLockingList);
   }
   
   private static void removeTargetingScriptsFromList(UnboundTile target, Vector<AnimationScript> list)
   {
      for(int i = 0; i < list.size(); i++)
      {
         if(list.elementAt(i).getTarget() == target)
         {
            list.removeElementAt(i);
            i--;
         }
      }
   }
}