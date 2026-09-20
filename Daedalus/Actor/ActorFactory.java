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
      a.setWeapon1(WeaponFactory.getBoltgun());
      a.setWeapon2(WeaponFactory.getShotgun());
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
      a.getBaseStats().setFlying(true);
      a.setAI(new DroneAI(a));
      a.setCurWeapon(WeaponFactory.getDroneGun());
      a.setShield(ShieldFactory.getDroneShield());
      a.fullHeal();
      return a;
   }
   
   public static Actor getJackal()
   {
      Actor a = new Actor("Jackal");
      a.setTileIndex('j');
      a.setBGColor(BROWN);
      a.setFGColor(ORANGE);
      a.getBaseStats().setMaxHealth(BASE_NPC_HEALTH / 2);
      a.setAI(new WolfAI(a));
      a.setNaturalWeapon(WeaponFactory.getJackalJaws());
      a.getBaseStats().setMoveSpeed(ActionSpeed.FAST);
      a.fullHeal();
      return a;
   }
   
   public static Actor getBandit()
   {
      Actor a = new Actor("Bandit");
      a.setTileIndex('b');
      a.setBGColor(BLACK);
      a.setFGColor(WHITE);
      a.getBaseStats().setMaxHealth(BASE_NPC_HEALTH / 2);
      a.setAI(new StandardAI(a));
      switch(RNG.nextInt(5))
      {
         case 0   : a.setCurWeapon(WeaponFactory.getShotgun()); break;
         case 1   : a.setCurWeapon(WeaponFactory.getAutogun()); break;
         default  : a.setCurWeapon(WeaponFactory.getBoltgun()); break;
      }
      WeaponFactory.setLowQuality(a.getCurWeapon());
      a.setShield(ShieldFactory.getDroneShield());
      a.fullHeal();
      return a;
   }
}