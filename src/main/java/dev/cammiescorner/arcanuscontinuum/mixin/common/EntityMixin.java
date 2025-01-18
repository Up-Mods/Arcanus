package dev.cammiescorner.arcanuscontinuum.mixin.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class EntityMixin {
	@Shadow private Vec3 deltaMovement;
	private Entity self = (Entity) (Object) this;

	@ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
	private Vec3 modifyMotion(Vec3 value) {
		return value.scale(0.5);
	}

//	@WrapOperation(method = "saveWithoutId", at = @At(
//		value = "INVOKE",
//		target = "Lnet/minecraft/world/entity/Entity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;"
//	))
//	private Vec3 saveDeltaMovementField(Entity instance, Operation<Vec3> original) {
//		return deltaMovement;
//	}
//
//	@ModifyReturnValue(method = "isNoGravity", at = @At("RETURN"))
//	private boolean noGrav(boolean original) {
//		return !(self instanceof Player);
//	}
//
//	@ModifyReturnValue(method = "getDeltaMovement", at = @At("RETURN"))
//	private Vec3 slowMovement(Vec3 original) {
//		return self instanceof Player ? original : original.scale(0.5);
//	}
//
//	@WrapOperation(method = "addDeltaMovement", at = @At(
//		value = "INVOKE",
//		target = "Lnet/minecraft/world/entity/Entity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;"
//	))
//	private Vec3 painPainPain(Entity instance, Operation<Vec3> original) {
//		return deltaMovement;
//	}
}
