package Daedalus.Item;

import Daedalus.GUI.*;
import WidlerSuite.Coord;

public class Item implements ItemConstants, GUIConstants
{
	protected ImageTile squareTile;
	protected ImageTile rectTile;
   protected String name;


	public ImageTile getSquareTile(){return squareTile;}
	public ImageTile getRectTile(){return rectTile;}
   public String getName(){return name;}
   
   public void setName(String n){name = n;}


   public Item(String _name, int tileIndex, int fgColor, int bgColor)
   {
      squareTile = new ImageTile(SQUARE_PALETTE);
      rectTile = new ImageTile(RECT_PALETTE);
      setName(_name);
      setFGColor(fgColor);
      setBGColor(bgColor);
      setTileIndex(tileIndex);
   }
   public Item(String _name, int tileIndex){this(_name, tileIndex, WHITE, BLACK);}
   
   
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
      ut.set(getSquareTile());
      ut.setTileLoc(xLoc, yLoc);
      ut.setBGColor(GUIConstants.TRANSPARENT);
      return ut;
   }
   public UnboundTile getUnboundTile(Coord c){return getUnboundTile(c.x, c.y);}
   
   
   // passthroughs for imageTiles
   //////////////////////////////////////////////
   public void setFGColor(int f)
   {
      squareTile.setFGColor(f);
      rectTile.setFGColor(f);
   }
	
   public void setBGColor(int b)
   {
      squareTile.setBGColor(b);
      rectTile.setBGColor(b);
   }
	
   public void setTileIndex(int t)
   {
      squareTile.setTileIndex(t);
      rectTile.setTileIndex(t);
   }
   
   public void setLowerTileIndex(int t)
   {
      squareTile.setLowerTileIndex(t);
      rectTile.setLowerTileIndex(t);
   }
   
   public int getFGColor(){return squareTile.getFGColor();}
	public int getBGColor(){return squareTile.getBGColor();}
	public int getTileIndex(){return squareTile.getTileIndex();}
   public int getLowerTileIndex(){return squareTile.getLowerTileIndex();}
}