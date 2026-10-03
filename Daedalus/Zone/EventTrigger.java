package Daedalus.Zone;

import java.util.*;
import Daedalus.Engine.*;

public class EventTrigger
{
   public enum TriggerAction
   {
      TOGGLE,
      UNLOCK,
      SET_TILE,
      SPAWN_ACTOR;
   }
   
	private int triggerIndex;
	private Coord targetTile;
	private TriggerAction action;
	private Object actionObj;
	private boolean onlyTriggersOnce;


	public int getTriggerIndex(){return triggerIndex;}
	public Coord getTargetTile(){return new Coord(targetTile);}
	public TriggerAction getAction(){return action;}
	public Object getActionObj(){return actionObj;}
	public boolean onlyTriggersOnce(){return onlyTriggersOnce;}


	public void setTriggerIndex(int t){triggerIndex = t;}
	public void setTargetTile(Coord t){setTargetTile(t.x, t.y);}
	public void setTargetTile(int x, int y){targetTile = new Coord(x, y);}
	public void setAction(TriggerAction a){action = a;}
	public void setActionObj(Object a){actionObj = a;}
	public void setOnlyTriggersOnce(boolean o){onlyTriggersOnce = o;}


   public EventTrigger()
   {
      triggerIndex = -1;
      targetTile = new Coord();
      action = null;
      actionObj = null;
      onlyTriggersOnce = false;
   }
   
   public EventTrigger(int index, int x, int y, TriggerAction tAction)
   {
      this();
      triggerIndex = index;
      targetTile = new Coord(x, y);
      action = tAction;
   }
   public EventTrigger(int index, Coord c, TriggerAction tAction){this(index, c.x, c.y, tAction);}
   
   public EventTrigger(EventTrigger that)
   {
      this();
      this.triggerIndex = that.triggerIndex;
      this.targetTile = that.targetTile.copy();
      this.action = that.action;
      this.actionObj = that.actionObj;
      this.onlyTriggersOnce = that.onlyTriggersOnce;
   }
   
   public EventTrigger copy()
   {
      return new EventTrigger(this);
   }
}