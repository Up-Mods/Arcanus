package dev.cammiescorner.arcanus.fabric.datagen;

import dev.cammiescorner.arcanus.fabric.common.compat.ArcanusCompat;
import dev.cammiescorner.arcanus.fabric.common.util.datagen.DynamicRegistryEntryProvider;
import dev.cammiescorner.arcanus.fabric.datagen.client.ArcanusEnglishLanguageProvider;
import dev.cammiescorner.arcanus.fabric.datagen.client.ArcanusModelProvider;
import dev.cammiescorner.arcanus.fabric.datagen.common.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;

public class ArcanusDataGenerator implements DataGeneratorEntrypoint {

	static {
		// need patchouli to generate the book
		ArcanusCompat.PATCHOULI.orThrow();
		// need pehkui to generate spell effect entries
		ArcanusCompat.PEHKUI.orThrow();
	}

	@Override
	public void buildRegistry(RegistrySetBuilder builder) {
		DynamicRegistryEntryProvider.builder()
				.add(ArcanusBiomeProvider::new)
				.add(ArcanusDamageTypeProvider::new)
				.add(ArcanusDimensionProvider::new)
				.add(ArcanusStructureProvider::new)
				.build(builder);
	}

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		var pack = generator.createPack();
		pack.addProvider(DynamicRegistryEntryProvider::getGenerator);

		var blockTags = pack.addProvider(ArcanusBlockTagsProvider::new);
		pack.addProvider((output, registriesFuture) -> new ArcanusItemTagsProvider(output, registriesFuture, blockTags));
		pack.addProvider(ArcanusBiomeTagsProvider::new);
		pack.addProvider(ArcanusDamageTagsProvider::new);
		pack.addProvider(ArcanusEntityTagsProvider::new);
		pack.addProvider(ArcanusEnchantmentTagsProvider::new);
		pack.addProvider(ArcanusDimensionTagsProvider::new);
		pack.addProvider(ArcanusAdvancementRewardProvider::new);
		pack.addProvider(ArcanusBlockLootProvider::new);
		pack.addProvider(ArcanusChestLootProvider::new);
		pack.addProvider(ArcanusRecipeProvider::new);
		pack.addProvider(ArcanusAdvancementProvider::new);
		pack.addProvider(ArcanusLevelStemProvider::new);

		pack.addProvider(ArcanusEnglishLanguageProvider::new);
		pack.addProvider(ArcanusModelProvider::new);
	}
}
