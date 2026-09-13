package Daedalus.AI;

import Daedalus.GUI.GUIConstants;

public interface AIConstants
{
   public static final int PATHING_SEARCH_DIAMETER = GUIConstants.BOARD_SIZE_TILES;
   public static final int FOV_SEARCH_DIAMETER = GUIConstants.BOARD_SIZE_TILES;
   
   public enum ActorAction
   {
      DELAY,
      INTERACT, PICK_UP, DROP, USE, EQUIP, SWAP_WEAPONS,
      STEP,
      BASIC_ATTACK, ABILITY, CHARGE,
      CONTEXTUAL;
   }
   
   public enum Team
   {
      PLAYER,
      GOOD,
      NEUTRAL,
      EVIL;
      
      public boolean isEnemy(Team that)
      {
         if(this == that)
            return false;
            
         if(this == PLAYER || this == GOOD)
         {
            switch(that)
            {
               case GOOD :    return false;
               case NEUTRAL : return false;
               case EVIL :    return true;
            }
         }
            
         if(this == NEUTRAL)
         {
            return false;
         }
            
         if(this == EVIL)
         {
            switch(that)
            {
               case GOOD :    return true;
               case NEUTRAL : return false;
               case PLAYER :  return true;
            }
         }
         return false;
      }
   }
   
}