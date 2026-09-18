package Daedalus.GUI;

import java.util.*;
import Daedalus.AI.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;

public class AnimationManager implements GUIConstants, AIConstants
{
   private static Vector<AnimationScript> lockingList = new Vector<AnimationScript>();
   private static Vector<AnimationScript> nonLockingList = new Vector<AnimationScript>();
   private static Vector<AnimationScript> semiLockingList = new Vector<AnimationScript>();
   private static BoardPanel boardPanel = null; // because we need to add visual effects from a bunch of other 
                                                // places, like AI. Set in BoardPanel constructor.
   private static double screenShakeX = 0.0;
   private static double screenShakeY = 0.0;
   private static int screenShakeDuration = 0;
   private static double screenShakeMaxDistance = 0.0;
   private static boolean slowBlink;
   private static boolean mediumBlink;
   private static boolean fastBlink;
   private static double slowPulse;
   private static double mediumPulse;
   private static double fastPulse;
   private static int blinkCounter;
   private static int rumbleCountdown = 0;
   private static int shakeCountdown = 0;
   private static int violentShakeCountdown = 0;
   
   public static void setBoardPanel(BoardPanel bp){boardPanel = bp;}
   
   public static void addLocking(AnimationScript as){lockingList.add(as);}
   public static void addNonLocking(AnimationScript as){nonLockingList.add(as);}
   public static void addSemiLocking(AnimationScript as){semiLockingList.add(as);}
   
   public static double getScreenShakeX(){return screenShakeX;}
   public static double getScreenShakeY(){return screenShakeY;}
   public static boolean isShakingScreen(){return screenShakeDuration > 0;}
   public static boolean getSlowBlink(){return slowBlink;}
   public static boolean getMediumBlink(){return mediumBlink;}
   public static boolean getFastBlink(){return fastBlink;}
   public static double getSlowPulse(){return slowPulse;}
   public static double getMediumPulse(){return mediumPulse;}
   public static double getFastPulse(){return fastPulse;}
   
   public static boolean isLocked()
   {
      return lockingList.size() > 0;
   }
   
   public static boolean isSemiLocked()
   {
      return isLocked() || semiLockingList.size() > 0;
   }
   
   public static void update()
   {
      updateList(lockingList);
      updateList(nonLockingList);
      updateList(semiLockingList);
      
      if(rumbleCountdown > 0)
      {
         rumbleCountdown--;
         if(rumbleCountdown == 0)
            setScreenRumble();
      }
      if(shakeCountdown > 0)
      {
         shakeCountdown--;
         if(shakeCountdown == 0)
            setScreenShake();
      }
      if(violentShakeCountdown > 0)
      {
         violentShakeCountdown--;
         if(violentShakeCountdown == 0)
            setViolentScreenShake();
      }
      
      // increment blinking
      blinkCounter++;
      if(blinkCounter == FRAMES_PER_SECOND)
         blinkCounter = 0;
      // slow blink and pulse
      if(blinkCounter % SLOW_BLINK_SPEED == 0)
         slowBlink = !slowBlink;
      if(slowBlink)
         slowPulse = SLOW_PULSE_STEP * (blinkCounter % SLOW_BLINK_SPEED);
      else
         slowPulse = 1.0 - (SLOW_PULSE_STEP * (blinkCounter % SLOW_BLINK_SPEED));
      // medium blink and pulse
      if(blinkCounter % MEDIUM_BLINK_SPEED == 0)
         mediumBlink = !mediumBlink;
      if(mediumBlink)
         mediumPulse = MEDIUM_PULSE_STEP * (blinkCounter % MEDIUM_BLINK_SPEED);
      else
         mediumPulse = 1.0 - (MEDIUM_PULSE_STEP * (blinkCounter % MEDIUM_BLINK_SPEED));
      // fast blink and pulse
      if(blinkCounter % FAST_BLINK_SPEED == 0)
         fastBlink = !fastBlink;
      if(fastBlink)
         fastPulse = FAST_PULSE_STEP * (blinkCounter % FAST_BLINK_SPEED);
      else
         fastPulse = 1.0 - (FAST_PULSE_STEP * (blinkCounter % FAST_BLINK_SPEED));
      
      if(isShakingScreen())
      {
         screenShakeX = ((2.0 * screenShakeMaxDistance) * RNG.nextDouble()) - screenShakeMaxDistance;
         screenShakeY = ((2.0 * screenShakeMaxDistance) * RNG.nextDouble()) - screenShakeMaxDistance;
         screenShakeDuration--;
      }
      else
      {
         screenShakeX = 0.0;
         screenShakeY = 0.0;
      }
   }
   
   private static void updateList(Vector<AnimationScript> list)
   {
      for(int i = 0; i < list.size(); i++)
      {
         list.elementAt(i).update();
         if(list.elementAt(i).isExpired())
         {
            list.removeElementAt(i);
            i--;
         }
      }
   }
   
   public static void clear()
   {
      lockingList = new Vector<AnimationScript>();
      nonLockingList = new Vector<AnimationScript>();
      semiLockingList = new Vector<AnimationScript>();
      screenShakeX = 0.0;
      screenShakeY = 0.0;
      screenShakeDuration = 0;
      rumbleCountdown = 0;
      shakeCountdown = 0;
      violentShakeCountdown = 0;
   }
   
   
   public static boolean isOnSemiLockingList(Actor a)
   {
      for(int i = 0; i < semiLockingList.size(); i++)
      {
         if(semiLockingList.elementAt(i).getTarget() == a)
            return true;
      }
      return false;
   }
   
   // returns false is animation prevents actor from starting turn, else true
   public static boolean isClearToAct(Actor a)
   {  
      // slow down if player is dead
      if(Game.getPlayer().isDead())
         return !isSemiLocked();
      // player must wait for semi-locked
      if(a == Game.getPlayer())
         return !isSemiLocked();
      // actors doing something other than delaying or stepping must wait for semi-locked
      if(!(a.getAI().getPendingAction() == ActorAction.DELAY ||
          a.getAI().getPendingAction() == ActorAction.STEP))
         return !isSemiLocked();
      // actors already on the semilocking list must wait
      if(isOnSemiLockingList(a))
         return !isSemiLocked();
      return !isLocked();
   }
   
   public static void removeTargetingScripts(UnboundTile target)
   {
      removeTargetingScriptsFromList(target, lockingList);
      removeTargetingScriptsFromList(target, nonLockingList);
      removeTargetingScriptsFromList(target, semiLockingList);
   }
   
   private static void removeTargetingScriptsFromList(UnboundTile target, Vector<AnimationScript> list)
   {
      for(int i = 0; i < list.size(); i++)
      {
         if(list.elementAt(i).getTarget() == target)
         {
            list.removeElementAt(i);
            i--;
         }
      }
   }
   
   public static void addToBoardPanel(UnboundTile ut)
   {
      if(boardPanel != null)
         boardPanel.addUnboundTile(ut);
   }
   
   
   public static void setScreenShake(double maxDist, int durationInTicks)
   {
      screenShakeDuration = durationInTicks;
      screenShakeMaxDistance = maxDist;
   }
   
   public static void setScreenRumble()
   {
      setScreenShake(.15, GUIConstants.FRAMES_PER_SECOND / 6);
   }
   
   public static void setScreenRumble(int delay)
   {
      rumbleCountdown = delay;
   }
   
   public static void setScreenShake()
   {
      setScreenShake(.5, GUIConstants.FRAMES_PER_SECOND / 3);
   }
   
   public static void setScreenShake(int delay)
   {
      shakeCountdown = delay;
   }
   
   public static void setViolentScreenShake()
   {
      setScreenShake(1.0, GUIConstants.FRAMES_PER_SECOND / 2);
   }
   
   public static void setViolentScreenShake(int delay)
   {
      violentShakeCountdown = delay;
   }
   
}