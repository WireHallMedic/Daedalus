package Daedalus.Item;

import Daedalus.GUI.*;

public class Item implements ItemConstants, GUIConstants
{
	private ImageTile squareTile;
	private ImageTile rectTile;
   private String name;


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
}