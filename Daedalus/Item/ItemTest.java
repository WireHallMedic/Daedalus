package Daedalus.Item;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

import Daedalus.Engine.*;


public class ItemTest 
{


   /** Fixture initialization (common initialization for all tests). **/
   @Before public void setUp() {
   }


   /** A test that always fails. **/
   @Test public void testRollIgnoresOutOfLevelRange() 
   {
      TableItemWrapper ti1 = new TableItemWrapper(null, 1, 6, 10);
      TableItemWrapper ti2 = new TableItemWrapper(null, 3, 8, 10);
      TableItemWrapper ti3 = new TableItemWrapper(null, 5, 10, 10);
      
      Vector<TableItemWrapper> list = new Vector<TableItemWrapper>();
      list.add(ti1);
      list.add(ti2);
      list.add(ti3);
      
      RNG.setSeed(100);
      Assert.assertEquals("Level 5 roll returns ti3", ti3, RNG.roll(list, 5));
      RNG.setSeed(100);
      Assert.assertNotEquals("Level 4 roll does not", ti3, RNG.roll(list, 4));
      
      RNG.setSeed(101);
      Assert.assertEquals("Level 5 roll returns ti2", ti2, RNG.roll(list, 5));
      RNG.setSeed(101);
      Assert.assertNotEquals("Level 9 roll does not", ti2, RNG.roll(list, 9));
   }
}
