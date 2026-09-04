package Daedalus.Item;

import java.util.*;
import Daedalus.GUI.*;
import WidlerSuite.Coord;

public class Inventory implements ItemConstants
{
	private Vector<Item> itemList;
	private Credits credits;


	public Vector<Item> getItemList(){return itemList;}
	public Credits getCredits(){return credits;}


	public void setItemList(Vector<Item> i){itemList = i;}
	public void setCredits(Credits c){credits = c;}

   
}