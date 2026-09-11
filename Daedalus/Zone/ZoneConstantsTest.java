package Daedalus.Zone;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class ZoneConstantsTest implements ZoneConstants
{


   /** Fixture initialization (common initialization for all tests). **/
   @Before public void setUp() {
   }


   
   @Test public void testOpposite() 
   {
      Assert.assertEquals("Opposite of north is south",           Direction.SOUTH, Direction.NORTH.opposite());
      Assert.assertEquals("Opposite of northeast is southwest",   Direction.SOUTH_WEST, Direction.NORTH_EAST.opposite());
      Assert.assertEquals("Opposite of east is west",             Direction.WEST, Direction.EAST.opposite());
      Assert.assertEquals("Opposite of southeast is northwest",   Direction.NORTH_WEST, Direction.SOUTH_EAST.opposite());
      Assert.assertEquals("Opposite of south is north",           Direction.NORTH, Direction.SOUTH.opposite());
      Assert.assertEquals("Opposite of southwest is northeast",   Direction.NORTH_EAST, Direction.SOUTH_WEST.opposite());
      Assert.assertEquals("Opposite of west is east",             Direction.EAST, Direction.WEST.opposite());
      Assert.assertEquals("Opposite of northwest is southeast",   Direction.SOUTH_EAST, Direction.NORTH_WEST.opposite());
      Assert.assertEquals("Opposite of origin is origin",         Direction.ORIGIN, Direction.ORIGIN.opposite());
   }
   
   @Test public void testClockwise() 
   {
      Assert.assertEquals("Clockwise of north is northeast",   Direction.NORTH_EAST, Direction.NORTH.nextClockwise());
      Assert.assertEquals("Clockwise of northeast is east",    Direction.EAST, Direction.NORTH_EAST.nextClockwise());
      Assert.assertEquals("Clockwise of east is southeast",    Direction.SOUTH_EAST, Direction.EAST.nextClockwise());
      Assert.assertEquals("Clockwise of southeast is south",   Direction.SOUTH, Direction.SOUTH_EAST.nextClockwise());
      Assert.assertEquals("Clockwise of south is southwest",   Direction.SOUTH_WEST, Direction.SOUTH.nextClockwise());
      Assert.assertEquals("Clockwise of southwest is west",    Direction.WEST, Direction.SOUTH_WEST.nextClockwise());
      Assert.assertEquals("Clockwise of west is northwest",    Direction.NORTH_WEST, Direction.WEST.nextClockwise());
      Assert.assertEquals("Clockwise of northwest is north",   Direction.NORTH, Direction.NORTH_WEST.nextClockwise());
      Assert.assertEquals("Clockwise of origin is origin",     Direction.ORIGIN, Direction.ORIGIN.nextClockwise());
   }
   
   @Test public void testCounterclockwise() 
   {
      Assert.assertEquals("Counterclockwise of north is northwest",   Direction.NORTH_WEST, Direction.NORTH.prevClockwise());
      Assert.assertEquals("Counterclockwise of northeast is north",   Direction.NORTH, Direction.NORTH_EAST.prevClockwise());
      Assert.assertEquals("Counterclockwise of east is northeast",    Direction.NORTH_EAST, Direction.EAST.prevClockwise());
      Assert.assertEquals("Counterclockwise of southeast is east",    Direction.EAST, Direction.SOUTH_EAST.prevClockwise());
      Assert.assertEquals("Counterclockwise of south is southeast",   Direction.SOUTH_EAST, Direction.SOUTH.prevClockwise());
      Assert.assertEquals("Counterclockwise of southwest is south",   Direction.SOUTH, Direction.SOUTH_WEST.prevClockwise());
      Assert.assertEquals("Counterclockwise of west is southwest",    Direction.SOUTH_WEST, Direction.WEST.prevClockwise());
      Assert.assertEquals("Counterclockwise of northwest is west",    Direction.WEST, Direction.NORTH_WEST.prevClockwise());
      Assert.assertEquals("Counterclockwise of origin is origin",     Direction.ORIGIN, Direction.ORIGIN.prevClockwise());
   }
}
