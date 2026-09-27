package Daedalus.Actor;

import Daedalus.GUI.*;


public class Corpse extends ImageTile implements GUIConstants
{
	private Actor actor;


	public Actor getActor(){return actor;}


	public void setActor(Actor a){actor = a;}

   
   public Corpse(Actor a)
   {
      super(SQUARE_PALETTE, '%', RED, TRANSPARENT);
      actor = a;
   }
}