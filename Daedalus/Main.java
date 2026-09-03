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
      Game game = new Game();
      game.setCurZone(new ZoneMap(10, 10));
      Actor a = new Actor();
      a.setTileLoc(2, 2);
      game.setPlayer(a);
      game.addActor(a);
      
      Actor b = new Actor();
      b.setName("NPC");
      b.setTileIndex('X');
      b.setTileLoc(0, 0);
      game.addActor(b);
      
      game.play();
   }
}