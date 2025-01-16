package dev.cammiescorner.arcanus.fabric.mixin.common;

import dev.cammiescorner.arcanus.fabric.common.entities.magic.AggressorbEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ThrowableProjectile.class)
public abstract class ThrownEntityMixin extends Projectile {
	public ThrownEntityMixin(EntityType<? extends Projectile> entityType, Level world) {
		super(entityType, world);
	}

	@SuppressWarnings("ConstantValue")
	@ModifyArg(method = "tick", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;"
	))
	private double arcanus$noWaterDrag(double value) {
		return ((Object) this) instanceof AggressorbEntity && this.isInWater() ? 0.99f : value;
	}
}
