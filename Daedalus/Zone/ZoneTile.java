package Daedalus.Zone;

import java.awt.*;
import Daedalus.GUI.*;

public class ZoneTile extends ImageTile implements ZoneConstants, GUIConstants
{
	protected boolean lowPassable;
	protected boolean highPassable;
	protected boolean transparent;
   protected int decorationType;
   protected Durability durability;
   protected boolean liquid;


	public boolean isLowPassable(){return lowPassable;}
	public boolean isHighPassable(){return highPassable;}
	public boolean isTransparent(){return transparent;}
   public int getDecorationType(){return decorationType;}
   public Durability getDurability(){return durability;}
   public boolean isLiquid(){return liquid;}


	public void setLowPassable(boolean l){lowPassable = l;}
	public void setHighPassable(boolean h){highPassable = h;}
	public void setTransparent(boolean t){transparent = t;}
   public void setDecorationType(int d){decorationType = d;}
   public void setDurability(Durability d){durability = d;}
   public void setLiquid(boolean l){liquid = l;}

   public ZoneTile(TileBase base)
   {
      super(SQUARE_PALETTE);
      set(base, WHITE, BLACK);
      decorationType = 0;
   }
   
   public ZoneTile(ZoneTile that)
   {
      super(that);
      this.lowPassable = that.lowPassable;
      this.highPassable = that.highPassable;
      this.transparent = that.transparent;
      this.decorationType = that.decorationType;
      this.name = that.name;
      this.durability = that.durability;
      this.liquid = that.liquid;
   }
   
   public ZoneTile copy()
   {
      return new ZoneTile(this);
   }
   
   public ZoneTile getBroken()
   {
      ZoneTile z = new ZoneTile(TileBase.ROUGH);
      z.setName("Rubble");
      z.setFGColor(this.getFGColor());
      z.setBGColor(this.getBGColor());
      return z;
   }
   
   public void set(TileBase base, int fg, int bg, int dt)
   {
      set(base);
      fgColor = fg;
      bgColor = bg;
      decorationType = dt;
   }
   public void set(TileBase base, int fg, int bg){set(base, fg, bg, 0);}
   
   public void set(TileBase base)
   {
      setTileIndex(base.tileIndex);
      lowPassable = base.lowPassable;
      highPassable = base.highPassable;
      transparent = base.transparent;
      name = base.name;
      switch(base)
      {
         case CLEAR: 
         case PATH: 
         case DEEP_LIQUID: 
         case SHALLOW_LIQUID: 
         case SWITCH: 
         case FLIPPED_SWITCH: 
         case ROUGH: 
         case TERMINAL: 
         case EXIT:        setDurability(Durability.UNBREAKABLE); break;
         default :         durability = Durability.STANDARD; break;
      }
      switch(base)
      {
         case DEEP_LIQUID: 
         case SHALLOW_LIQUID: setLiquid(true); break;
         default:             setLiquid(false); break;
      }

   }
   
   public boolean isPathable()
   {
      return isLowPassable() || this instanceof Door;
   }

}