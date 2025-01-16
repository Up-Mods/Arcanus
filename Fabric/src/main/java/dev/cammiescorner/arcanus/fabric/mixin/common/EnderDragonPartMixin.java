package dev.cammiescorner.arcanus.fabric.mixin.common;

import dev.cammiescorner.arcanus.api.entities.Targetable;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnderDragonPart.class)
public abstract class EnderDragonPartMixin implements Targetable {
	@Shadow @Final public EnderDragon parentMob;

	@Override
	public boolean arcanus$canBeTargeted() {
		return parentMob.arcanus$canBeTargeted();
	}
}
