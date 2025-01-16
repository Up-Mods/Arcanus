package dev.cammiescorner.arcanus.fabric.mixin.common;

import dev.cammiescorner.arcanus.fabric.common.entities.magic.ManaShieldEntity;
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
public interface EntityViewMixin {
	@Inject(method = "getEntityCollisions", at = @At("HEAD"))
	private void arcanus$collidesWithHead(@Nullable Entity entity, AABB box, CallbackInfoReturnable<List<VoxelShape>> info) {
		ManaShieldEntity.COLLIDING_ENTITY.set(entity);
	}

	@Inject(method = "getEntityCollisions", at = @At("RETURN"))
	private void arcanus$collidesWithReturn(@Nullable Entity entity, AABB box, CallbackInfoReturnable<List<VoxelShape>> info) {
		ManaShieldEntity.COLLIDING_ENTITY.remove();
	}
}
