package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import Daedalus.GUI.*;

public class ToggleTile extends ZoneTile implements ZoneConstants, GUIConstants
{
	protected ZoneTile aState;
	protected ZoneTile bState;
   protected boolean curState;

   public ToggleTile(TileBase base1, TileBase base2)
   {
      super(base1);
      aState = new ZoneTile(base1);
      bState = new ZoneTile(base2);
      curState = true;
   }
   
   public void toggle()
   {
      curState = !curState;
   }
   
   public ZoneTile getCurState()
   {
      if(curState) 
         return aState; 
      else 
         return bState;
   }
   
   public void setAState(ZoneTile zt)
   {
      aState = zt;
   }
   
   public void setBState(ZoneTile zt)
   {
      bState = zt;
   }
   
   @Override
   public BufferedImage getImage()
   {
      return getCurState().getImage();
   }

   // pass through getters to current state
   @Override public boolean isLowPassable(){return getCurState().isLowPassable();}
	@Override public boolean isHighPassable(){return getCurState().isHighPassable();}
	@Override public boolean isTransparent(){return getCurState().isTransparent();}
   @Override public int getFGColor(){return getCurState().getFGColor();}
	@Override public int getBGColor(){return getCurState().getBGColor();}
	@Override public int getTileIndex(){return getCurState().getTileIndex();}
   @Override public int getLowerTileIndex(){return getCurState().getLowerTileIndex();}
   
   // setting colors applies to both states
	@Override public void setFGColor(int f){aState.setFGColor(f); bState.setFGColor(f);}
	@Override public void setBGColor(int b){aState.setBGColor(b); bState.setBGColor(b);}
	@Override public void setPalette(TilePalette p){aState.setPalette(p); bState.setPalette(p);}
   
}