package Daedalus.Ability;

import java.util.*;
import Daedalus.Actor.*;

public class StatusEffect implements AbilityConstants
{
   private String name;
	private StatBlock statBlock;
	private int maxDuration;
	private int remainingDuration;
	private Vector<StatusEffectTag> tagList;


   public String getName(){return name;}
	public StatBlock getStatBlock(){return statBlock;}
	public int getMaxDuration(){return maxDuration;}
	public int getRemainingDuration(){return remainingDuration;}
	public Vector<StatusEffectTag> getTagList(){return tagList;}


   public void setName(String n){name = n;}
	public void setStatBlock(StatBlock s){statBlock = s;}
	public void setMaxDuration(int m){maxDuration = m; setRemainingDuration(m);}
	public void setRemainingDuration(int r){remainingDuration = r;}
	public void setTagList(Vector<StatusEffectTag> t){tagList = t;}


   public StatusEffect(String n)
   {
      name = n;
      statBlock = new StatBlock();
      maxDuration = 10;
      remainingDuration = 10;
      tagList = new Vector<StatusEffectTag>();
   }
   
   public void addTag(StatusEffectTag tag)
   {
      tagList.add(tag);
   }
   
   public boolean hasTag(StatusEffectTag tag)
   {
      return tagList.contains(tag);
   }
   
   public void increment()
   {
      remainingDuration--;
   }
   
   public boolean isExpired()
   {
      return remainingDuration <= 0;
   }
   
   public void applyTags(Actor a)
   {
      for(StatusEffectTag tag: tagList)
      {
         switch(tag)
         {
            case HEALING : a.heal(1); break;
         }
      }
   }
}