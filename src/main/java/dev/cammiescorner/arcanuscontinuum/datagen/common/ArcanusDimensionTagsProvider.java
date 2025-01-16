package dev.cammiescorner.arcanuscontinuum.datagen.common;

import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusDimensionTags;
import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusDimensionTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.concurrent.CompletableFuture;

public class ArcanusDimensionTagsProvider extends FabricTagProvider<DimensionType> {

	public ArcanusDimensionTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.DIMENSION_TYPE, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider arg) {
		getOrCreateTagBuilder(ArcanusDimensionTags.WARDING_NOT_ALLOWED)
			.add(ArcanusDimensionTypes.POCKET_DIMENSION);
	}
}
