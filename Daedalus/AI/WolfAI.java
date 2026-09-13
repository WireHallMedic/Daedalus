package Daedalus.AI;

import Daedalus.GUI.*;
import Daedalus.Zone.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;
import java.util.*;

public class WolfAI extends WanderAI implements AIConstants, ZoneConstants
{
   public WolfAI(Actor s)
   {
      super(s);
      setStepChance(.25);
   }
   
   
}