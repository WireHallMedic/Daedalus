package Daedalus.Actor;

import Daedalus.GUI.*;


public class Corpse extends ImageTile implements GUIConstants
{
	private Actor actor;
   private String name;


	public Actor getActor(){return actor;}
   public String setName(){return name;}


	public void setActor(Actor a){actor = a;}
   public void setName(String n){name = n;}

   
   public Corpse(Actor a)
   {
      super(SQUARE_PALETTE, '%', RED, TRANSPARENT);
      actor = a;
      setName(a.getName() + " Corpse");
   }
}