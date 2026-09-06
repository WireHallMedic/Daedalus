package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import WidlerSuite.Coord;
import WidlerSuite.SpiralSearch;

public class ZoneMap implements ZoneConstants, GUIConstants
{
	private int width;
	private int height;
	private ZoneTile oobTile;
	private ZoneTile[][] tileMap;
	private Item[][] itemMap;
	private boolean[][] visibilityMap;
	private BufferedImage[][] lastSeenMap;
   private static final BufferedImage BLACK_SQUARE = SQUARE_PALETTE.getTile(' ');


	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public ZoneTile getOOBTile(){return oobTile;}
	public ZoneTile[][] getTileMap(){return tileMap;}
   public Item[][] getItemMap(){return itemMap;}
   public boolean[][] getVisibilityMap(){return visibilityMap;}
   public BufferedImage[][] getLastSeenMap(){return lastSeenMap;}


	public void setWidth(int w){width = w;}
	public void setHeight(int h){height = h;}
	public void setOOBTile(ZoneTile o){oobTile = o;}
	public void setTileMap(ZoneTile[][] t){tileMap = t; updateSubmaps();}
   public void setItemMap(Item[][] im){itemMap = im;}


   public ZoneMap(int w, int h)
   {
      width = w;
      height = h;
      oobTile = new ZoneTile(TileBase.WALL);
      tileMap = new ZoneTile[width][height];
      itemMap = new Item[width][height];
      visibilityMap = new boolean[width][height];
      lastSeenMap = new BufferedImage[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         tileMap[x][y] = new ZoneTile(TileBase.CLEAR);
         itemMap[x][y] = null;
         visibilityMap[x][y] = false;
         lastSeenMap[x][y] = BLACK_SQUARE;
      }
   }
   
   
   public boolean isInBounds(int x, int y)
   {
      return x >= 0 && y >= 0 && x < width && y < height;
   }
   public boolean isInBounds(Coord c){return isInBounds(c.x, c.y);}
   public boolean isInBounds(Actor a){return isInBounds(a.getTileLoc());}
   
   
   public void updateSubmaps(int x, int y)
   {
      visibilityMap[x][y] = getTile(x, y).isTransparent();
   }
   public void updateSubmaps(Coord c){updateSubmaps(c.x, c.y);}
   
   
   public void updateSubmaps()
   {
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         updateSubmaps(x, y);
   }
   
   public void toggle(int x, int y)
   {
      ((ToggleTile)tileMap[x][y]).toggle();
      updateSubmaps(x, y);
   }
   public void toggle(Coord c){toggle(c.x, c.y);}
   
   
   // tile and image stuff
   ////////////////////////////////////////////////
   
   public void setTile(int x, int y, ZoneTile zt)
   {
      if(isInBounds(x, y))
      {
         tileMap[x][y] = zt;
         updateSubmaps(x, y);
      }
   }
   
   
   public void setTile(int x, int y, TileBase base)
   {
      if(isInBounds(x, y))
      {
         tileMap[x][y].set(base);
         updateSubmaps(x, y);
      }
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
   
   
   public void updateLastSeen(int x, int y, int tileIndex)
   {
      lastSeenMap[x][y] = SQUARE_PALETTE.getTile(tileIndex);
   }
   
   
   public BufferedImage getLastSeen(int x, int y)
   {
      if(isInBounds(x, y))
         return lastSeenMap[x][y];
      return BLACK_SQUARE;
   }
   public BufferedImage getLastSeen(Coord c){return getLastSeen(c.x, c.y);}
   
   public void setLastSeen(int x, int y)
   {
      if(!isInBounds(x, y))
         lastSeenMap[x][y] = SQUARE_PALETTE.getTile(oobTile.getTileIndex(), GREY, BLACK);
      else if(isItemAt(x, y))
         lastSeenMap[x][y] = SQUARE_PALETTE.getTile(itemMap[x][y].getTileIndex(), GREY, BLACK);
      else 
         lastSeenMap[x][y] = SQUARE_PALETTE.getTile(tileMap[x][y].getTileIndex(), GREY, BLACK);
   }
   
   
   // actor stuff
   ////////////////////////////////////////////
   
   public boolean canStep(Actor a, int x, int y)
   {
      return isInBounds(x, y) && tileMap[x][y].isLowPassable();
   }
   public boolean canStep(Actor a, Coord c){return canStep(a, c.x, c.y);}
   
   
   
   // item stuff
   ////////////////////////////////////////////
   
   public boolean canPlaceItem(int x, int y)
   {
      return isValidLocationForItem(x, y) && getItemAt(x, y) == null;
   }
   public boolean canPlaceItem(Coord c){return canPlaceItem(c.x, c.y);}
   
   
   public boolean isValidLocationForItem(int x, int y)
   {
      return isInBounds(x, y) && 
             tileMap[x][y].isLowPassable() &&
             !(tileMap[x][y] instanceof Door) &&
             !(tileMap[x][y] instanceof Exit);
   }
   public boolean isValidLocationForItem(Coord c){return isValidLocationForItem(c.x, c.y);}
   
   
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
   
   
   // drops an item in the nearest droppable tile
   public void dropItem(Item item, int x, int y)
   {
      Coord loc = getDropLocation(x, y);
      if(loc != null)
         setItemAt(item, loc.x, loc.y);
   }
   public void dropItem(Item item, Coord c){dropItem(item, c.x, c.y);}
   
   
   public boolean[][] getItemDroppableMap(int centerX, int centerY)
   {
      boolean[][] map = new boolean[ITEM_SEARCH_DIAMETER][ITEM_SEARCH_DIAMETER];
      int radius = ITEM_SEARCH_DIAMETER / 2;
      for(int x = 0; x < ITEM_SEARCH_DIAMETER; x++)
      for(int y = 0; y < ITEM_SEARCH_DIAMETER; y++)
      {
         int xSearch = centerX - radius + x;
         int ySearch = centerY - radius + y;
         map[x][y] = isValidLocationForItem(xSearch, ySearch);
      }
      return map;
   }
   public boolean[][] getItemDroppableMap(Coord c){return getItemDroppableMap(c.x, c.y);}
   
   
   public Coord getDropLocation(int originX, int originY)
   {
      int xInset = originX - (ITEM_SEARCH_DIAMETER / 2);
      int yInset = originY - (ITEM_SEARCH_DIAMETER / 2);
      boolean[][] searchMap = getItemDroppableMap(originX, originY);
      SpiralSearch search = new SpiralSearch(searchMap, ITEM_SEARCH_DIAMETER / 2, ITEM_SEARCH_DIAMETER / 2);
      Coord prospect = search.getNext();
      while(prospect != null)
      {
         prospect.x += xInset;
         prospect.y += yInset;
         if(canPlaceItem(prospect))
         {
            return prospect;
         }
         prospect = search.getNext();
      }
      System.out.println("No place to drop item.");
      return null;
   }
   public Coord getDropLocation(Coord origin){return getDropLocation(origin.x, origin.y);}
   
   
   // test map
   ////////////////////////////////////////
   
   public static ZoneMap getTestMap()
   {
      int diameter = 12;
      ZoneMap z = new ZoneMap(diameter, diameter);
      z.setTile(6, 3, new Door());
      z.setTile(6, 5, new Switch());
      z.getTile(6, 5).setFGColor(CYAN);
      z.setTile(6, 7, new Chest());
      
      for(int x = 0; x < diameter; x++)
      {
         z.setTile(x, 0, new ZoneTile(TileBase.WALL));
         z.setTile(x, diameter - 1, new ZoneTile(TileBase.WALL));
      }
      
      for(int y = 0; y < diameter; y++)
      {
         z.setTile(0, y, new ZoneTile(TileBase.WALL));
         z.setTile(diameter - 1, y, new ZoneTile(TileBase.WALL));
      }
      
      z.setItemAt(new Item("Test Item", '"'), 1, 5);
      z.setItemAt(new Item("Enticing Item", '"'), 1, 6);
      z.setItemAt(new Credits(10), 1, 7);
      
      z.updateSubmaps();
      return z;
   }
}