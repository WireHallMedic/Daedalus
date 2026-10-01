package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import Daedalus.Combat.*;
import Daedalus.Ability.*;
import java.util.*;

public class ConsumableFactory implements ItemConstants, GUIConstants, CombatConstants, AbilityConstants
{
   public static Vector<? extends TableItem> standardTable = getStandardTable();
   
   
   public static Consumable getMedPatch()
   {
      Consumable c = new Consumable("Med-Patch");
      Ability a = getSelfTargetingAbility("Med-Patch");
      StatusEffect se = new StatusEffect("Healing");
      se.addTag(StatusEffectTag.HEALING);
      se.setIntensity(2);
      a.setStatusEffect(se);
      c.setAbility(a);
      return c;
   }
   
   private static Ability getSelfTargetingAbility(String name)
   {
      Ability a = new Ability(name);
      a.setRange(0);
      return a;
   }
   
   
   private static Vector<TableItemWrapper> getStandardTable()
   {
      Vector<TableItemWrapper> list = new Vector<TableItemWrapper>();
      for(Consumable.BaseType type: Consumable.BaseType.values())
      {
         list.add(new TableItemWrapper(type, 0, 100, 1.0));
      }
      return list;
   }

}