/* for copy-pasting:

	private int minLevel;
	private int maxLevel;
	private int weight;


	public int getMinLevel(){return minLevel;}
	public int getMaxLevel(){return maxLevel;}
	public int getWeight(){return weight;}


	public void setMinLevel(int m){minLevel = m;}
	public void setMaxLevel(int m){maxLevel = m;}
	public void setWeight(int w){weight = w;}


*/


package Daedalus.Engine;

public interface TableItem
{
   public static final int BASE_WEIGHT = 60;
   
   public int getMinLevel();
   public int getMaxLevel();
   public int getWeight();
}