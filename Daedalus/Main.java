package Daedalus;

import Daedalus.GUI.*;
import Daedalus.Engine.*;
import Daedalus.Actor.*;
import Daedalus.Zone.*;

public class Main
{
   public static void main(String[] args)
   {
      DaeFrame frame = new DaeFrame();
      Game.setCurZone(new ZoneMap(10, 10));
      Actor a = new Actor(new TilePalette("Daedalus/res/img/WSFont_16x16.png", 16, 16));
      a.setTileLoc(2, 2);
      Game.setPlayer(a);
   }
}