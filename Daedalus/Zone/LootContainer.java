/*
	private boolean dispensedLoot;
	private Vector<Item> guaranteedItems;
	private int lootQuality;
	private int level;


	public boolean hasDispensedLoot(){return dispensedLoot;}
	public Vector<Item> getGuaranteedItems(){return guaranteedItems;}
	public int getLootQuality(){return lootQuality;}
	public int getLevel(){return level;}


	public void setDispensedLoot(boolean d){dispensedLoot = d;}
	public void setGuaranteedItems(Vector<Item> g){guaranteedItems = g;}
	public void setLootQuality(int l){lootQuality = l;}
	public void setLevel(int l){level = l;}
*/

package Daedalus.Zone;

import java.util.*;
import Daedalus.Item.*;

public interface LootContainer
{
   public boolean hasDispensedLoot();
   public int getLevel();
   public Vector<Item> getGuaranteedItems();
   public int getLootQuality();
}