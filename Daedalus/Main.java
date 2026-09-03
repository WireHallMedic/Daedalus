package Daedalus;

import Daedalus.AI.*;
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
      game.setCurZone(ZoneMap.getTestMap());
      
      Actor a = new Actor();
      a.setAI(new PlayerAI(a));
      a.setTileLoc(2, 2);
      game.setPlayer(a);
      game.addActor(a);
      
      Actor b = new Actor();
      b.setName("NPC");
      b.setTileIndex('X');
      b.setTileLoc(1, 1);
      WanderAI bAI = new WanderAI(b);
      bAI.setStepChance(1.0);
      b.setAI(bAI);
      game.addActor(b);
      
      b = new Actor();
      b.setName("NPC");
      b.setTileIndex('X');
      b.setTileLoc(4, 1);
      bAI = new WanderAI(b);
      bAI.setStepChance(1.0);
      b.setAI(bAI);
      game.addActor(b);
      
      game.play();
   }
}