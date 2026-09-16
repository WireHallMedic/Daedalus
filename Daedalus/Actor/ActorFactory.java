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