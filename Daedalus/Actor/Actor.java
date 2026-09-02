package Daedalus.Actor;

import Daedalus.GUI.*;
import WidlerSuite.WSFontConstants;

public class Actor extends UnboundTile
{
   public Actor(TilePalette tp)
   {
      super(tp, '?', GUIConstants.CYAN, GUIConstants.ORANGE);
      setLowerTileIndex(WSFontConstants.CIRCLE_TILE);
   }
}