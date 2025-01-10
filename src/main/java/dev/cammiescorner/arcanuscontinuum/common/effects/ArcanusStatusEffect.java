package dev.cammiescorner.arcanuscontinuum.common.effects;

import dev.cammiescorner.arcanuscontinuum.common.packets.s2c.SyncStatusEffectPacket;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class ArcanusStatusEffect extends MobEffect {
	public ArcanusStatusEffect(MobEffectCategory type, int color) {
		super(type, color);
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
		super.addAttributeModifiers(entity, attributes, amplifier);

		if(entity.level() instanceof ServerLevel) {
			// FIXME temporal dilation no worky
			if(this == ArcanusMobEffects.ANONYMITY.get() || /*this == ArcanusStatusEffects.TEMPORAL_DILATION.get() ||*/ this == ArcanusMobEffects.MANA_WINGS.get())
				SyncStatusEffectPacket.sendToAll(entity, this, true);

			if(this == ArcanusMobEffects.FLOAT.get())
				entity.setNoGravity(true);
		}
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
		super.removeAttributeModifiers(entity, attributes, amplifier);

		if(entity.level() instanceof ServerLevel) {
			// FIXME temporal dilation no worky
			if(this == ArcanusMobEffects.ANONYMITY.get() || /*this == ArcanusStatusEffects.TEMPORAL_DILATION.get() ||*/ this == ArcanusMobEffects.MANA_WINGS.get())
				SyncStatusEffectPacket.sendToAll(entity, this, false);

			if(this == ArcanusMobEffects.FLOAT.get())
				entity.setNoGravity(false);
		}
	}

	// FIXME temporal dilation no worky
//	@Override
//	public boolean canApplyUpdateEffect(int duration, int amplifier) {
//		return this == ArcanusStatusEffects.TEMPORAL_DILATION.get();
//	}
//
//	@Override
//	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
//		World world = entity.getWorld();
//
//		if(this == ArcanusStatusEffects.TEMPORAL_DILATION.get()) {
//			float radius = 4;
//
//			List<Entity> targets = world.getOtherEntities(entity, new Box(-radius, -radius, -radius, radius, radius, radius).offset(entity.getPos().add(0, entity.getHeight() / 2, 0)), target -> target.squaredDistanceTo(entity) <= radius * radius && !(target instanceof LivingEntity livingTarget && livingTarget.hasStatusEffect(ArcanusStatusEffects.TEMPORAL_DILATION.get())));
//			int interval = MathHelper.clamp((amplifier + 1) * 2, 2, 10); // stacks to a maximum of 50% (5 temporal dilation effects)
//
//			for(Entity target : targets) {
//				ArcanusComponents.setSlowTime(target, true);
//				ArcanusComponents.setBlockUpdates(target, world.getTime() % interval != 0);
//				ArcanusComponents.setBlockUpdatesInterval(target, interval);
//
//				if(world.isClient()) {
//					if(target.isOnGround())
//						target.setVelocity(target.getVelocity().getX(), 0, target.getVelocity().getZ());
//
//					if(target instanceof LivingEntity livingEntity) {
//						livingEntity.handSwingProgress = livingEntity.lastHandSwingProgress;
//						livingEntity.setBodyYaw(livingEntity.prevBodyYaw);
//						livingEntity.setHeadYaw(livingEntity.prevHeadYaw);
//					}
//
//					target.setYaw(target.prevYaw);
//					target.setPitch(target.prevPitch);
//
//					if(ArcanusComponents.areUpdatesBlocked(target)) {
//						Vec3d pos = target.getPos().add(target.getVelocity().multiply((20d - interval) / 20d));
//
//						target.prevX = target.getX();
//						target.prevY = target.getY();
//						target.prevZ = target.getZ();
//
//						target.setPosition(pos);
//					}
//					else {
//						target.setPosition(target.getPos().add(target.getVelocity().multiply((20d - interval) / 20d)));
//
//						target.prevX = target.getX() - target.getVelocity().getX() * ((20d - interval) / 20d);
//						target.prevY = target.getY() - target.getVelocity().getY() * ((20d - interval) / 20d);
//						target.prevZ = target.getZ() - target.getVelocity().getZ() * ((20d - interval) / 20d);
//					}
//				}
//			}
//
//			List<Entity> targetsOutOfRange = world.getOtherEntities(entity, new Box(-radius - 3, -radius - 3, -radius - 3, radius + 3, radius + 3, radius + 3).offset(entity.getPos()), target -> !targets.contains(target) && !(target instanceof LivingEntity livingTarget && livingTarget.hasStatusEffect(ArcanusStatusEffects.TEMPORAL_DILATION.get())));
//			targetsOutOfRange.forEach(target -> ArcanusComponents.setBlockUpdates(target, false));
//		}
//	}
}
