package dev.cammiescorner.arcanuscontinuum.common.entities;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;

import java.util.UUID;

public interface Summon extends NeutralMob {
	LivingEntity getCaster();

	UUID getOwnerId();

	void setOwner(LivingEntity entity);

	boolean wantsToAttack(LivingEntity target, LivingEntity caster);
}
