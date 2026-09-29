package Daedalus;

import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Combat.*;
import Daedalus.Engine.*;
import Daedalus.Ability.*;
import Daedalus.Actor.*;
import Daedalus.Zone.*;
import java.util.*;

public class Main
{
   public static void main(String[] args)
   {
      DaeFrame frame = new DaeFrame();
      //Game game = new Game();
      
      Vector<Zone> zoneList = RegionBuilder.buildWasteland();
      
      Actor a = ActorFactory.getPlayer();
      a.setTileLoc(3, 5);
      a.addToInventory(WeaponFactory.getBeamCannon());
      a.addToInventory(new Shield("Test Shield"));
      a.addToInventory(WeaponFactory.getAutogun());
      a.getCurWeapon().getAttack().getBaseDamage().setValue(CombatConstants.DamageType.COLD, 20);
      Game.setPlayer(a);

      Actor b = ActorFactory.getDrone();
      b.setTileLoc(6, 6);
      zoneList.elementAt(1).getActorList().add(b);
      
      Game.setZoneList(zoneList);
      Game.setZone(zoneList.elementAt(0), new Coord(3, 5));
      Game.play();

   }
}