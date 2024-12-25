package dev.cammiescorner.arcanuscontinuum.mixin.common;

import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnderDragonPart.class)
public abstract class EnderDragonPartMixin implements Targetable {

	@Shadow
	@Final
	public EnderDragon parentMob;

	@Override
	public boolean arcanuscontinuum$canBeTargeted() {
		return parentMob.arcanuscontinuum$canBeTargeted();
	}
}
