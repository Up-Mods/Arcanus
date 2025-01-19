package dev.cammiescorner.arcanuscontinuum.mixin.common;

import dev.cammiescorner.arcanuscontinuum.common.entities.magic.ManaShieldEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EntityGetter.class)
public interface EntityGetterMixin {
	@Inject(method = "getEntityCollisions", at = @At("HEAD"))
	private void arcanuscontinuum$collidesWithHead(@Nullable Entity entity, AABB box, CallbackInfoReturnable<List<VoxelShape>> info) {
		ManaShieldEntity.COLLIDING_ENTITY.set(entity);
	}

	@Inject(method = "getEntityCollisions", at = @At("RETURN"))
	private void arcanuscontinuum$collidesWithReturn(@Nullable Entity entity, AABB box, CallbackInfoReturnable<List<VoxelShape>> info) {
		ManaShieldEntity.COLLIDING_ENTITY.remove();
	}
}
