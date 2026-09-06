package Daedalus.Ability;

public class Ability implements AbilityConstants
{
	private String name;
	private TargetingType targetingType;
	private int range;


	public String getName(){return name;}
	public TargetingType getTargetingType(){return targetingType;}
	public int getRange(){return range;}


	public void setName(String n){name = n;}
	public void setTargetingType(TargetingType t){targetingType = t;}
	public void setRange(int r){range = r;}

   public Ability(String n)
   {
      name = n;
      targetingType = TargetingType.POINT;
      range = 10;
   }
}