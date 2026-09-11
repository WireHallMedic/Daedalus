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
      
      Actor a = new Actor();
      a.setName("Test Player");
      a.setAI(new PlayerAI(a));
      a.setTileLoc(2, 2);
      game.setPlayer(a);
      game.addActor(a);
      for(int i = 0; i < ItemConstants.MAX_INVENTORY_SIZE - 1; i++)
      {
         a.getInventory().add(new Item("Test Item", '*'));
      }
      a.setCurWeapon(Weapon.getMock());
      a.swapWeapons();
      Weapon w = new Weapon("Melee");
      Attack atk = Attack.getMock();
      atk.setRange(1);
      atk.setMelee(true);
      w.setAttack(atk);
      a.setCurWeapon(w);
      a.setShield(new Shield("Test Shield"));
      
      Actor b = new Actor();
      b.setName("NPC");
      b.setTileIndex('X');
      b.setTileLoc(3, 2);
      WanderAI bAI = new WanderAI(b);
      bAI.setStepChance(1.0);
      b.setAI(bAI);
      b.addToInventory(new Item("Test Item", '^'));
      b.addToInventory(new Credits(10));
      game.addActor(b);
      
      b = new Actor();
      b.setName("NPC");
      b.setTileIndex('X');
      b.setTileLoc(4, 1);
      bAI = new WanderAI(b);
      bAI.setStepChance(1.0);
      b.setAI(bAI);
      game.addActor(b);
      b.setShield(new Shield("Test Shield"));
      
      game.play();
   }
}