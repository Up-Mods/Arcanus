package dev.cammiescorner.arcanuscontinuum.common.effects;

import dev.cammiescorner.arcanuscontinuum.common.packets.s2c.SyncStatusEffectPacket;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class ArcanusStatusEffect extends MobEffect {
	private final boolean shouldSync;
	private final boolean shouldTick;

	public ArcanusStatusEffect(MobEffectCategory type, int color, boolean shouldSync, boolean shouldTick) {
		super(type, color);
		this.shouldSync = shouldSync;
		this.shouldTick = shouldTick;
	}

	public ArcanusStatusEffect(MobEffectCategory type, int color) {
		this(type, color, false, false);
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
		super.addAttributeModifiers(entity, attributes, amplifier);

		if(entity.level() instanceof ServerLevel) {
			if(shouldSync)
				SyncStatusEffectPacket.sendToAll(entity, this, true);

			if(this == ArcanusMobEffects.FLOAT.get())
				entity.setNoGravity(true);
		}
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
		super.removeAttributeModifiers(entity, attributes, amplifier);

		if(entity.level() instanceof ServerLevel) {
			if(shouldSync)
				SyncStatusEffectPacket.sendToAll(entity, this, false);

			if(this == ArcanusMobEffects.FLOAT.get())
				entity.setNoGravity(false);
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return shouldTick;
	}
}
