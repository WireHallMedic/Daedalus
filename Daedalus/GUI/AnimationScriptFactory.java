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
}