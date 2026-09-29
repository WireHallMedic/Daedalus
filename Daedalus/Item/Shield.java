package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Ability.*;
import Daedalus.Combat.*;
import java.util.*;

public class Shield extends ChargeItem implements Equippable, ItemConstants, GUIConstants
{
   public static final int STANDARD_MAX_DAMAGE_CAPACITY = 10;
   public static final int STANDARD_CHARGE_DELAY = 10;
   public static final int STANDARD_MAX_CHARGE_TIME = 20;
   
	private int maxDamageCapacity;
	private int chargeDelay;
   private int ticksSinceCharge;
	private double damagePerCharge;


	public int getMaxDamageCapacity(){return maxDamageCapacity;}
	public int getChargeDelay(){return chargeDelay;}
	public double getDamagePerCharge(){return damagePerCharge;}
   public int getTicksSinceCharge(){return ticksSinceCharge;}


	public void setMaxDamageCapacity(int m){maxDamageCapacity = m; setDamagePerCharge();}
	public void setChargeDelay(int c){chargeDelay = c;}
	public void setDamagePerCharge(){damagePerCharge = (double)getMaxDamageCapacity() / (double)getMaxCharge();}


   public Shield(String name)
   {
      super(name, ItemBase.SHIELD);
      setMaxDamageCapacity(STANDARD_MAX_DAMAGE_CAPACITY);
      setChargeDelay(STANDARD_CHARGE_DELAY);
      setMaxChargeTurns(STANDARD_MAX_CHARGE_TIME);
      ticksSinceCharge = 0;
   }
   
   
	public int getCurDamageCapacity()
   {
      return Math.round((int)(getCurCharge() * damagePerCharge));
   }
   
   @Override
   public void charge()
   {
      if(ticksSinceCharge == chargeDelay)
         super.charge();
      else
         ticksSinceCharge++;
   }
   
   @Override
   public void setMaxChargeTurns(int t)
   {
      super.setMaxChargeTurns(t);
      setDamagePerCharge();
   }
   
   @Override
   public void discharge(int val)
   {
      ticksSinceCharge = 0;
      super.discharge(val);
   }
   
   @Override
   public void fullyDischarge()
   {
      ticksSinceCharge = 0;
      super.fullyDischarge();
   }
   
   // returns damage absorbed
   public int applyDamage(int damageSum)
   {;
      int damageAbsorbed = Math.min(damageSum, getCurDamageCapacity());
      //setCurCharge((int)(getCurCharge() - (damageAbsorbed / damagePerCharge)));
      discharge((int)(damageAbsorbed / damagePerCharge));
      return damageAbsorbed;
   }
   public int applyDamage(Damage damage){return applyDamage(damage.getSum());}
   
   
   public Vector<String> getDescriptionList()
   {
      Vector<String> list = new Vector<String>();
      list.add(getName());
      list.add("Damage Capacity: " + getMaxDamageCapacity());
      list.add("Charge Delay:    " + GUITools.turnsToSeconds(getChargeDelay()));
      list.add("Full Charge:     " + GUITools.turnsToSeconds(getTurnsToFullCharge()));
      return list;
   }
}