package dev.cammiescorner.arcanuscontinuum.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.MagicProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractArrow.class)
public abstract class PersistentProjectileEntityMixin extends Projectile {

	public PersistentProjectileEntityMixin(EntityType<? extends Projectile> entityType, Level world) {
		super(entityType, world);
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;canHarmPlayer(Lnet/minecraft/world/entity/player/Player;)Z"))
	private boolean ignorePvpFlag(boolean original) {
		return original || ((Object) this) instanceof MagicProjectileEntity;
	}
}
