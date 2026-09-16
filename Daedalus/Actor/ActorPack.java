package Daedalus.Actor;

import WidlerSuite.Coord;
import java.util.*;

public class ActorPack implements ActorConstants
{
	private Vector<Actor> memberList;
	private Actor leader;
	private int preferredRange;
	private int maxRange;


	public Vector<Actor> getMemberList(){return memberList;}
	public Actor getLeader(){return leader;}
   public boolean hasLeader(){return leader != null;}
	public int getPreferredRange(){return preferredRange;}
	public int getMaxRange(){return maxRange;}


	public void setMemberList(Vector<Actor> m){memberList = m;}
	public void setLeader(Actor l){leader = l;}
	public void setPreferredRange(int p){preferredRange = p;}
	public void setMaxRange(int m){maxRange = m;}


   public ActorPack()
   {
      memberList = new Vector<Actor>();
      leader = null;
   }
   
   public void addMember(Actor a)
   {
      memberList.add(a);
   }
   
   public void removeMember(Actor a)
   {
      memberList.remove(a);
   }
   
   public int size()
   {
      return memberList.size();
   }
   
   public Coord getCenterLoc()
   {
      if(hasLeader())
         return leader.getTileLoc();
      int x = 0;
      int y = 0;
      for(Actor a : memberList)
      {
         x += a.getTileLoc().x;
         y += a.getTileLoc().y;
      }
      return new Coord(x / size(), y / size());
   }
   
   public boolean isLeader(Actor a)
   {
      return a == leader;
   }
}