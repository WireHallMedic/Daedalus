package Daedalus.Zone;

import Daedalus.GUI.*;
import Daedalus.Combat.*;
import java.awt.image.*;

public class Fire implements GUIConstants
{
   public static final int[] FG_COLOR_LIST = getColorList();
   public static final Damage STANDARD_DAMAGE = new Damage(CombatConstants.DamageType.FIRE, 2);
   
	private int intensity;
   private Damage damage;


	public int getIntensity(){return intensity;}
   public Damage getDamage(){return damage;}
   public boolean hasDamage(){return damage != null;}


	public void setIntensity(int i){intensity = i;}
   public void setDamage(Damage d){damage = d;}


   public Fire(int duration)
   {
      intensity = duration;
      damage = STANDARD_DAMAGE;
   }
   
   public void increment()
   {
      intensity--;
   }
   
   public boolean isExpired()
   {
      return intensity <= 0;
   }
   
   public int getTileIndex()
   {
      return '^';
   }
   
   public int getFGColor()
   {
      return FG_COLOR_LIST[(int)(AnimationManager.getMediumPulse() * MEDIUM_BLINK_SPEED)];
   }
   
   
   public static int[] getColorList()
   {
      return AnimationScriptFactory.getColorGradient(VIVID_ORANGE, VIVID_YELLOW, MEDIUM_BLINK_SPEED + 1);
   }
}