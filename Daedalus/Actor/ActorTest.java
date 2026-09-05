package Daedalus.Actor;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class ActorTest implements ActorConstants
{
   private Actor a;

   /** Fixture initialization (common initialization for all tests). **/
   @Before public void setUp() 
   {
      a = new Actor();
   }


   @Test public void testSpeedArithmetic() 
   {
      Assert.assertEquals("Base speed is NORMAL", ActionSpeed.NORMAL, a.getMoveSpeed());
      StatBlock modBlock = new StatBlock();
      modBlock.setMoveSpeed(ActionSpeed.FAST);
      a.getBaseStats().add(modBlock);
      Assert.assertEquals("Base speed + 1 is FAST", ActionSpeed.FAST, a.getMoveSpeed());
      a.getBaseStats().add(modBlock);
      Assert.assertEquals("Base speed + 2 is FAST", ActionSpeed.FAST, a.getMoveSpeed());
      modBlock.setMoveSpeed(ActionSpeed.SLOW);
      a.getBaseStats().add(modBlock);
      Assert.assertEquals("FAST + 1 - 1 is FAST", ActionSpeed.FAST, a.getMoveSpeed());
      a.getBaseStats().add(modBlock);
      Assert.assertEquals("FAST + 1 - 2 is NORMAL", ActionSpeed.NORMAL, a.getMoveSpeed());
      a.getBaseStats().add(modBlock);
      Assert.assertEquals("FAST + 1 - 3 is SLOW", ActionSpeed.SLOW, a.getMoveSpeed());
      a.getBaseStats().add(modBlock);
      Assert.assertEquals("FAST + 1 - 4 is SLOW", ActionSpeed.SLOW, a.getMoveSpeed());
   }


   @Test public void testInstantenousSpeeds() 
   {
      // move
      Assert.assertEquals("Base speed is NORMAL", ActionSpeed.NORMAL, a.getMoveSpeed());
      StatBlock modBlock = new StatBlock();
      modBlock.setMoveSpeed(ActionSpeed.INSTANTANEOUS);
      a.getBaseStats().add(modBlock);
      Assert.assertEquals("Base speed + INSTANTANEOUS is INSTANTANEOUS", ActionSpeed.INSTANTANEOUS, a.getMoveSpeed());
      modBlock.setMoveSpeed(ActionSpeed.SLOW);
      a.getBaseStats().setMoveSpeed(ActionSpeed.INSTANTANEOUS);
      a.getBaseStats().add(modBlock);
      Assert.assertEquals("INSTANTANEOUS Base speed + SLOW is INSTANTANEOUS", ActionSpeed.INSTANTANEOUS, a.getMoveSpeed());
      
      // action
      Assert.assertEquals("Base speed is NORMAL", ActionSpeed.NORMAL, a.getAttackSpeed());
      modBlock = new StatBlock();
      modBlock.setAttackSpeed(ActionSpeed.INSTANTANEOUS);
      a.getBaseStats().add(modBlock);
      Assert.assertEquals("Base speed + INSTANTANEOUS is INSTANTANEOUS", ActionSpeed.INSTANTANEOUS, a.getAttackSpeed());
      modBlock.setAttackSpeed(ActionSpeed.SLOW);
      a.getBaseStats().setAttackSpeed(ActionSpeed.INSTANTANEOUS);
      a.getBaseStats().add(modBlock);
      Assert.assertEquals("INSTANTANEOUS Base speed + SLOW is INSTANTANEOUS", ActionSpeed.INSTANTANEOUS, a.getAttackSpeed());
      
      // interact
      Assert.assertEquals("Base speed is NORMAL", ActionSpeed.NORMAL, a.getInteractSpeed());
      modBlock = new StatBlock();
      modBlock.setInteractSpeed(ActionSpeed.INSTANTANEOUS);
      a.getBaseStats().add(modBlock);
      Assert.assertEquals("Base speed + INSTANTANEOUS is INSTANTANEOUS", ActionSpeed.INSTANTANEOUS, a.getInteractSpeed());
      modBlock.setInteractSpeed(ActionSpeed.SLOW);
      a.getBaseStats().setInteractSpeed(ActionSpeed.INSTANTANEOUS);
      a.getBaseStats().add(modBlock);
      Assert.assertEquals("INSTANTANEOUS Base speed + SLOW is INSTANTANEOUS", ActionSpeed.INSTANTANEOUS, a.getInteractSpeed());
   }
}
