package Daedalus.Actor;


public class StatBlock implements ActorConstants
{
	protected int maxHealth;
	protected int visionRadius;
	protected int moveSpeed;      // tracked internally as ints for stacking
	protected int attackSpeed;    // tracked internally as ints for stacking
	protected int interactSpeed;  // tracked internally as ints for stacking


	public int getMaxHealth(){return maxHealth;}
	public int getVisionRadius(){return visionRadius;}
	public ActionSpeed getMoveSpeed(){return ActionSpeed.getByModifier(moveSpeed);}
	public ActionSpeed getAttackSpeed(){return ActionSpeed.getByModifier(attackSpeed);}
	public ActionSpeed getInteractSpeed(){return ActionSpeed.getByModifier(interactSpeed);}


	public void setMaxHealth(int m){maxHealth = m;}
	public void setVisionRadius(int v){visionRadius = v;}
	public void setMoveSpeed(ActionSpeed m){moveSpeed = m.modifier;}
	public void setAttackSpeed(ActionSpeed a){attackSpeed = a.modifier;}
	public void setInteractSpeed(ActionSpeed i){interactSpeed = i.modifier;}

   public StatBlock()
   {
      maxHealth = 10;
      visionRadius = 10;
      moveSpeed = ActionSpeed.NORMAL.modifier;
      attackSpeed = ActionSpeed.NORMAL.modifier;
      interactSpeed = ActionSpeed.NORMAL.modifier;
   }
   
   public void set(StatBlock that)
   {
      this.maxHealth = that.maxHealth;
      this.visionRadius = that.visionRadius;
      this.moveSpeed = that.moveSpeed;
      this.attackSpeed = that.attackSpeed;
      this.interactSpeed = that.interactSpeed;
   }
   
   public void add(StatBlock that)
   {
      this.maxHealth += that.maxHealth;
      this.visionRadius += that.visionRadius;
      
      if(this.moveSpeed == ActionSpeed.INSTANTANEOUS.modifier ||
         that.moveSpeed == ActionSpeed.INSTANTANEOUS.modifier)
         this.moveSpeed = ActionSpeed.INSTANTANEOUS.modifier;
      else
         this.moveSpeed += that.moveSpeed;
         
      if(this.attackSpeed == ActionSpeed.INSTANTANEOUS.modifier ||
         that.attackSpeed == ActionSpeed.INSTANTANEOUS.modifier)
         this.attackSpeed = ActionSpeed.INSTANTANEOUS.modifier;
      else
         this.attackSpeed += that.attackSpeed;
         
      if(this.interactSpeed == ActionSpeed.INSTANTANEOUS.modifier ||
         that.interactSpeed == ActionSpeed.INSTANTANEOUS.modifier)
         this.interactSpeed = ActionSpeed.INSTANTANEOUS.modifier;
      else
         this.interactSpeed += that.interactSpeed;
   }
}