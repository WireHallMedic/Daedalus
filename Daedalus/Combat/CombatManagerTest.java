package Daedalus.Combat;

import WidlerSuite.Coord;
import Daedalus.Ability.*;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class CombatManagerTest {


   /** Fixture initialization (common initialization for all tests). **/
   @Before public void setUp() {
   }


   /** A test that always fails. **/
   @Test public void testDamageDropoff() 
   {
      Attack atk = new Attack("Test No Dropoff");
      atk.setBaseDamage(new Damage(CombatConstants.DamageType.CONCUSSION, 4));
      atk.setRange(7);
      Coord origin = new Coord(0, 0);
      
      double dropoff = CombatManager.getDamageDropoffMultiplier(atk, origin, new Coord(0, 0));
      Assert.assertEquals("Non-dropoff deals full damage at range 0", 1.0, dropoff, .01);
      dropoff = CombatManager.getDamageDropoffMultiplier(atk, origin, new Coord(1, 0));
      Assert.assertEquals("Non-dropoff deals full damage at range 1", 1.0, dropoff, .01);
      dropoff = CombatManager.getDamageDropoffMultiplier(atk, origin, new Coord(7, 0));
      Assert.assertEquals("Non-dropoff deals full damage at max range", 1.0, dropoff, .01);
      
      atk.setDamageDropoff(true);
      dropoff = CombatManager.getDamageDropoffMultiplier(atk, origin, new Coord(0, 0));
      Assert.assertEquals("Dropoff deals full damage at range 0", 1.0, dropoff, .01);
      dropoff = CombatManager.getDamageDropoffMultiplier(atk, origin, new Coord(1, 0));
      Assert.assertEquals("Dropoff deals full damage at range 1", 1.0, dropoff, .01);
      dropoff = CombatManager.getDamageDropoffMultiplier(atk, origin, new Coord(4, 0));
      Assert.assertEquals("Dropoff deals three quarters damage at range *max - 1) / 2", 0.75, dropoff, .01);
      dropoff = CombatManager.getDamageDropoffMultiplier(atk, origin, new Coord(7, 0));
      Assert.assertEquals("Dropoff deals half damage at max range", 0.5, dropoff, .01);
   }
}
