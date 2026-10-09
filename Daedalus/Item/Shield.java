package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Ability.*;
import Daedalus.Combat.*;
import java.util.*;

public class Shield extends ChargeItem implements Equippable, ItemConstants, GUIConstants
{
   public enum BaseType
   {
      STANDARD,      // baseline version
      QUICK_CHARGE,  // less delay, less capacity, same charge time per damage (less time overall)
      HEAVY;         // more delay, more capacity, same charge time per damage (more time overall)
   }
   
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
   public int applyDamage(int damageSum, int electDamage)
   {
      // bonus electric damage is not reported as ablated
      int bonusDamage = Math.min(electDamage / 2, getCurDamageCapacity());
      discharge((int)(bonusDamage / damagePerCharge));
      
      int damageAbsorbed = Math.min(damageSum, getCurDamageCapacity());
      discharge((int)(damageAbsorbed / damagePerCharge));
      return damageAbsorbed;
   }
   public int applyDamage(int damageSum){return applyDamage(damageSum, 0);}
   public int applyDamage(Damage damage)
   {
      return applyDamage(damage.getSum(), damage.getValue(CombatConstants.DamageType.ELECTRIC));
   }
   
   
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