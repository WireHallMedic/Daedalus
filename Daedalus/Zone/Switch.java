package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import Daedalus.GUI.*;

public class Switch extends ToggleTile implements ZoneConstants, GUIConstants
{

   public Switch()
   {
      super(TileBase.SWITCH, TileBase.FLIPPED_SWITCH);
   }
}