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
      
      Actor a = new Actor("Test Player");
      a.setTileIndex('@');
      a.setAI(new PlayerAI(a));
      a.setTileLoc(3, 5);
      game.setPlayer(a);
      game.addActor(a);
      for(int i = 0; i < ItemConstants.MAX_INVENTORY_SIZE - 1; i++)
      {
         a.getInventory().add(new Item("Test Item", '*'));
      }
      a.setWeapon1(WeaponFactory.getTestWeapon());
      a.setWeapon2(WeaponFactory.getPlasmaCannon());
      a.setShield(new Shield("Test Shield"));
      
      Actor b = new Actor("Zombie");
      b.setAI(new ZombieAI(b));
      b.setTileIndex('z');
      b.setTileLoc(3, 11);
      b.addToInventory(new Item("Test Item", '^'));
      b.addToInventory(new Credits(10));
      b.setCurWeapon(WeaponFactory.getBasicMelee());
      game.addActor(b);
      
      b = new Actor("Wolf");
      b.setAI(new WolfAI(b));
      b.setTileIndex('w');
      b.setTileLoc(4, 11);
      game.addActor(b);
      b.setShield(new Shield("Test Shield"));
      
      b = new Actor("Wolf");
      b.setAI(new WolfAI(b));
      b.setTileIndex('w');
      b.setTileLoc(10, 7);
      game.addActor(b);
      
      b = new Actor("Wolf");
      b.setAI(new WolfAI(b));
      b.setTileIndex('w');
      b.setTileLoc(6, 6);
      game.addActor(b);
      
      game.play();

   }
}