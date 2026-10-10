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
      boolean testing = false;
      Vector<Zone> zoneList = null;
      
      if(testing)
         zoneList = RegionBuilder.buildTestRegion();
      else
         zoneList = RegionBuilder.buildWasteland();
      
      Actor a = ActorFactory.getPlayer();
      a.addToInventory(ConsumableFactory.getCausticSmokeGrenade());
      a.addToInventory(ConsumableFactory.getGrenade());
      a.addToInventory(ConsumableFactory.getGrenade());
      a.addToInventory(ConsumableFactory.getStims());
      a.addToInventory(ConsumableFactory.getMedPatch());
      a.addToInventory(WeaponFactory.getBaton());
      a.addToInventory(ConsumableFactory.getDecoy(ItemConstants.ItemQuality.HIGH));
      for(int i = 0; i < 10; i++)
         a.addToInventory(LootFactory.roll(1));
      a.getCurWeapon().getAttack().getBaseDamage().setValue(CombatConstants.DamageType.CRYO, 20);
      Game.setPlayer(a);
      
      
      if(testing)
         Game.setZone(zoneList.elementAt(0), new Coord(4, 4));
      else
         Game.setZone(zoneList.elementAt(0), new Coord(4, 10));
      
      Game.setZoneList(zoneList);
      Game.play();

   }
}