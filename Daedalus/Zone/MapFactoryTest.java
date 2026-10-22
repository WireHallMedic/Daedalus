package Daedalus.Zone;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Daedalus.Engine.*;


public class MapFactoryTest implements ZoneConstants
{


   /** Fixture initialization (common initialization for all tests). **/
   @Before public void setUp() {
   }


   
   @Test public void testWastelandExitBlocking() 
   {
      int overwrittenExitMaps = 0;
      ZoneMap zm = null;
      long seed = System.currentTimeMillis();
      for(int i = 0; i < 1000; i++)
      {
         seed++;
         RNG.setSeed(seed);
         zm = WastelandMapFactory.getUnpaintedBasicMap(1, BITMASK_NORTH + BITMASK_EAST + BITMASK_WEST + BITMASK_SOUTH);
         if(zm.getExitList().size() != 4)
         {
            System.out.println("Failure on seed " + seed);
            overwrittenExitMaps++;
         }
      }
      Assert.assertEquals("0/1000 generated maps fill exits as unreachable", 0, overwrittenExitMaps);
   }

   
   @Test public void testGenerationWithoutError() 
   {
      ZoneMap map = null;
      for(int i = 0; i < 100; i++)
         map = BSPMapFactory.getCatacombMap(100, 100, 20, 20, 5, 10, .75);
      for(int i = 0; i < 100; i++)
         map = BSPMapFactory.getBSPMap(100, 100, 10, 30, .75);
   }
}
