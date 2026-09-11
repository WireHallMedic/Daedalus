package Daedalus.GUI;

import java.awt.*;
import java.util.*;
import Daedalus.Zone.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;
import WidlerSuite.Vect;
import WidlerSuite.WSFontConstants;

public class AnimationScriptFactory implements ZoneConstants, GUIConstants
{
   private static final int MELEE_PHASE_DURATION = GUIConstants.FRAMES_PER_SECOND / 10;
   public static final int MELEE_IMPACT_DELAY = MELEE_PHASE_DURATION * 2;
   
   
   public static AnimationScript getStep(UnboundTile target, Direction dir)
   {
      return getOneTileMove(target, dir, GUIConstants.FRAMES_PER_SECOND / 4);
   }
   
   
   public static AnimationScript getKnockback(UnboundTile target, Direction dir)
   {
      return getOneTileMove(target, dir, GUIConstants.FRAMES_PER_SECOND / 10);
   }
   
   
   public static AnimationScript getFloat(UnboundTile target)
   {  
      AnimationScript script = new AnimationScript(target);
      int halfDuration = GUIConstants.FRAMES_PER_SECOND;
      double yStep = 0.25 / halfDuration;
      double[] yList = new double[halfDuration * 2];
      for(int i = 0; i < halfDuration; i++)
      {
         yList[i] = -yStep;
         yList[i + halfDuration] = yStep;
      }
      script.setYMoveList(yList);
      script.setEndBehavior(AnimationScript.CENTER_TARGET + AnimationScript.LOOP);
      script.setNonTrackingMovement(true);
      return script;
   }
   
   // direction is direction to attacker
   public static AnimationScript getImpact(UnboundTile target, Direction dir)
   {
      AnimationScript script = new AnimationScript(target);
      int phaseDuration = GUIConstants.FRAMES_PER_SECOND / 20;
      double xStep = (0.5 / phaseDuration) * dir.opposite().x;
      double yStep = (0.5 / phaseDuration) * dir.opposite().y;
      double[] xList = new double[phaseDuration * 3];
      double[] yList = new double[phaseDuration * 3];
      for(int i = 0; i < phaseDuration; i++)
      {
         xList[i] = xStep;
         yList[i] = yStep;
         xList[i + phaseDuration] = -xStep / 2;
         yList[i + phaseDuration] = -yStep / 2;
         xList[i + (2 * phaseDuration)] = -xStep / 2;
         yList[i + (2 * phaseDuration)] = -yStep / 2;
      }
      script.setXMoveList(xList);
      script.setYMoveList(yList);
      script.setEndBehavior(AnimationScript.CENTER_TARGET);
      script.setNonTrackingMovement(true);
      return script;
   }
   
   // direction is direction to target direction
   public static AnimationScript getRecoil(UnboundTile target, Direction dir)
   {
      return getImpact(target, dir);
   }
   
   // direction is direction to target
   public static AnimationScript getMeleeAttack(UnboundTile target, Direction dir)
   {
      AnimationScript script = new AnimationScript(target);
      int phaseDuration = MELEE_PHASE_DURATION;
      double xStep = (0.75 / phaseDuration) * dir.x / 3.0;
      double yStep = (0.75 / phaseDuration) * dir.y / 3.0;
      double[] xList = new double[phaseDuration * 3];
      double[] yList = new double[phaseDuration * 3];
      for(int i = 0; i < phaseDuration; i++)
      {
         xList[i] = -xStep;
         yList[i] = -yStep;
         xList[i + phaseDuration] = xStep * 3;
         yList[i + phaseDuration] = yStep * 3;
         xList[i + (2 * phaseDuration)] = -xStep;
         yList[i + (2 * phaseDuration)] = -yStep;
      }
      script.setXMoveList(xList);
      script.setYMoveList(yList);
      script.setEndBehavior(AnimationScript.CENTER_TARGET);
      script.setNonTrackingMovement(true);
      return script;
   }   
   
   // direction is direction to attacker
   public static AnimationScript getMeleeImpact(UnboundTile target, Direction dir)
   {
      AnimationScript script = getImpact(target, dir);
      script.setXMoveList(prepend(0.0, MELEE_IMPACT_DELAY, script.getXMoveList()));
      script.setYMoveList(prepend(0.0, MELEE_IMPACT_DELAY, script.getYMoveList()));
      return script;
   }
   
   public static AnimationScript getPickupEffect(UnboundTile target)
   {
      AnimationScript script = new AnimationScript(target);
      int duration = GUIConstants.FRAMES_PER_SECOND / 2;
      double[] yList = new double[duration];
      double yStep = -0.5 / duration;
      for(int i = 0; i < duration; i++)
      {
         yList[i] = yStep;
      }
      script.setYMoveList(yList);
      script.setEndBehavior(AnimationScript.EXPIRE_TARGET);
      return script;
   }
   
   public static AnimationScript getFloatStringEffect(UnboundTile target)
   {
      AnimationScript script = new AnimationScript(target);
      int duration = GUIConstants.FRAMES_PER_SECOND;
      double[] yList = new double[duration];
      double yStep = -1.0 / duration;
      for(int i = 0; i < duration; i++)
      {
         yList[i] = yStep;
      }
      script.setYMoveList(yList);
      script.setEndBehavior(AnimationScript.EXPIRE_TARGET);
      return script;
   }
   
   public static AnimationScript getShieldParticleScript(UnboundTile target)
   {
      AnimationScript script = new AnimationScript(target);
      int duration = (int)((GUIConstants.FRAMES_PER_SECOND / 4) * (1.0 + RNG.nextDouble()));
      double[] xList = new double[duration];
      double[] yList = new double[duration];
      double yStep = (((RNG.nextDouble() * 2.0) - 1.0) / GUIConstants.FRAMES_PER_SECOND) * 4;
      double xStep = (((RNG.nextDouble() * 2.0) - 1.0) / GUIConstants.FRAMES_PER_SECOND) * 4;
      for(int i = 0; i < duration; i++)
      {
         xList[i] = xStep;
         yList[i] = yStep;
      }
      script.setXMoveList(xList);
      script.setYMoveList(yList);
      script.setEndBehavior(AnimationScript.EXPIRE_TARGET);
      return script;
   }
   
   // adders. Create and add to boardpanel and animationmanager
   ///////////////////////////////////////////////////////////////////////////////
   
   // creates pickup unboundTile and script, and adds them to boardPanel and 
   public static void addPickupEffect(Item item, Coord loc)
   {
      UnboundTile ut = item.getUnboundTile(loc);
      ut.setYOffset(-.5);
      AnimationScript as = getPickupEffect(ut);
      AnimationManager.addToBoardPanel(ut);
      AnimationManager.addNonLocking(as);
   }
   public static void addPickupEffect(Item item, int x, int y){addPickupEffect(item, new Coord(x, y));}
   
   
   public static void addExplosion(Coord loc)
   {
      double baseDist = 1.0;
      for(int i = 0; i < 16; i++)
      {
         if(i == 8)
            baseDist /= 2.0;
         UnboundTile ut = new UnboundTile(SQUARE_PALETTE, '*', VIVID_YELLOW, TRANSPARENT);
         ut.setTileLoc(loc);
         AnimationScript as = AnimationScriptFactory.getExplosionParticleAnimation(ut, i, baseDist);
         AnimationManager.addToBoardPanel(ut);
         AnimationManager.addNonLocking(as);
      }
   }
   public static void addExplosion(int x, int y){addExplosion(new Coord(x, y));}
   
   
   public static void addFloatString(String str, Coord loc, int fgColor)
   {  
      UnboundString floatStr = new UnboundString(str);
      floatStr.setTileLoc(loc);
      floatStr.setYOffset(-.5);
      floatStr.setFGColor(fgColor);
      
      AnimationScript as = getFloatStringEffect(floatStr);
      AnimationManager.addNonLocking(as);
      AnimationManager.addToBoardPanel(floatStr);
   }
   public static void addFloatStringEffect(String str, int x, int y, int fgColor){addFloatString(str, new Coord(x, y), fgColor);}
   
   public static void addShieldParticles(Coord loc)
   {
      for(int i = 0; i < 12; i++)
      {
         UnboundTile ut = new UnboundTile(SQUARE_PALETTE, WSFontConstants.SMALL_BULLET_TILE, SHIELD_COLOR, TRANSPARENT);         
         ut.setTileLoc(loc);
         AnimationScript as = AnimationScriptFactory.getShieldParticleScript(ut);
         AnimationManager.addToBoardPanel(ut);
         AnimationManager.addNonLocking(as);
      }
   }
   public static void addShieldParticles(Actor a){addShieldParticles(a.getTileLoc());}
   
   // private methods
   /////////////////////////////////////////////////////////////
   
   private static AnimationScript getExplosionParticleAnimation(UnboundTile target, int rotation, double baseTravelDistance)
   {
      AnimationScript script = new AnimationScript(target);
      int duration = GUIConstants.FRAMES_PER_SECOND / 3;
      duration += (GUIConstants.FRAMES_PER_SECOND / 6) * RNG.nextDouble();
      double travelDist = baseTravelDistance + (RNG.nextDouble() * (baseTravelDistance / 2.0));
      double angle = (Math.PI / 4.0) * (double)rotation;
      double angleVariation = Math.PI / 8;
      angle += RNG.nextDouble() * angleVariation;
      angle -= RNG.nextDouble() * angleVariation;
      Vect vect = new Vect(angle, travelDist);
      double xStep = vect.getXAsDouble() / duration;
      double yStep = vect.getYAsDouble() / duration;
      double[] xList = new double[duration];
      double[] yList = new double[duration];
      for(int i = 0; i < duration; i++)
      {
         xList[i] = xStep;
         yList[i] = yStep;
      }
      script.setXMoveList(xList);
      script.setYMoveList(yList);
      int color1 = VIVID_YELLOW;
      int color2 = VIVID_RED;
      if(RNG.nextBoolean())
      {
         color1 = VIVID_RED;
         color2 = VIVID_YELLOW;
      }
      // set to alternate red and yellow every tenth of a second, randomizing which it starts on
      int timeIncrement = FRAMES_PER_SECOND / 10;
      int[] fgColor = new int[duration];
      for(int i = 0; i < duration; i++)
      {
         if((i / timeIncrement) % 2 == 0)
            fgColor[i] = color1;
         else
            fgColor[i] = color2;
      }
      script.setFGColorList(fgColor);
      script.setEndBehavior(AnimationScript.EXPIRE_TARGET);
      return script;
   }
   
   private static double[] getDoubleGradient(double start, double end, int length)
   {
      double[] gradient = new double[length];
      double curVal = start;
      double incrementAmt = (end - start) / length;
      for(int i = 0; i < length; i++)
      {
         gradient[i] = curVal;
         curVal += incrementAmt;
      }
      return gradient;
   }
   
   private static int[] getColorGradient(int start, int end, int length)
   {
      int[] gradient = new int[length];
      int startRed = new Color(start).getRed();
      int startGreen = new Color(start).getGreen();
      int startBlue = new Color(start).getBlue();
      int endRed = new Color(end).getRed();
      int endGreen = new Color(end).getGreen();
      int endBlue = new Color(end).getBlue();
      int redStep = (endRed - startRed) / length;
      int greenStep = (endGreen - startGreen) / length;
      int blueStep = (endBlue - startBlue) / length;

      for(int i = 0; i < length; i++)
      {
         gradient[i] = new Color(startRed + (redStep * i), startGreen + (greenStep * i), startBlue + (blueStep * i)).getRGB();
      }
      return gradient;
   }
   
   private static double[] prepend(double val, int count, double[] original)
   {
      double[] newArray = new double[count + original.length];
      for(int i = 0; i < count; i++)
         newArray[i] = val;
      for(int i = 0; i < original.length; i++)
         newArray[count + i] = original[i];
      return newArray;
   }
   
   public static AnimationScript getOneTileMove(UnboundTile target, Direction dir, int duration)
   {
      AnimationScript script = new AnimationScript(target);
      double xStep = (1.0 / duration) * dir.x;
      double yStep = (1.0 / duration) * dir.y;
      double[] xList = new double[duration];
      double[] yList = new double[duration];
      for(int i = 0; i < duration; i++)
      {
         xList[i] = xStep;
         yList[i] = yStep;
      }
      script.setXMoveList(xList);
      script.setYMoveList(yList);
      script.setEndBehavior(AnimationScript.CENTER_TARGET);
      return script;
   }
   
   
   
   // testing
   //////////////////////////////////////////////////
   public static void addTestEffect()
   {
      addExplosion(Game.getPlayer().getTileLoc());
      AnimationManager.setScreenShake();
      //addFloatString("Snort", Game.getPlayer().getTileLoc(), LIGHT_GREEN);
   }
}