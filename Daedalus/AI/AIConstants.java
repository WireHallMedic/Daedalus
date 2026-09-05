package Daedalus.AI;

import Daedalus.GUI.GUIConstants;

public interface AIConstants
{
   public static final int PATHING_SEARCH_DIAMETER = GUIConstants.BOARD_SIZE_TILES;
   public static final int FOV_SEARCH_DIAMETER = GUIConstants.BOARD_SIZE_TILES;
   
   public enum ActorAction
   {
      DELAY,
      INTERACT, PICK_UP, DROP, USE,
      STEP,
      ATTACK, ABILITY, CHARGE,
      CONTEXTUAL;
   }
}