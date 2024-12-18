package dev.cammiescorner.arcanuscontinuum.common.registry;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ArcanusDamageTypes {
	public static final ResourceKey<DamageType> MAGIC = ResourceKey.create(Registries.DAMAGE_TYPE, Arcanus.id("magic"));
	public static final ResourceKey<DamageType> MAGIC_PROJECTILE = ResourceKey.create(Registries.DAMAGE_TYPE, Arcanus.id("magic_projectile"));

	public static DamageSource getMagicDamage(Entity source) {
		return create(source.level(), MAGIC, source);
	}

	public static DamageSource getMagicDamage(Projectile source, @Nullable Entity attacker) {
		return create(source.level(), MAGIC_PROJECTILE, source, attacker);
	}

	public static DamageSource create(Level world, ResourceKey<DamageType> key, Entity attacker) {
		return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), attacker);
	}

	public static DamageSource create(Level world, ResourceKey<DamageType> key, Projectile source, @Nullable Entity attacker) {
		return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), source, attacker);
	}
}
