package Daedalus.Item;

import java.util.*;
import Daedalus.GUI.*;
import Daedalus.Actor.*;

public class Inventory implements ItemConstants
{
	private Credits credits;
	private Vector<Item> itemList;
	private Actor owner;


	public Credits getCredits(){return credits;}
	public Vector<Item> getItemList(){return itemList;}
	public Actor getOwner(){return owner;}


	public void setCredits(Credits c){credits = c;}
	public void setItemList(Vector<Item> i){itemList = i;}
	public void setOwner(Actor o){owner = o;}


   public Inventory(Actor a)
   {
      credits = new Credits(0);
      itemList = new Vector<Item>();
      owner = a;
   }
   
   public boolean isFull()
   {
      return itemList.size() >= MAX_INVENTORY_SIZE;
   }
   
   public void add(Item item)
   {
      if(item instanceof Credits)
      {
         credits.add((Credits)item);
      }
      else
      {
         itemList.add(item);
      }
   }
   
   public Item getItem(int index)
   {
      return itemList.elementAt(index);
   }
   
   public Item takeItem(int index)
   {
      Item item = itemList.elementAt(index);
      itemList.removeElementAt(index);
      return item;
   }
   
   public int size()
   {
      return itemList.size();
   }
   
}