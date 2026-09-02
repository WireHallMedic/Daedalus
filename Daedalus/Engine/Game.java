package Daedalus.Engine;

import Daedalus.Zone.*;
import Daedalus.Actor.*;

public class Game
{
	private static ZoneMap curZone = null;
	private static Actor player = null;


	public static ZoneMap getCurZone(){return curZone;}
	public static Actor getPlayer(){return player;}


	public static void setCurZone(ZoneMap c){curZone = c;}
	public static void setPlayer(Actor p){player = p;}


}