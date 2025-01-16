package dev.cammiescorner.arcanus.fabric.datagen.common;

import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusDamageTypes;
import dev.cammiescorner.arcanus.fabric.common.util.datagen.DynamicRegistryEntryProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class ArcanusDamageTypeProvider extends DynamicRegistryEntryProvider {

	@Override
	protected void generate(RegistrySetBuilder builder) {
		builder.add(Registries.DAMAGE_TYPE, bootstapContext -> {
			bootstapContext.register(ArcanusDamageTypes.MAGIC, new DamageType("arcanus.magic", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.2f));
			bootstapContext.register(ArcanusDamageTypes.MAGIC_PROJECTILE, new DamageType("arcanus.magic_projectile", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.2f));
		});
	}
}
