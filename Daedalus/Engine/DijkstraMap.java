/*
Note that this implementation is different from the one in WidlerSuite
*/

package Daedalus.Engine;

import WidlerSuite.Coord;

public class DijkstraMap
{
   public static final double MAX_DISTANCE = 10000.0;

	private int width;
	private int height;
	private boolean[][] passMap;
	private double[][] distanceMap;
   private int goalsPlaced;


	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public boolean[][] getPassMap(){return passMap;}
	public double[][] getDistanceMap(){return distanceMap;}
   public int getGoalsPlaced(){return goalsPlaced;}


	public void setWidth(int w){width = w;}
	public void setHeight(int h){height = h;}
	public void setPassMap(boolean[][] p){passMap = p; clear();}


   public DijkstraMap(boolean[][] pMap)
   {
      width = pMap.length;
      height = pMap[0].length;
      passMap = new boolean[width][height];
      distanceMap = new double[width][height];
      goalsPlaced = 0;
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         passMap[x][y] = pMap[x][y];
         distanceMap[x][y] = MAX_DISTANCE;
      }
   }
   
   
   // retains passMap
   public void clear()
   {
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         distanceMap[x][y] = MAX_DISTANCE;
      }
      goalsPlaced = 0;
   }
   
   
   public boolean isInBounds(int x, int y)
   {
      return x >= 0 && x < width && y >= 0 && y < height;
   }
   
   
   private void process(int x, int y, double val)
   {
      if(isInBounds(x, y) &&
         passMap[x][y] &&
         distanceMap[x][y] > val)
      {
         distanceMap[x][y] = val;
         // ortho
         process(x + 1, y, val + 1.0);
         process(x - 1, y, val + 1.0);
         process(x, y + 1, val + 1.0);
         process(x, y - 1, val + 1.0);
         // diag
         process(x + 1, y + 1, val + 1.4);
         process(x - 1, y - 1, val + 1.4);
         process(x + 1, y - 1, val + 1.4);
         process(x - 1, y + 1, val + 1.4);
      }
   }
   
   
   public void addGoal(int x, int y)
   {
      process(x, y, 0.0);
      goalsPlaced++;
   }
   public void addGoal(Coord c){addGoal(c.x, c.y);}
   
   
   public double getValue(int x, int y)
   {
      if(isInBounds(x, y))
         return distanceMap[x][y];
      return MAX_DISTANCE;
   }
   public double getValue(Coord c){return getValue(c.x, c.y);}
   
   
   public double getHighestPassable()
   {
      double max = 0.0;
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         if(distanceMap[x][y] > max && distanceMap[x][y] != MAX_DISTANCE)
            max = distanceMap[x][y];
      }
      return max;
   }
   
   public static void main(String[] args)
   {
      boolean[][] passMap = new boolean[5][5];
      for(int x = 0; x < 5; x++)
      for(int y = 0; y < 5; y++)
      {
         passMap[x][y] = true;
      }
      passMap[1][0] = false;
      DijkstraMap dMap = new DijkstraMap(passMap);
      dMap.addGoal(2, 2);
      for(int y = 0; y < 5; y++)
      {
         for(int x = 0; x < 5; x++)
         {
            System.out.print(dMap.getValue(x, y) + " ");
         }
         System.out.println();
      }
      System.out.println("Max: " + dMap.getHighestPassable());
   }
}