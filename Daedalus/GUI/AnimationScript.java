package Daedalus.GUI;

import WidlerSuite.WSFontConstants;
import java.awt.*;
import java.util.*;

public class AnimationScript
{
   public static final int LOOP = 1;
   public static final int EXPIRE_TARGET = 2;
   public static final int CENTER_TARGET = 4;
   
	private UnboundTile target;
	private int endBehavior;
	private int age;
   private boolean nonTrackingMovement;   // non-tracking movement is ignored for centering screen on player
	private int[] tileIndexList;
	private int[] fgColorList;
	private int[] bgColorList;
	private int[] lowerTileIndexList;
	private double[] xMoveList;
	private double[] yMoveList;
	private double[] scaleList;
   private UnboundTile originalTile;


	public UnboundTile getTarget(){return target;}
	public int getEndBehavior(){return endBehavior;}
	public int getAge(){return age;}
   public boolean isNonTrackingMovement(){return nonTrackingMovement;}
	public int[] getTileIndexList(){return tileIndexList;}
	public int[] getFGColorList(){return fgColorList;}
	public int[] getBGColorList(){return bgColorList;}
	public int[] getLowerTileIndexList(){return lowerTileIndexList;}
	public double[] getXMoveList(){return xMoveList;}
	public double[] getYMoveList(){return yMoveList;}
	public double[] getScaleList(){return scaleList;}


	public void setTarget(UnboundTile t){target = t;}
	public void setEndBehavior(int e){endBehavior = e;}
	public void setAge(int a){age = a;}
   public void setNonTrackingMovement(boolean ntm){nonTrackingMovement = ntm;}
	public void setTileIndexList(int[] i){tileIndexList = i;}
	public void setFGColorList(int[] f){fgColorList = f;}
	public void setBGColorList(int[] b){bgColorList = b;}
	public void setLowerTileIndexList(int[] l){lowerTileIndexList = l;}
	public void setXMoveList(double[] x){xMoveList = x;}
	public void setYMoveList(double[] y){yMoveList = y;}
	public void setScaleList(double[] s){scaleList = s;}


   public AnimationScript(UnboundTile _target)
   {
      target = _target;
      endBehavior = 0;
      age = -1;
      nonTrackingMovement = false;
      tileIndexList = null;
   	fgColorList = null;
   	bgColorList = null;
   	lowerTileIndexList = null;
   	xMoveList = null;
   	yMoveList = null;
   	scaleList = null;
      originalTile = target.copy();
   }
   
   public int getLifespan()
   {
      int lifespan = 0;
      if(tileIndexList != null)
         lifespan = Math.max(lifespan, tileIndexList.length);
      if(fgColorList != null)
         lifespan = Math.max(lifespan, fgColorList.length);
      if(bgColorList != null)
         lifespan = Math.max(lifespan, bgColorList.length);
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
   
   public boolean isExpired()
   {
      return age >= getLifespan();
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
            target.setTileIndex(tileIndexList[age]);
         if(fgColorList != null)
            target.setFGColor(fgColorList[age]);
         if(bgColorList != null)
            target.setBGColor(bgColorList[age]);
         if(lowerTileIndexList != null)
            target.setLowerTileIndex(lowerTileIndexList[age]);
         if(scaleList != null)
            target.setScale(scaleList[age]);
         if(nonTrackingMovement)
         {
            if(xMoveList != null)
               target.setNonTrackingXOffset(target.getNonTrackingXOffset() + xMoveList[age]);
            if(yMoveList != null)
               target.setNonTrackingYOffset(target.getNonTrackingYOffset() + yMoveList[age]);
         }
         else
         {
            if(xMoveList != null)
               target.setXOffset(target.getXOffset() + xMoveList[age]);
            if(yMoveList != null)
               target.setYOffset(target.getYOffset() + yMoveList[age]);
         }
      }
   }
   
   private void resolveEndBehavior()
   {
      // put stuff back
      target.setFGColor(originalTile.getFGColor());
      target.setBGColor(originalTile.getBGColor());
      target.setTileIndex(originalTile.getTileIndex());
      target.setLowerTileIndex(originalTile.getLowerTileIndex());
      target.setScale(originalTile.getScale());
   
      if((endBehavior & LOOP) > 0)
      {
         age = 0;
      }
      if((endBehavior & EXPIRE_TARGET) > 0)
      {
         target.setExpired(true);
      }
      if((endBehavior & CENTER_TARGET) > 0)
      {
         if(nonTrackingMovement)
         {
            target.setNonTrackingXOffset(0.0);
            target.setNonTrackingYOffset(0.0);
         }
         else
         {
            target.setXOffset(0.0);
            target.setYOffset(0.0);
         }
      }
   }
}

