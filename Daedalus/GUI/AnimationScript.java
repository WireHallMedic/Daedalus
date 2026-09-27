package Daedalus.GUI;

import java.awt.*;
import java.util.*;

public class AnimationScript extends GroundAnimationScript
{
   public static final int LOOP = GroundAnimationScript.LOOP;
   public static final int EXPIRE_TARGET = 2;
   public static final int CENTER_TARGET = 4;
   
	private UnboundTile targetUnboundTile;
   private boolean nonTrackingMovement;   // non-tracking movement is ignored for centering screen on player
	private int[] lowerTileIndexList;
	private double[] xMoveList;
	private double[] yMoveList;
	private double[] scaleList;
   private UnboundTile originalUnboundTile;


	public UnboundTile getTarget(){return targetUnboundTile;}
   public boolean isNonTrackingMovement(){return nonTrackingMovement;}
	public int[] getLowerTileIndexList(){return lowerTileIndexList;}
	public double[] getXMoveList(){return xMoveList;}
	public double[] getYMoveList(){return yMoveList;}
	public double[] getScaleList(){return scaleList;}
   public Vector<ScriptListener> getScriptListenerList(){return scriptListenerList;}


	public void setTarget(UnboundTile t){targetUnboundTile = t; originalUnboundTile = targetUnboundTile.copy();}
   public void setNonTrackingMovement(boolean ntm){nonTrackingMovement = ntm;}
	public void setLowerTileIndexList(int[] l){lowerTileIndexList = l;}
	public void setXMoveList(double[] x){xMoveList = x;}
	public void setYMoveList(double[] y){yMoveList = y;}
	public void setScaleList(double[] s){scaleList = s;}
   public void setScriptListenerList(Vector<ScriptListener> sll){scriptListenerList = sll;}


   public AnimationScript(UnboundTile _target)
   {
      super(null);
      targetUnboundTile = _target;
   	lowerTileIndexList = null;
   	xMoveList = null;
   	yMoveList = null;
   	scaleList = null;
      if(targetUnboundTile != null)
         originalUnboundTile = targetUnboundTile.copy();
      else
         originalUnboundTile = null;
   }
   
   
   public int getLifespan()
   {
      int lifespan = super.getLifespan();
      if(lowerTileIndexList != null)
         lifespan = Math.max(lifespan, lowerTileIndexList.length);
      if(xMoveList != null)
         lifespan = Math.max(lifespan, xMoveList.length);
      if(yMoveList != null)
         lifespan = Math.max(lifespan, yMoveList.length);
      if(scaleList != null)
         lifespan = Math.max(lifespan, scaleList.length);
      return lifespan;
   }
   
   public void update()
   {
      age++;
      if(isExpired())
      {
         resolveEndBehavior();
      }
      
      if(!isExpired())
      {
         if(tileIndexList != null)
            targetUnboundTile.setTileIndex(tileIndexList[age]);
         if(fgColorList != null)
            targetUnboundTile.setFGColor(fgColorList[age]);
         if(bgColorList != null)
            targetUnboundTile.setBGColor(bgColorList[age]);
         if(lowerTileIndexList != null)
            targetUnboundTile.setLowerTileIndex(lowerTileIndexList[age]);
         if(scaleList != null)
            targetUnboundTile.setScale(scaleList[age]);
         if(nonTrackingMovement)
         {
            if(xMoveList != null)
               targetUnboundTile.setNonTrackingXOffset(targetUnboundTile.getNonTrackingXOffset() + xMoveList[age]);
            if(yMoveList != null)
               targetUnboundTile.setNonTrackingYOffset(targetUnboundTile.getNonTrackingYOffset() + yMoveList[age]);
         }
         else
         {
            if(xMoveList != null)
               targetUnboundTile.setXOffset(targetUnboundTile.getXOffset() + xMoveList[age]);
            if(yMoveList != null)
               targetUnboundTile.setYOffset(targetUnboundTile.getYOffset() + yMoveList[age]);
         }
      }
   }
   
   protected void resolveEndBehavior()
   {
      // put stuff back
      targetUnboundTile.setFGColor(originalUnboundTile.getFGColor());
      targetUnboundTile.setBGColor(originalUnboundTile.getBGColor());
      targetUnboundTile.setTileIndex(originalUnboundTile.getTileIndex());
      targetUnboundTile.setLowerTileIndex(originalUnboundTile.getLowerTileIndex());
      targetUnboundTile.setScale(originalUnboundTile.getScale());
   
      if((endBehavior & LOOP) > 0)
      {
         age = 0;
      }
      if((endBehavior & EXPIRE_TARGET) > 0)
      {
         targetUnboundTile.setExpired(true);
      }
      if((endBehavior & CENTER_TARGET) > 0)
      {
         if(nonTrackingMovement)
         {
            targetUnboundTile.setNonTrackingXOffset(0.0);
            targetUnboundTile.setNonTrackingYOffset(0.0);
         }
         else
         {
            targetUnboundTile.setXOffset(0.0);
            targetUnboundTile.setYOffset(0.0);
         }
      }
      
      if(isExpired())
         for(ScriptListener listener: scriptListenerList)
            listener.scriptExpiring(this);
   }
}

