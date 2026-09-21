package Daedalus.Actor;

import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Zone.*;
import Daedalus.Combat.*;
import Daedalus.Engine.*;
import Daedalus.Ability.*;
import WidlerSuite.Coord;
import WidlerSuite.WSFontConstants;
import WidlerSuite.ShadowFoV;
import WidlerSuite.ShadowFoVRect;
import java.util.*;

public class Pet extends Actor
{
   private boolean hasBeenPet;
   
   public Pet()
   {
      super();
      hasBeenPet = false;
      setAI(new WolfAI(this));
      getAI().setTeam(AIConstants.Team.PLAYER);
   }
   
   public Pet(String n)
   {
      this();
      setName(n);
   }
   
   public void pet()
   {
      if(!hasBeenPet)
      {
         
      }
      MainGamePanel.clearMessage();
      MainGamePanel.addMessage("You pet " + getName() + ".");
      hasBeenPet = true;
   }
}