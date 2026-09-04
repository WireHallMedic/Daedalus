package Daedalus.GUI;

import java.awt.*;
import java.util.*;
import Daedalus.Zone.*;

public class AnimationScriptFactory implements ZoneConstants
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
}