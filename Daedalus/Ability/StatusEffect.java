package Daedalus.Ability;

import java.util.*;
import Daedalus.Actor.*;

public class StatusEffect implements AbilityConstants
{
   private String name;
	private StatBlock statBlock;
	private int maxDuration;
	private int remainingDuration;
   private int intensity;
	private Vector<StatusEffectTag> tagList;


   public String getName(){return name;}
	public StatBlock getStatBlock(){return statBlock;}
	public int getMaxDuration(){return maxDuration;}
	public int getRemainingDuration(){return remainingDuration;}
   public int getIntensity(){return intensity;}
	public Vector<StatusEffectTag> getTagList(){return tagList;}


   public void setName(String n){name = n;}
	public void setStatBlock(StatBlock s){statBlock = s;}
	public void setMaxDuration(int m){maxDuration = m; setRemainingDuration(m);}
	public void setRemainingDuration(int r){remainingDuration = r;}
   public void setIntensity(int i){intensity = i;}
	public void setTagList(Vector<StatusEffectTag> t){tagList = t;}


   public StatusEffect(String n)
   {
      name = n;
      statBlock = new StatBlock();
      maxDuration = 10;
      remainingDuration = 10;
      intensity = 1;
      tagList = new Vector<StatusEffectTag>();
   }

   public StatusEffect(StatusEffect that)
   {
      this.name = that.name;
      this.statBlock = that.statBlock.copy();
      this.maxDuration = that.maxDuration;
      this.remainingDuration = that.remainingDuration;
      this.tagList = new Vector<StatusEffectTag>();
      this.intensity = that.intensity;
      for(StatusEffectTag tag: that.tagList)
         this.tagList.add(tag);
   }
   
   public StatusEffect copy()
   {
      return new StatusEffect(this);
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
            case HEALING : a.heal(intensity); break;
         }
      }
   }
   
   // returns true if tag lists match and are not empty
   public boolean canCombine(StatusEffect that)
   {
      if(this.tagList.size() == 0 || this.tagList.size() != that.tagList.size())
         return false;
      for(StatusEffectTag tag: this.tagList)
         if(!that.hasTag(tag))
            return false;
      return true;
   }
   
   public void combine(StatusEffect that)
   {
      this.maxDuration = Math.max(this.maxDuration, that.maxDuration);
      this.remainingDuration = Math.max(this.remainingDuration, that.remainingDuration);
      this.intensity = Math.max(this.intensity, that.intensity);
      // as these should only be used on temporary status effects, we don't need to combine stat blocks
   }
}