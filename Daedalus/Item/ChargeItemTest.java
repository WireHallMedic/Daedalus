package Daedalus.Item;

import Daedalus.Actor.*;
import Daedalus.Combat.*;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class ChargeItemTest {


   /** Fixture initialization (common initialization for all tests). **/
   @Before public void setUp() {
   }


   /** A test that always fails. **/
   @Test public void testShieldValues() 
   {
      Shield s = new Shield("Test Shield");
      s.setChargeDelayTurns(1);
      s.setMaxChargeTurns(10);
      Assert.assertEquals("Initial charge is full.", 10, s.getCurDamageCapacity());
      s.applyDamage(5);
      Assert.assertEquals("Taking damage lowers charge.", 5, s.getCurDamageCapacity());
      s.charge();
      s.charge();
      Assert.assertEquals("No change first turn.", 5, s.getCurDamageCapacity());
      s.charge();
      s.charge();
      Assert.assertEquals("Then charge 1 per std turn.", 6, s.getCurDamageCapacity());
      s.charge();
      s.charge();
      Assert.assertEquals("Charge 1 per std turn.", 7, s.getCurDamageCapacity());
      s.charge();
      s.charge();
      Assert.assertEquals("Charge 1 per std turn.", 8, s.getCurDamageCapacity());
      s.charge();
      s.charge();
      Assert.assertEquals("Charge 1 per std turn.", 9, s.getCurDamageCapacity());
      s.charge();
      s.charge();
      Assert.assertEquals("Charge 1 per std turn.", 10, s.getCurDamageCapacity());
      s.charge();
      s.charge();
      Assert.assertEquals("Don't overcharge.", 10, s.getCurDamageCapacity());
      int absorbedDamage = s.applyDamage(12);
      Assert.assertEquals("Absorbed damage returned.", 10, absorbedDamage);
   }


   /** A test that always fails. **/
   @Test public void testWeaponValues() 
   {
      Weapon w = new Weapon("Test Weapon");
      w.setMaxShots(5);
      w.setChargeTimePerShotTurns(1);
      w.fullyCharge();
      Assert.assertEquals("Initial charge is full.", 5, w.getChargedShots());
      w.discharge();
      Assert.assertEquals("Firing lowers charged shots.", 4, w.getChargedShots());
      w.charge();
      Assert.assertEquals("First tick not enought to get shot back.", 4, w.getChargedShots());
      w.charge();
      Assert.assertEquals("Second tick is.", 5, w.getChargedShots());
   }


   /** A test that always fails. **/
   @Test public void testArmorValues() 
   {
      Armor a = new Armor("Test Armor");
      a.setDamageProtection(CombatConstants.DamageType.FIRE, 5);
      Damage d = new Damage();
      d.setValue(CombatConstants.DamageType.FIRE, 3);
      d.setValue(CombatConstants.DamageType.COLD, 3);
      d = a.absorbDamage(d);
      Assert.assertEquals("Higher armor blocks damage", 3, d.getSum());
      
      d.setValue(CombatConstants.DamageType.FIRE, 7);
      d.setValue(CombatConstants.DamageType.COLD, 3);
      d = a.absorbDamage(d);
      Assert.assertEquals("Lower armor blocks some damage", 5, d.getSum());
   }
}
