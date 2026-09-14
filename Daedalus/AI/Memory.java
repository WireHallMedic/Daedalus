package Daedalus.AI;

import Daedalus.Zone.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;
import java.util.*;

public class Memory implements AIConstants
{
	private MemoryList[] teamList;
	private Actor self;
	private int memoryDuration;


	public MemoryList[] getTeamList(){return teamList;}
	public Actor getSelf(){return self;}
	public int getMemoryDuration(){return memoryDuration;}


	public void setTeamList(MemoryList[] t){teamList = t;}
	public void setSelf(Actor s){self = s;}
	public void setMemoryDuration(int m){memoryDuration = m;}


   public Memory(Actor s)
   {
      self = s;
      memoryDuration = STANDARD_MEMORY_DURATION;
      teamList = new MemoryList[Team.values().length];
      for(int i = 0; i < Team.values().length; i++)
         teamList[i] = new MemoryList();
   }
      
   public void notice(Actor a)
   {
      teamList[a.getAI().getTeam().ordinal()].notice(a);
   }
   
   public void cleanLists()
   {
      for(int i = 0; i < teamList.length; i++)
         teamList[i].clean();
   }
   
   public void increment()
   {
      for(int i = 0; i < teamList.length; i++)
         teamList[i].increment();
   }
   
   public Vector<Actor> getEnemyList()
   {
      Vector<Actor> enemyList = new Vector<Actor>();
      for(int i = 0; i < teamList.length; i++)
      {
         if(self.getAI().getTeam().isEnemy(Team.values()[i]))
         {
            for(int j = 0; j < teamList[i].size(); j++)
               enemyList.add(teamList[i].getActorAt(j));
         }
      }
      return enemyList;
   }
   
   
   private class MemoryList
   {
      public Vector<MemoryObject> list;
      
      public MemoryList()
      {
         list = new Vector<MemoryObject>();
      }
      
      public int size()
      {
         return list.size();
      }
      
      public void clean()
      {
         for(int i = 0; i < list.size(); i++)
         {
            if(list.elementAt(i).isExpired())
            {
               list.removeElementAt(i);
               i--;
            }
         }
      }
      
      public void increment()
      {
         for(int i = 0; i < list.size(); i++)
         {
            list.elementAt(i).increment();
         }
      }
      
      public void notice(Actor a)
      {
         for(int i = 0; i < list.size(); i++)
         {
            if(list.elementAt(i).actor == a)
            {
               list.elementAt(i).notice();
               return;
            }
         }
         list.add(new MemoryObject(a));
      }
      
      public Actor getActorAt(int i)
      {
         return list.elementAt(i).actor;
      }
   }  // end of MemoryList
   
   
   private class MemoryObject
   {
      public Actor actor;
      public int turnsSinceNoticed;
      
      public MemoryObject(Actor a)
      {
         actor = a;
         turnsSinceNoticed = 0;
      }
      
      public void increment()
      {
         turnsSinceNoticed++;
      }
      
      public boolean isExpired()
      {
         return turnsSinceNoticed >= memoryDuration || actor.isDead();
      }
      
      public void notice()
      {
         turnsSinceNoticed = 0;
      }
   }  // end of MemoryObject
}