package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import Daedalus.GUI.*;
import Daedalus.Engine.*;

public class Switch extends ToggleTile implements ZoneConstants, GUIConstants
{
	private int triggerIndex;


	public int getTriggerIndex(){return triggerIndex;}


	public void setTriggerIndex(int t){triggerIndex = t;}

   public Switch()
   {
      super(TileBase.SWITCH, TileBase.FLIPPED_SWITCH);
      triggerIndex = EngineTools.getUniqueNum();
   }
   
   @Override
   protected void onToggle()
   {
      Game.getCurZone().resolveTriggerEvent(triggerIndex);
   }
}
