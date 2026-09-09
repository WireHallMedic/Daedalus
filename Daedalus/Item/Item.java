package Daedalus.Item;

import Daedalus.GUI.*;
import WidlerSuite.Coord;

public class Item extends ImageTile implements ItemConstants, GUIConstants
{
	private String name;


	public String getName(){return name;}


	public void setName(String n){name = n;}


   public Item(String _name, int tileIndex, int fgColor)
   {
      super(SQUARE_PALETTE);
      setName(_name);
      setTileIndex(tileIndex);
      setFGColor(fgColor);
      setBGColor(TRANSPARENT);
   }
   public Item(String _name, int tileIndex){this(_name, tileIndex, WHITE);}
   public Item(String _name, ItemBase base){this(_name, base.tileIndex, WHITE);}
   public Item(String _name, ItemBase base, int fgColor){this(_name, base.tileIndex, fgColor);}
   
   
   public String getNameWithParticle()
   {
      if(name.toLowerCase().charAt(0) == 'a' ||
         name.toLowerCase().charAt(0) == 'e' ||
         name.toLowerCase().charAt(0) == 'i' ||
         name.toLowerCase().charAt(0) == 'o' ||
         name.toLowerCase().charAt(0) == 'u')
         return "an " + name;
      return "a " + name;
   }
   
   // for pickup effects
   public UnboundTile getUnboundTile(int xLoc, int yLoc)
   {
      UnboundTile ut = new UnboundTile(SQUARE_PALETTE);
      ut.set(this);
      ut.setTileLoc(xLoc, yLoc);
      ut.setBGColor(GUIConstants.TRANSPARENT);
      return ut;
   }
   public UnboundTile getUnboundTile(Coord c){return getUnboundTile(c.x, c.y);}
   
}