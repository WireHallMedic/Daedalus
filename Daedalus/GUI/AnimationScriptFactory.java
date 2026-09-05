package Daedalus.GUI;

import java.awt.*;
import java.util.*;
import Daedalus.Zone.*;
import Daedalus.Item.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;
import WidlerSuite.Vect;

public class AnimationScriptFactory implements ZoneConstants, GUIConstants
{
   public static AnimationScript getStep(UnboundTile target, Direction dir)
   {
      AnimationScript script = new AnimationScript(target);
      int scriptDuration = GUIConstants.FRAMES_PER_SECOND / 4;
      double xStep = (1.0 / scriptDuration) * dir.x;
      double yStep = (1.0 / scriptDuration) * dir.y;
      double[] xList = new double[scriptDuration];
      double[] yList = new double[scriptDuration];
      for(int i = 0; i < scriptDuration; i++)
      {
         xList[i] = xStep;
         yList[i] = yStep;
      }
      script.setXMoveList(xList);
      script.setYMoveList(yList);
      script.setEndBehavior(AnimationScript.CENTER_TARGET);
      return script;
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
         UnboundTile ut = new UnboundTile(SQUARE_PALETTE, '*', YELLOW, TRANSPARENT);
         ut.setTileLoc(loc);
         AnimationScript as = AnimationScriptFactory.getExplosionParticleAnimation(ut, i, baseDist);
         AnimationManager.addToBoardPanel(ut);
         AnimationManager.addNonLocking(as);
      }
   }
   public static void addExplosion(int x, int y){addExplosion(new Coord(x, y));}
   
   
   
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
      //script.setScaleList(getDoubleGradient(.5, 1.5, duration));
      script.setFGColorList(getColorGradient(EXPLOSION_YELLOW, EXPLOSION_RED, duration));
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
}