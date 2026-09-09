package Daedalus.Item;

import Daedalus.GUI.*;
import Daedalus.Actor.*;
import Daedalus.Ability.*;
import Daedalus.Combat.*;

public class Shield extends ChargeItem implements ItemConstants, GUIConstants
{
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
      setMaxDamageCapacity(10);
      setChargeDelayStandardTurns(5);
      ticksSinceCharge = 0;
   }
   
   public void setChargeDelayStandardTurns(int cd)
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
   
   // returns remaining damage
   public int applyDamage(int damageSum)
   {
      ticksSinceCharge = 0;
      if(damageSum / damagePerCharge <= getCurCharge())
      {
         setCurCharge((int)(getCurCharge() - (damageSum / damagePerCharge)));
         return 0;
      }
      else
      {
         damageSum -= getCurDamageCapacity();
         setCurCharge(0);
         return damageSum;
      }
   }
}