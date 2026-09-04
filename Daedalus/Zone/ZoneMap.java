package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import WidlerSuite.Coord;

public class ZoneMap implements ZoneConstants, GUIConstants
{
	private int width;
	private int height;
	private ZoneTile oobTile;
	private ZoneTile[][] tileMap;
	private Item[][] itemMap;


	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public ZoneTile getOOBTile(){return oobTile;}
	public ZoneTile[][] getTileMap(){return tileMap;}
   public Item[][] getItemMap(){return itemMap;}


	public void setWidth(int w){width = w;}
	public void setHeight(int h){height = h;}
	public void setOOBTile(ZoneTile o){oobTile = o;}
	public void setTileMap(ZoneTile[][] t){tileMap = t;}
   public void setItemMap(Item[][] im){itemMap = im;}


   public ZoneMap(int w, int h)
   {
      width = w;
      height = h;
      oobTile = new ZoneTile(TileBase.WALL);
      tileMap = new ZoneTile[width][height];
      itemMap = new Item[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         tileMap[x][y] = new ZoneTile(TileBase.CLEAR);
         itemMap[x][y] = null;
      }
   }
   
   
   public boolean isInBounds(int x, int y)
   {
      return x >= 0 && y >= 0 && x < width && y < height;
   }
   public boolean isInBounds(Coord c){return isInBounds(c.x, c.y);}
   public boolean isInBounds(Actor a){return isInBounds(a.getTileLoc());}
   
   
   // tile and image stuff
   ////////////////////////////////////////////////
   
   public void setTile(int x, int y, ZoneTile zt)
   {
      if(isInBounds(x, y))
         tileMap[x][y] = zt;
   }
   
   
   public void setTile(int x, int y, TileBase base)
   {
      if(isInBounds(x, y))
         tileMap[x][y].set(base);
   }
   
   
   public ZoneTile getTile(int x, int y)
   {
      if(isInBounds(x, y))
         return tileMap[x][y];
      return oobTile.copy();
   }
   public ZoneTile getTile(Coord c){return getTile(c.x, c.y);}
   
   
   public BufferedImage getImage(int x, int y)
   {
      // out of bounds
      if(!isInBounds(x, y))
         return oobTile.getImage();
      // item on tile
      if(isItemAt(x, y))
      {
         return new ImageTile(SQUARE_PALETTE, itemMap[x][y].getTileIndex(), 
                              itemMap[x][y].getFGColor(), tileMap[x][y].getBGColor()).getImage();
      }
      // out of bounds
      return tileMap[x][y].getImage();
   }
   public BufferedImage getImage(Coord c){return getImage(c.x, c.y);}
   
   // interaction stuff
   ////////////////////////////////////////////
   
   public boolean canStep(Actor a, int x, int y)
   {
      return isInBounds(x, y) && tileMap[x][y].isLowPassable();
   }
   public boolean canStep(Actor a, Coord c){return canStep(a, c.x, c.y);}
   
   
   public boolean isItemAt(int x, int y)
   {
      return isInBounds(x, y) && itemMap[x][y] != null;
   }
   public boolean isItemAt(Coord c){return isItemAt(c.x, c.y);}
   
   
   public Item getItemAt(int x, int y)
   {
      if(isItemAt(x, y))
         return itemMap[x][y];
      return null;
   }
   public Item getItemAt(Coord c){return getItemAt(c.x, c.y);}
   
   
   public Item takeItemAt(int x, int y)
   {
      Item item = null;
      if(isItemAt(x, y))
         item = itemMap[x][y];
      itemMap[x][y] = null;
      return item;
   }
   public Item takeItemAt(Coord c){return takeItemAt(c.x, c.y);}
   
   
   public void setItemAt(Item item, int x, int y)
   {
      if(isItemAt(x, y))
         throw new Error("Attempt to overwrite item at [" + x + "," + y + "]");
      itemMap[x][y] = item;
   }
   public void setItemAt(Item item, Coord c){setItemAt(item, c.x, c.y);}
   
   
   // test map
   ////////////////////////////////////////
   
   public static ZoneMap getTestMap()
   {
      ZoneMap z = new ZoneMap(10, 10);
      z.setTile(6, 3, new Door());
      z.setTile(6, 5, new Switch());
      z.getTile(6, 5).setFGColor(CYAN);
      z.setTile(6, 7, new Chest());
      
      z.setItemAt(new Item("Test Item", '"'), 1, 5);
      return z;
   }
}