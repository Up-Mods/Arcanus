package dev.cammiescorner.arcanuscontinuum.datagen.common;

import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusBiomes;
import dev.cammiescorner.arcanuscontinuum.common.util.datagen.DynamicRegistryEntryProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.*;

public class ArcanusBiomeProvider extends DynamicRegistryEntryProvider {

	@Override
	protected void generate(RegistrySetBuilder builder) {
		builder.add(Registries.BIOME, context -> {
			var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
			var worldCarvers = context.lookup(Registries.CONFIGURED_CARVER);

			context.register(ArcanusBiomes.POCKET_DIMENSION, new Biome.BiomeBuilder().hasPrecipitation(false).temperature(0.5F).downfall(0.5F).specialEffects(new BiomeSpecialEffects.Builder().waterColor(0x3F76E4).waterFogColor(0x050533).fogColor(0xC0D8FF).skyColor(0x000000).build()).mobSpawnSettings(new MobSpawnSettings.Builder().build()).generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers).build()).build());
		});
	}
}
