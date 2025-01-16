package dev.cammiescorner.arcanus.fabric.datagen.common;

import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusDamageTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class ArcanusDamageTagsProvider extends FabricTagProvider<DamageType> {

	public ArcanusDamageTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.DAMAGE_TYPE, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider arg) {
		getOrCreateTagBuilder(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)
				.add(ArcanusDamageTypes.MAGIC)
				.add(ArcanusDamageTypes.MAGIC_PROJECTILE);

		getOrCreateTagBuilder(DamageTypeTags.IS_PROJECTILE)
				.add(ArcanusDamageTypes.MAGIC_PROJECTILE);

		getOrCreateTagBuilder(DamageTypeTags.WITCH_RESISTANT_TO)
				.add(ArcanusDamageTypes.MAGIC)
				.add(ArcanusDamageTypes.MAGIC_PROJECTILE);
	}
}
