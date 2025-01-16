package dev.cammiescorner.arcanus.fabric.mixin.common;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerLevel.class)
public class ServerWorldMixin {
	@WrapWithCondition(method = "tickNonPassenger", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;tick()V"
	))
	private boolean arcanus$blockEntityTick(Entity entity) {
		return !ArcanusComponents.areUpdatesBlocked(entity);
	}
}
