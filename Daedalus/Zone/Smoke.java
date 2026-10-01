package Daedalus.Zone;

import Daedalus.GUI.*;
import java.awt.image.*;

public class Smoke implements GUIConstants
{
   public static final BufferedImage STANDARD_IMAGE = SQUARE_PALETTE.getTile(' ', WHITE, SMOKE);
   
	private int intensity;
	private BufferedImage image;


	public int getIntensity(){return intensity;}
	public BufferedImage getImage(){return image;}


	public void setIntensity(int i){intensity = i;}
	public void setImage(BufferedImage i){image = i;}


   public Smoke(int duration)
   {
      intensity = duration;
      image = STANDARD_IMAGE;
   }
   
   public void increment()
   {
      intensity--;
   }
   
   public boolean isExpired()
   {
      return intensity <= 0;
   }
}