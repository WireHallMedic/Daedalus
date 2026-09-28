package Daedalus.GUI;

import Daedalus.Engine.*;
import java.awt.*;
import java.util.*;

public class GroundAnimationScript
{
   public static final int LOOP = 1;
   
	protected ImageTile targetTile;
	protected int endBehavior;
	protected int age;
	protected int[] tileIndexList;
	protected int[] fgColorList;
	protected int[] bgColorList;
   protected ImageTile originalTile;
   protected Vector<ScriptListener> scriptListenerList;


	public ImageTile getTargetTile(){return targetTile;}
	public int getEndBehavior(){return endBehavior;}
	public int getAge(){return age;}
	public int[] getTileIndexList(){return tileIndexList;}
	public int[] getFGColorList(){return fgColorList;}
	public int[] getBGColorList(){return bgColorList;}
   public Vector<ScriptListener> getScriptListenerList(){return scriptListenerList;}


	public void setTargetTile(ImageTile t){targetTile = t; originalTile = targetTile.copy();}
	public void setEndBehavior(int e){endBehavior = e;}
	public void setAge(int a){age = a;}
	public void setTileIndexList(int[] i){tileIndexList = i;}
	public void setFGColorList(int[] f){fgColorList = f;}
	public void setBGColorList(int[] b){bgColorList = b;}
   public void setScriptListenerList(Vector<ScriptListener> sll){scriptListenerList = sll;}


   public GroundAnimationScript(ImageTile _target)
   {
      targetTile = _target;
      endBehavior = 0;
      age = -1;
      tileIndexList = null;
   	fgColorList = null;
   	bgColorList = null;
      scriptListenerList = new Vector<ScriptListener>();
      if(targetTile != null)
         originalTile = targetTile.copy();
      else
         originalTile = null;
   }
   
   public void addScriptListener(ScriptListener sl)
   {
      scriptListenerList.add(sl);
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
            targetTile.setTileIndex(tileIndexList[age]);
         if(fgColorList != null)
            targetTile.setFGColor(fgColorList[age]);
         if(bgColorList != null)
            targetTile.setBGColor(bgColorList[age]);
      }
   }
   
   protected void resolveEndBehavior()
   {
      // put stuff back
      targetTile.setFGColor(originalTile.getFGColor());
      targetTile.setBGColor(originalTile.getBGColor());
      targetTile.setTileIndex(originalTile.getTileIndex());
   
      if((endBehavior & LOOP) > 0)
      {
         age = 0;
      }
      
      if(isExpired())
         for(ScriptListener listener: scriptListenerList)
            listener.scriptExpiring(this);
   }
}