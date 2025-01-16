package dev.cammiescorner.arcanus.fabric.common.registry;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.Nullable;

public class ArcanusDamageTypes {
	public static final ResourceKey<DamageType> MAGIC = ResourceKey.create(Registries.DAMAGE_TYPE, FabricMain.id("magic"));
	public static final ResourceKey<DamageType> MAGIC_PROJECTILE = ResourceKey.create(Registries.DAMAGE_TYPE, FabricMain.id("magic_projectile"));

	public static DamageSource getMagicDamage(Entity source) {
		return source.damageSources().source(MAGIC, source);
	}

	public static DamageSource getMagicProjectileDamage(Projectile direct, @Nullable Entity trueSource) {
		return direct.damageSources().source(MAGIC_PROJECTILE, direct, trueSource);
	}
}
