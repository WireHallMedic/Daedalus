package Daedalus.Zone;

import java.awt.*;
import Daedalus.GUI.*;
import WidlerSuite.Coord;

public class ZoneMap implements ZoneConstants, GUIConstants
{
	private int width;
	private int height;
	private ZoneTile oobTile;
	private ZoneTile[][] tileMap;


	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public ZoneTile getOOBTile(){return oobTile;}
	public ZoneTile[][] getTileMap(){return tileMap;}


	public void setWidth(int w){width = w;}
	public void setHeight(int h){height = h;}
	public void setOOBTile(ZoneTile o){oobTile = o;}
	public void setTileMap(ZoneTile[][] t){tileMap = t;}

   public ZoneMap(int w, int h)
   {
      width = w;
      height = h;
      oobTile = new ZoneTile(TileBase.WALL);
      tileMap = new ZoneTile[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         tileMap[x][y] = new ZoneTile(TileBase.CLEAR);
      }
   }
   
   public boolean isInBounds(int x, int y)
   {
      return x >= 0 && y >= 0 && x < width && y < height;
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
}