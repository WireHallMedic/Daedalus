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
      int halfMoveDuration = GUIConstants.FRAMES_PER_SECOND;
      int pauseDuration = halfMoveDuration / 4;
      double yStep = 0.5 / halfMoveDuration;
      double[] yList = new double[(halfMoveDuration + pauseDuration) * 2];
      for(int i = 0; i < halfMoveDuration; i++)
      {
         double theta = Math.PI + (Math.PI * (i / (double)halfMoveDuration) * 2.0);
         yList[i] = -(yStep * ((Math.cos(theta) + 1) / 2));
         yList[pauseDuration + i + halfMoveDuration] = -yList[i];
      }
      script.setYMoveList(yList);
      script.setEndBehavior(AnimationScript.CENTER_TARGET + AnimationScript.LOOP);
      script.setNonTrackingMovement(true);
      return script;
   }
}