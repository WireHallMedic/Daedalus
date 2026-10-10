package Daedalus.Zone;

import java.util.*;
import Daedalus.Engine.*;

public class MazeBuilder
{
	private int width;
	private int height;
	private double wallChance;
	public MazeCell[][] grid;
	
	private static final int MIN_WIDTH = 3;
	private static final int MIN_HEIGHT = 3;
	private static final double MAX_WALL_CHANCE = 0.8;
	private static final double MIN_WALL_CHANCE = 0.0;
	
	
	public MazeBuilder()
	{
		this(10, 10, .5);
	}
   
   public boolean exitsNorth(int x, int y)
   {
      return !grid[x][y].northWall;
   }
   
   public boolean exitsEast(int x, int y)
   {
      return !grid[x][y].eastWall;
   }
   
   public boolean exitsSouth(int x, int y)
   {
      return !grid[x][y].southWall;
   }
   
   public boolean exitsWest(int x, int y)
   {
      return !grid[x][y].westWall;
   }
	
	
	public MazeBuilder(int w, int h, double c)
	{
		width = Math.max(MIN_WIDTH, w);
		height = Math.max(MIN_HEIGHT, h);
		wallChance = Math.max(MIN_WALL_CHANCE, c);
		wallChance = Math.min(MAX_WALL_CHANCE, wallChance);
		grid = new MazeCell[width][height];
		
		for(int x = 0; x < width; x++)
		for(int y = 0; y < height; y++)
			grid[x][y] = new MazeCell();
		
		for(int x = 0; x < width; x++)
		{
			grid[x][0].northWall = true;
			grid[x][height - 1].southWall = true;
		}
		
		for(int y = 0; y < height; y++)
		{
			grid[0][y].westWall = true;
			grid[width - 1][y].eastWall = true;
		}
		
		
		addWalls();
		
		connect();
	}
	
	
	
	public void addWalls()
	{
		for(int x = 0; x < grid.length; x++)
		for(int y = 0; y < grid[0].length; y++)
		{
			if(RNG.nextDouble() <= wallChance &&
				x != grid.length - 1)
			{
				grid[x][y].eastWall = true;
				grid[x+1][y].westWall = true;
			}
			if(RNG.nextDouble() <= wallChance &&
				y != grid[0].length - 1)
			{
				grid[x][y].southWall = true;
				grid[x][y+1].northWall = true;
			}
		}
	}
	
	
	public void connect()
	{
		boolean[][] passMap = floodFill();
		
		for(int x = 0; x < width; x++)
		for(int y = 0; y < height; y++)
		{
			if(passMap[x][y] == false)
			{
				if(y == 0)
				{
					burrowWest(x, y);
				}
				else if(x == 0)
				{
					burrowNorth(x, y);
				}
				else
				{
					if(Math.random() > .5)
						burrowNorth(x, y);
					else
						burrowWest(x, y);
				}
				passMap = floodFill();
			}
		}
	}
	
	
	
	public boolean[][] floodFill()
	{
		boolean[][] boolMap = new boolean[width][height];
		Vector<Coord> boolList = new Vector<Coord>();
		boolList.add(new Coord(0, 0));
		Coord curLoc;
		
		while(boolList.size() > 0)
		{
			curLoc = boolList.elementAt(0);
			
			boolMap[curLoc.x][curLoc.y] = true;
			
			if(grid[curLoc.x][curLoc.y].northWall == false &&
				boolMap[curLoc.x][curLoc.y - 1] == false)
			{
				boolMap[curLoc.x][curLoc.y - 1] = true;
				boolList.add(new Coord(curLoc.x, curLoc.y - 1));
			}
			
			if(grid[curLoc.x][curLoc.y].southWall == false &&
				boolMap[curLoc.x][curLoc.y + 1] == false)
			{
				boolMap[curLoc.x][curLoc.y + 1] = true;
				boolList.add(new Coord(curLoc.x, curLoc.y + 1));
			}
			
			if(grid[curLoc.x][curLoc.y].westWall == false &&
				boolMap[curLoc.x - 1][curLoc.y] == false)
			{
				boolMap[curLoc.x - 1][curLoc.y] = true;
				boolList.add(new Coord(curLoc.x - 1, curLoc.y));
			}
			
			if(grid[curLoc.x][curLoc.y].eastWall == false &&
				boolMap[curLoc.x + 1][curLoc.y] == false)
			{
				boolMap[curLoc.x + 1][curLoc.y] = true;
				boolList.add(new Coord(curLoc.x + 1, curLoc.y));
			}
			
			boolList.remove(0);
		}
		
		return boolMap;
	}
	
	
	private void burrowNorth(int x, int y)
	{
		grid[x][y].northWall = false;
		grid[x][y - 1].southWall = false;
	}
	
	
	private void burrowWest(int x, int y)
	{
		grid[x][y].westWall = false;
		grid[x - 1][y].eastWall = false;
	}
   
   public int[][] getTileIndexMap()
   {
      int NS = 3 + (11 * 16);
      int WE = 4 + (12 * 16);
      
      int NE = 0 + (12 * 16);
      int NW = 9 + (13 * 16);
      int SE = 10 + (13 * 16);
      int SW = 15 + (11 * 16);
      
      int NES = 3 + (12 * 16);
      int ESW = 2 + (12 * 16);
      int NSW = 4 + (11 * 16);
      int NEW = 1 + (12 * 16);
      
      int[][] tileIndexMap = new int[width * 3][height * 3];
      for(int w = 0; w < width; w++)
      for(int h = 0; h < height; h++)
      {
         // corners and interior
         tileIndexMap[(w * 3) + 0][(h * 3) + 0] = SE;
         tileIndexMap[(w * 3) + 2][(h * 3) + 0] = SW;
         tileIndexMap[(w * 3) + 0][(h * 3) + 2] = NE;
         tileIndexMap[(w * 3) + 2][(h * 3) + 2] = NW;
         tileIndexMap[(w * 3) + 1][(h * 3) + 1] = ' ';
         
         if(grid[w][h].northWall)
         {
            tileIndexMap[(w * 3) + 1][(h * 3) + 0] = WE;
         }
         else
         {
            tileIndexMap[(w * 3) + 1][(h * 3) + 0] = NEW;
         }
         if(grid[w][h].southWall)
         {
            tileIndexMap[(w * 3) + 1][(h * 3) + 2] = WE;
         }
         else
         {
            tileIndexMap[(w * 3) + 1][(h * 3) + 2] = ESW;
         }
         if(grid[w][h].westWall)
         {
            tileIndexMap[(w * 3) + 0][(h * 3) + 1] = NS;
         }
         else
         {
            tileIndexMap[(w * 3) + 0][(h * 3) + 1] = NSW;
         }
         if(grid[w][h].eastWall)
         {
            tileIndexMap[(w * 3) + 2][(h * 3) + 1] = NS;
         }
         else
         {
            tileIndexMap[(w * 3) + 2][(h * 3) + 1] = NES;
         }
      }
      return tileIndexMap;
   }
	
	
   public void print()
   {
      char[][] charMap = new char[3*width][3*height];
      int startX = 0;
      int startY = 0;
		
		MazeCell curCell;
		
		for(int x = 0; x < width; x++)
		for(int y = 0; y < height; y++)
		{
			curCell = grid[x][y];
			
			startX = x * 3;
			startY = y * 3;
			
			charMap[startX][startY] = '#';
			charMap[startX+2][startY] = '#';
			charMap[startX][startY+2] = '#';
			charMap[startX+2][startY+2] = '#';
			charMap[startX+1][startY+1] = '.';
			
			if(curCell.northWall)
				charMap[startX+1][startY] = '#';
			else
				charMap[startX+1][startY] = '.';
			
			if(curCell.southWall)
				charMap[startX+1][startY+2] = '#';
			else
				charMap[startX+1][startY+2] = '.';
			
			if(curCell.westWall)
				charMap[startX][startY+1] = '#';
			else
				charMap[startX][startY+1] = '.';
			
			if(curCell.eastWall)
				charMap[startX+2][startY+1] = '#';
			else
				charMap[startX+2][startY+1] = '.';
			
		}
		
		
		for(int y = 0; y < charMap[0].length; y++)
		{
			for(int x = 0; x < charMap.length; x++)
			{
				System.out.print(charMap[x][y] + "");
			}
			System.out.println();
		}
   }
	
	
   private class MazeCell
   {
   	public boolean northWall;
   	public boolean eastWall;
   	public boolean southWall;
   	public boolean westWall;
   	
   	public MazeCell()
   	{
   		northWall = false;
   		eastWall = false;
   		southWall = false;
   		westWall = false;
   	}
   }
   
   public static void main(String[] args)
   {
      MazeBuilder mb = new MazeBuilder(4, 4, .5);
      mb.print();
   }
}