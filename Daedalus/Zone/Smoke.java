package Daedalus.Zone;

import Daedalus.GUI.*;
import Daedalus.Combat.*;
import java.awt.image.*;

public class Smoke implements GUIConstants
{
   public static final BufferedImage STANDARD_IMAGE = SQUARE_PALETTE.getTile(' ', WHITE, SMOKE);
   public static final BufferedImage CAUSTIC_IMAGE = SQUARE_PALETTE.getTile(' ', WHITE, CAUSTIC_SMOKE);
   public static final Damage CAUSTIC_DAMAGE = getCausticDamage();
   
	private int intensity;
	private BufferedImage image;
   private Damage damage;


	public int getIntensity(){return intensity;}
	public BufferedImage getImage(){return image;}
   public Damage getDamage(){return damage;}
   public boolean hasDamage(){return damage != null;}


	public void setIntensity(int i){intensity = i;}
	public void setImage(BufferedImage i){image = i;}
   public void setDamage(Damage d){damage = d;}


   public Smoke(int duration)
   {
      intensity = duration;
      image = STANDARD_IMAGE;
      damage = null;
   }
   
   public static Smoke getCausticSmoke(int duration)
   {
      Smoke s = new Smoke(duration);
      s.setImage(CAUSTIC_IMAGE);
      s.setDamage(CAUSTIC_DAMAGE);
      return s;
   }
   
   public void increment()
   {
      intensity--;
   }
   
   public boolean isExpired()
   {
      return intensity <= 0;
   }
   
   private static Damage getCausticDamage()
   {
      Damage d = new Damage();
      d.setValue(CombatConstants.DamageType.CORROSION, 2);
      return d;
   }
}