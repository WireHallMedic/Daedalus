package Daedalus;

import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Engine.*;
import Daedalus.Ability.*;
import Daedalus.Actor.*;
import Daedalus.Zone.*;

public class Main
{
   public static void main(String[] args)
   {
      DaeFrame frame = new DaeFrame();
      Game game = new Game();
      game.setCurZone(ZoneMap.getTestMap());
      
      Actor a = ActorFactory.getPlayer();
      a.setTileLoc(3, 5);
      game.setPlayer(a);
      game.addActor(a);
      
      Actor b = new Actor("Zombie");
      b.setAI(new ZombieAI(b));
      b.setTileIndex('z');
      b.setTileLoc(3, 11);
      b.addToInventory(new Item("Test Item", '^'));
      b.addToInventory(new Credits(10));
      b.setCurWeapon(WeaponFactory.getBasicMelee());
      game.addActor(b);
      
//       b = new Actor("Wolf");
//       b.setAI(new WolfAI(b));
//       b.setTileIndex('w');
//       b.setShield(new Shield("Test Shield"));
      b = ActorFactory.getDrone();
      b.setTileLoc(4, 11);
      game.addActor(b);
      
//       b = new Actor("Wolf");
//       b.setAI(new WolfAI(b));
//       b.setTileIndex('w');
      b = ActorFactory.getDrone();
      b.setTileLoc(10, 7);
      game.addActor(b);
      
//       b = new Actor("Wolf");
//       b.setAI(new WolfAI(b));
//       b.setTileIndex('w');
      b = ActorFactory.getDrone();
      b.setTileLoc(6, 6);
      game.addActor(b);
      
      game.play();

   }
}