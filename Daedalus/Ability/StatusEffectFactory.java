package Daedalus.Ability;

import java.util.*;
import Daedalus.Actor.*;

public class StatusEffectFactory implements AbilityConstants
{
   public static StatusEffect getHealing(int intensity)
   {
      StatusEffect se = new StatusEffect("Healing");
      se.addTag(StatusEffectTag.HEALING);
      se.setIntensity(intensity);
      return se;
   }
   public static StatusEffect getHealing(){return getHealing(2);}
   
   
   public static StatusEffect getHasted()
   {
      StatusEffect se = new StatusEffect("Hasted");
	   se.getStatBlock().setMoveSpeed(ActorConstants.ActionSpeed.FAST);
	   se.getStatBlock().setAttackSpeed(ActorConstants.ActionSpeed.FAST);
	   se.getStatBlock().setInteractSpeed(ActorConstants.ActionSpeed.FAST);
      se.addTag(StatusEffectTag.HASTED);
      return se;
   }
   
   
   public static StatusEffect getSlowed()
   {
      StatusEffect se = new StatusEffect("Slowed");
	   se.getStatBlock().setMoveSpeed(ActorConstants.ActionSpeed.SLOW);
	   se.getStatBlock().setAttackSpeed(ActorConstants.ActionSpeed.SLOW);
	   se.getStatBlock().setInteractSpeed(ActorConstants.ActionSpeed.SLOW);
      se.addTag(StatusEffectTag.SLOWED);
      return se;
   }
   
   
   public static StatusEffect getEntangled()
   {
      StatusEffect se = new StatusEffect("Entangled");
	   se.getStatBlock().setMoveSpeed(ActorConstants.ActionSpeed.SLOW);
	   se.getStatBlock().setInteractSpeed(ActorConstants.ActionSpeed.SLOW);
      se.addTag(StatusEffectTag.ENTANGLED);
      return se;
   }
   
   
   public static StatusEffect getBurning(int intensity)
   {
      StatusEffect se = new StatusEffect("Burning");
      se.setIntensity(intensity);
      se.addTag(StatusEffectTag.BURNING);
      return se;
   }
   public static StatusEffect getBurning(){return getBurning(2);}
   
   
   public static StatusEffect getVulnerable(int intensity)
   {
      StatusEffect se = new StatusEffect("Vulnerable");
      se.setIntensity(intensity);
      se.addTag(StatusEffectTag.VULNERABLE);
      return se;
   }
   public static StatusEffect getVulnerable(){return getVulnerable(2);}

}