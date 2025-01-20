package dev.cammiescorner.arcanuscontinuum.mixin.common;

import dev.cammiescorner.arcanuscontinuum.common.entities.magic.TemporalDilationField;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Unique private Entity self = (Entity) (Object) this;
	@Shadow public abstract Vec3 position();
	@Shadow public abstract Level level();
	@Shadow public abstract AABB getBoundingBox();

	@ModifyVariable(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
	private float pitchDown(float pitch) {
		boolean isInsideTemporalField = !level().getEntities(self, getBoundingBox(), entity -> entity instanceof TemporalDilationField && !(self instanceof TemporalDilationField)).isEmpty();

		if(isInsideTemporalField)
			return pitch * 0.5f;

		return pitch;
	}
}
