package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Ability.*;
import Daedalus.Combat.*;

public class Shield extends ChargeItem implements ItemConstants, GUIConstants
{
   public static final int STANDARD_MAX_DAMAGE_CAPACITY = 10;
   public static final int STANDARD_CHARGE_DELAY_NORMAL_TURNS = 5;
   public static final int STANDARD_MAX_CHARGE_TIME_STANDARD_TURNS = 10;
   
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
      setChargeDelayTurns(STANDARD_CHARGE_DELAY_NORMAL_TURNS);
      setMaxChargeTurns(STANDARD_MAX_CHARGE_TIME_STANDARD_TURNS);
      ticksSinceCharge = 0;
   }
   
   public void setChargeDelayTurns(int cd)
   {
      chargeDelay = ActorConstants.ActionSpeed.NORMAL.increments * cd;
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
   public void setMaxCharge(int m)
   {
      super.setMaxCharge(m);
      setDamagePerCharge();
      fullyCharge();
   }
   
   @Override
   public void setMaxChargeTurns(int turns)
   {
      super.setMaxChargeTurns(turns);
      setDamagePerCharge();
   }
   
   // returns damage absorbed
   public int applyDamage(int damageSum)
   {
      ticksSinceCharge = 0;
      int damageAbsorbed = Math.min(damageSum, getCurDamageCapacity());
      setCurCharge((int)(getCurCharge() - (damageAbsorbed / damagePerCharge)));
      return damageAbsorbed;
   }
   public int applyDamage(Damage damage){return applyDamage(damage.getSum());}
}