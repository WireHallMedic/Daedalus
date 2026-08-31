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
	private Vector<Integer> tileIndexList;
	private Vector<Integer> fgColorList;
	private Vector<Integer> bgColorList;
	private Vector<Integer> lowerTileIndexList;
	private Vector<Double> xMoveList;
	private Vector<Double> yMoveList;
	private Vector<Double> scaleList;
   private UnboundTile originalTile;


	public UnboundTile getTarget(){return target;}
	public int getEndBehavior(){return endBehavior;}
	public int getAge(){return age;}
	public Vector<Integer> getTileIndexList(){return tileIndexList;}
	public Vector<Integer> getFGColorList(){return fgColorList;}
	public Vector<Integer> getBGColorList(){return bgColorList;}
	public Vector<Integer> getLowerTileIndexList(){return lowerTileIndexList;}
	public Vector<Double> getXMoveList(){return xMoveList;}
	public Vector<Double> getYMoveList(){return yMoveList;}
	public Vector<Double> getScaleList(){return scaleList;}


	public void setTarget(UnboundTile t){target = t;}
	public void setEndBehavior(int e){endBehavior = e;}
	public void setAge(int a){age = a;}
	public void setTileIndexList(Vector<Integer> i){tileIndexList = i;}
	public void setFGColorList(Vector<Integer> f){fgColorList = f;}
	public void setBGColorList(Vector<Integer> b){bgColorList = b;}
	public void setLowerTileIndexList(Vector<Integer> l){lowerTileIndexList = l;}
	public void setXMoveList(Vector<Double> x){xMoveList = x;}
	public void setYMoveList(Vector<Double> y){yMoveList = y;}
	public void setScaleList(Vector<Double> s){scaleList = s;}


   public AnimationScript(UnboundTile _target)
   {
      target = _target;
      endBehavior = 0;
      age = -1;
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
         lifespan = Math.max(lifespan, tileIndexList.size());
      if(fgColorList != null)
         lifespan = Math.max(lifespan, fgColorList.size());
      if(bgColorList != null)
         lifespan = Math.max(lifespan, bgColorList.size());
      if(lowerTileIndexList != null)
         lifespan = Math.max(lifespan, lowerTileIndexList.size());
      if(xMoveList != null)
         lifespan = Math.max(lifespan, xMoveList.size());
      if(yMoveList != null)
         lifespan = Math.max(lifespan, yMoveList.size());
      if(scaleList != null)
         lifespan = Math.max(lifespan, scaleList.size());
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
         target.setTileIndex(tileIndexList.elementAt(age));
      if(fgColorList != null)
         target.setFGColor(fgColorList.elementAt(age));
      if(bgColorList != null)
         target.setBGColor(bgColorList.elementAt(age));
      if(lowerTileIndexList != null)
         target.setLowerTileIndex(lowerTileIndexList.elementAt(age));
      if(xMoveList != null)
         target.setXOffset(target.getXOffset() + xMoveList.elementAt(age));
      if(yMoveList != null)
         target.setYOffset(target.getYOffset() + yMoveList.elementAt(age));
      if(scaleList != null)
         target.setScale(scaleList.elementAt(age));
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
         target.setXOffset(0.0);
         target.setYOffset(0.0);
      }
   }
}

