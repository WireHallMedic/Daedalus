package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Engine.*;

public class Chest extends ToggleTile implements ZoneConstants, GUIConstants, LootContainer
{
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


   public Chest(int lvl)
   {
      super(TileBase.CHEST, TileBase.OPEN_CHEST);
      oneToggleOnly = true;
      dispensedLoot = false;
      lootQuality = LootFactory.CHEST_LOOT;
      guaranteedItems = new Vector<Item>();
      level = lvl;
   }
   
   public void add(Item item)
   {
      guaranteedItems.add(item);
   }
   
   @Override
   protected void onToggle()
   {
      super.onToggle();
      Game.getCurMap().dumpLootContainer(this);
      dispensedLoot = true;
   }
   
   // a crate is a chest that's fragile and has less loot
   public static Chest getCrate(int lvl)
   {
      Chest crate = new Chest(lvl);
      crate.setName("Crate");
      crate.lootQuality = LootFactory.CRATE_LOOT;
      crate.setFGColor(GUIConstants.WOOD);
      crate.setDurability(Durability.FRAGILE);
      return crate;
   }
}