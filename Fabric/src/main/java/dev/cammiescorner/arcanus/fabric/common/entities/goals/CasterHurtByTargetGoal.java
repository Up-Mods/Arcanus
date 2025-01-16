package dev.cammiescorner.arcanus.fabric.common.entities.goals;

import dev.cammiescorner.arcanus.fabric.common.entities.Summon;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class CasterHurtByTargetGoal<T extends Mob & Summon> extends TargetGoal {
	private final T summon;
	private LivingEntity ownerLastHurtBy;
	private int timestamp;

	public CasterHurtByTargetGoal(T tameAnimal) {
		super(tameAnimal, false);
		this.summon = tameAnimal;
		this.setFlags(EnumSet.of(Flag.TARGET));
	}

	public boolean canUse() {
		LivingEntity caster = summon.getCaster();

		if(caster == null) {
			return false;
		}
		else {
			ownerLastHurtBy = caster.getLastHurtByMob();
			int i = caster.getLastHurtByMobTimestamp();
			return i != timestamp && canAttack(ownerLastHurtBy, TargetingConditions.DEFAULT) && summon.wantsToAttack(ownerLastHurtBy, caster);
		}
	}

	public void start() {
		LivingEntity caster = summon.getCaster();
		mob.setTarget(ownerLastHurtBy);

		if(caster != null)
			timestamp = caster.getLastHurtByMobTimestamp();

		super.start();
	}
}
