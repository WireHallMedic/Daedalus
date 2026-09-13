package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Combat.*;
import Daedalus.Ability.*;

public class ShieldFactory implements ItemConstants, GUIConstants, CombatConstants
{
   public static Shield getBasicShield()
   {
      return new Shield("Basic Shield");
   }
}