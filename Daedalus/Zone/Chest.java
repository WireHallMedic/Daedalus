package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import Daedalus.GUI.*;

public class Chest extends ToggleTile implements ZoneConstants, GUIConstants
{

   public Chest()
   {
      super(TileBase.CHEST, TileBase.OPEN_CHEST);
      oneToggleOnly = true;
   }
}