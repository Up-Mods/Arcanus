package dev.cammiescorner.arcanuscontinuum.common.entities.goals;

import dev.cammiescorner.arcanuscontinuum.common.entities.Summon;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class CasterHurtTargetGoal<T extends Mob & Summon> extends TargetGoal {
	private final T summon;
	private LivingEntity ownerLastHurt;
	private int timestamp;

	public CasterHurtTargetGoal(T tameAnimal) {
		super(tameAnimal, false);
		this.summon = tameAnimal;
		this.setFlags(EnumSet.of(Flag.TARGET));
	}

	public boolean canUse() {
		LivingEntity caster = this.summon.getCaster();

		if(caster == null) {
			return false;
		}
		else {
			ownerLastHurt = caster.getLastHurtMob();
			int i = caster.getLastHurtMobTimestamp();
			return i != timestamp && canAttack(ownerLastHurt, TargetingConditions.DEFAULT) && summon.wantsToAttack(ownerLastHurt, caster);
		}
	}

	public void start() {
		LivingEntity caster = summon.getCaster();
		mob.setTarget(ownerLastHurt);

		if(caster != null)
			timestamp = caster.getLastHurtMobTimestamp();

		super.start();
	}
}
