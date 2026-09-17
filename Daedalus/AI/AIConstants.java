package Daedalus.AI;

import Daedalus.GUI.GUIConstants;

public interface AIConstants
{
   public static final int PATHING_SEARCH_DIAMETER = GUIConstants.BOARD_SIZE_TILES;
   public static final int FOV_SEARCH_DIAMETER = GUIConstants.BOARD_SIZE_TILES;
   public static final int STANDARD_MEMORY_DURATION = 20;
   
   public enum ActorAction
   {
      DELAY,
      INTERACT, PICK_UP, DROP, USE, EQUIP, SWAP_WEAPONS,
      STEP,
      BASIC_ATTACK, ABILITY, CHARGE, NATURAL_ATTACK,
      CONTEXTUAL;
   }
   
   public enum Team
   {
      PLAYER,
      FAUNA,
      ENEMY;
      
      public boolean isEnemy(Team that)
      {
         if(this == that)
            return false;
            
         if(this == PLAYER)
         {
            switch(that)
            {
               case FAUNA :   return true;
               case ENEMY :   return true;
            }
         }
            
         if(this == FAUNA)
         {
            switch(that)
            {
               case PLAYER :  return true;
               case ENEMY :   return true;
            }
         }
            
         if(this == ENEMY)
         {
            switch(that)
            {
               case PLAYER :  return true;
               case FAUNA :   return true;
            }
         }
         return false;
      }
   }
   
}