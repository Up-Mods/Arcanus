package dev.cammiescorner.arcanuscontinuum.mixin.common;

import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity {
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) { super(entityType, world); }

	@Inject(method = "doHurtTarget", at = @At("HEAD"), cancellable = true)
	private void arcanuscontinuum$tryAttack(Entity target, CallbackInfoReturnable<Boolean> info) {
		if(ArcanusComponents.getStunTimer(this) > 0) {
			info.setReturnValue(false);
		}
	}
}
