package dev.cammiescorner.arcanuscontinuum.mixin.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow public abstract Vec3 position();

	@ModifyVariable(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
	private float pitchDown(float pitch) {
		if(position().distanceTo(new Vec3(0, 146, 0)) < 16)
			return pitch * 0.5f; // TODO tie to being in a time dilation entity

		return pitch;
	}
}
