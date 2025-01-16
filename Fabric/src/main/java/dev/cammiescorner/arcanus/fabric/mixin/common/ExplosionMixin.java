package dev.cammiescorner.arcanus.fabric.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Explosion.class)
public class ExplosionMixin {
	@ModifyExpressionValue(method = "explode", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
	))
	private boolean arcanus$captureDamageReturn(boolean original, @Share("tookDamage") LocalBooleanRef ref) {
		ref.set(original);
		return original;
	}

	@ModifyExpressionValue(method = "explode", slice = @Slice(from = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
	)), at = @At(
			value = "NEW",
			target = "(DDD)Lnet/minecraft/world/phys/Vec3;"
	))
	private Vec3 arcanus$noKnockback(Vec3 original, @Share("tookDamage") LocalBooleanRef ref) {
		return !ref.get() ? Vec3.ZERO : original;
	}
}
