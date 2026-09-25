package Daedalus.Engine;

public class TableItemWrapper implements TableItem
{
	private int minLevel;
	private int maxLevel;
	private int weight;
   private Object object;


	public int getMinLevel(){return minLevel;}
	public int getMaxLevel(){return maxLevel;}
	public int getWeight(){return weight;}
   public Object getObject(){return object;}


	public void setMinLevel(int m){minLevel = m;}
	public void setMaxLevel(int m){maxLevel = m;}
	public void setWeight(int w){weight = w;}
   public void setObject(Object o){object = o;}
   
   public TableItemWrapper(Object obj, int min, int max, int wei)
   {
      object = obj;
      minLevel = min;
      maxLevel = max;
      weight = wei;
   }
   
   public TableItemWrapper(Object obj, int min, int max)
   {
      this(obj, min, max, TableItem.BASE_WEIGHT);
   }
   
   public TableItemWrapper(Object obj, int min, int max, double weightMult)
   {
      this(obj, min, max, (int)(TableItem.BASE_WEIGHT * weightMult));
   }
   
   
}