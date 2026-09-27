/*******************************************************************************************

A simple mathematical vector system for WidlerSuite. Plays nicely with Coord.

Copyright 2019 Michael Widler
Free for private or public use. No warranty is implied or expressed.

*******************************************************************************************/

package Daedalus.Engine;


public class Vect
{
   public double angle;        //	in radians
   public double magnitude;
   
   public final static double FULL_CIRCLE = 2 * Math.PI;                // 360 degrees
   public final static double THREE_QUARTER_CIRCLE = 3 * (Math.PI / 2); // 270 degrees
   public final static double HALF_CIRCLE = Math.PI;                    // 180 degrees
   public final static double QUARTER_CIRCLE = Math.PI / 2;             // 90 degrees
   
   // empty constructor
   public Vect()
   {
      angle = 0.0;
      magnitude = 0;
   }
   
   // double constructor
   public Vect(double theta, double length)
   {
      this.set(theta, length);
   }
   
   // double/int constructor
   public Vect(double theta, int length)
   {
      this.set(theta, (double)length);
   }
   
   // constructor that converts a Coord
   public Vect(Coord cart)
   {
      this.set(cart);
   }
   
   // copy constructor
   public Vect(Vect that)
   {
      this.angle = that.angle;
      this.magnitude = that.magnitude;
   }
   
   // constructor that returns the Vect between two points
   public Vect(Coord origin, Coord terminus)
   {
      this.set(origin, terminus);
   }
   
   // returns a deep copy of this
   public Vect copy()
   {
      return new Vect(this);
   }
   
   ///////////////////////////////////////////////////
   //	setters
   
   public void set(double a, double m){angle = a; magnitude = m;}
   public void set(double a, int m){angle = a; magnitude = (double)m;}
   public void set(Vect that){this.angle = that.angle; this.magnitude = that.magnitude;}
   
   //	sets the values of this Vect equal to the difference of the passed coords
   public void set(Coord origin, Coord terminus)
   {
      this.set(new Coord(terminus.x - origin.x, terminus.y - origin.y));
   }
   
   //	converts the values of the passed Coord to a Vect, then sets this Vect to those
   public void set(Coord cart)
   {
      double x = Math.abs(cart.x);
      double y = Math.abs(cart.y);
      this.magnitude = Math.sqrt( (x * x) + (y * y) );
      
      try
      {
         this.angle = Math.atan( (double)(-1.0 * cart.y) / (double)cart.x);
         if(cart.x < 0)
            angle += HALF_CIRCLE;
         angle = simplifyAngle(angle);
      }
      catch(ArithmeticException aEx)
      {
         if(cart.y > 0)
            angle = THREE_QUARTER_CIRCLE;
         else
            angle = QUARTER_CIRCLE;
      }
   }
   
   /////////////////////////////////////////////////
   //	Getters.  Most common getters are unnecessary, as
   //		both data members are public
   
   public int getX()
   {
      return (int)Math.round(Math.cos(this.angle) * this.magnitude);
   }
   
   public int getY()
   {
      return (int)Math.round((Math.sin(this.angle) * this.magnitude * -1));
   }
   
   public double getXAsDouble()
   {
      return Math.cos(this.angle) * this.magnitude;
   }
   
   public double getYAsDouble()
   {
      return (Math.sin(this.angle) * this.magnitude * -1);
   }
   
   /////////////////////////////////////////////
   //	Other methods
   
   // loss of precision may result if the magnitudes are small, as Coord members are all int.
   public void add(Vect that)
   {
      Coord tempCoord = new Coord(this);
      tempCoord.add(new Coord(that));
      this.set(tempCoord);
   }
   
   // adds a Coord
   public void add(Coord that)
   {
      this.add(new Vect(that));
   }
   
   // checks for equality
   public boolean equals(Vect that)
   {
      if(this.angle == that.angle && this.magnitude == that.magnitude)
         return true;
      else
         return false;
   }
   
   // returns the equivalent Coord
   public Coord getAsCoord()
   {
      return new Coord(this);
   }
   
   //	removes full circles (including negative circles) from an angle
   private double simplifyAngle(double angle)
   {
      while(angle <= 0.0)
         angle += FULL_CIRCLE;
      return angle % FULL_CIRCLE;
   }
   
}
