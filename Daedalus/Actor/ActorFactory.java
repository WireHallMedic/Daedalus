package Daedalus.Actor;

import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Zone.*;
import Daedalus.Combat.*;
import Daedalus.Engine.*;
import Daedalus.Ability.*;

public class ActorFactory implements ActorConstants, GUIConstants, AIConstants
{
   public static Actor getPlayer()
   {
      Actor a = new Actor("Test Player");
      a.setTileIndex('@');
      a.getBaseStats().setMaxHealth(BASE_PLAYER_HEALTH);
      a.setAI(new PlayerAI(a));
      a.setWeapon1(WeaponFactory.getBoltgun());
      a.setWeapon2(WeaponFactory.getShotgun());
      a.setShield(new Shield("Test Shield"));
      a.getAI().setAlertness(Alertness.ALERT);
      a.setThreat(0);
      a.fullHeal();
      return a;
   }
   
   public static Actor getDog(String name)
   {
      Pet p = new Pet(name);
      p.setTileIndex('d');
      p.setBGColor(BROWN);
      p.setFGColor(WHITE);
      p.getBaseStats().setMaxHealth(BASE_NPC_HEALTH / 2);
      p.setNaturalWeapon(WeaponFactory.getJackalJaws());
      p.setThreat(0);
      p.fullHeal();
      return p;
   }
   public static Actor getDog(){return getDog("Dog");}
   
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
      a.setThreat(1);
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
      a.setThreat(1);
      a.fullHeal();
      return a;
   }
   
   public static Actor getBandit()
   {
      Actor a = new Actor("Bandit");
      a.setTileIndex('b');
      a.setBGColor(BLACK);
      a.setFGColor(WHITE);
      a.getBaseStats().setMaxHealth(BASE_NPC_HEALTH);
      a.setAI(new StandardAI(a));
      switch(RNG.nextInt(5))
      {
         case 0   :  a.setCurWeapon(WeaponFactory.getShotgun());
                     a.setThreat(2);
                     break;
         case 1   :  a.setCurWeapon(WeaponFactory.getBoltgun());;
                     a.setThreat(3);
                     break;
         default  :  a.setCurWeapon(WeaponFactory.getAutogun());;
                     a.setThreat(2);
                     break;
      }
      WeaponFactory.setLowQuality(a.getCurWeapon());
      Shield s = ShieldFactory.getBasicShield();
      ShieldFactory.setLowQuality(s);
      a.setShield(s);
      a.fullHeal();
      return a;
   }
}