package Daedalus.Actor;

import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Zone.*;
import Daedalus.Combat.*;
import Daedalus.Engine.*;
import Daedalus.Ability.*;

public class ActorFactory implements ActorConstants, GUIConstants
{
   public static Actor getPlayer()
   {
      Actor a = new Actor("Test Player");
      a.setTileIndex('@');
      a.setAI(new PlayerAI(a));
      for(int i = 0; i < ItemConstants.MAX_INVENTORY_SIZE - 1; i++)
      {
         a.getInventory().add(new Item("Test Item", '*'));
      }
      a.setWeapon1(WeaponFactory.getShotgun());
      a.setWeapon2(WeaponFactory.getPlasmaCannon());
      a.setShield(new Shield("Test Shield"));
      return a;
   }
   
   public static Actor getDrone()
   {
      Actor a = new Actor("Drone");
      a.setTileIndex('d');
      a.setBGColor(GREY);
      a.setFGColor(WHITE);
      a.getBaseStats().setMaxHealth(BASE_NPC_HEALTH);
      a.setAI(new DroneAI(a));
      a.setCurWeapon(WeaponFactory.getDroneGun());
      a.setShield(ShieldFactory.getDroneShield());
      a.fullHeal();
      return a;
   }
}