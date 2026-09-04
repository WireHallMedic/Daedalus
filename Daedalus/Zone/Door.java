package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import Daedalus.GUI.*;

public class Door extends ToggleTile implements ZoneConstants, GUIConstants
{

   public Door()
   {
      super(TileBase.DOOR, TileBase.OPEN_DOOR);
   }
}