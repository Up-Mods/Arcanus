package dev.cammiescorner.arcanuscontinuum.datagen.common;

import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusDimensionTypes;
import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusDimensions;
import dev.cammiescorner.arcanuscontinuum.common.util.datagen.DynamicRegistryEntryProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class ArcanusDimensionProvider extends DynamicRegistryEntryProvider {

	@Override
	protected void generate(RegistrySetBuilder builder) {
		builder.add(Registries.DIMENSION_TYPE, context -> {
			context.register(ArcanusDimensionTypes.POCKET_DIMENSION, new DimensionType(OptionalLong.of(12000), false, false, false, false, 1.0D, false, false, -256, 512, 512, BlockTags.INFINIBURN_OVERWORLD, BuiltinDimensionTypes.OVERWORLD_EFFECTS, 15.0F, new DimensionType.MonsterSettings(true, false, ConstantInt.of(0), 0)));
		});
		builder.add(Registries.LEVEL_STEM, context -> {
			var dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
			var biomes = context.lookup(Registries.BIOME);
			context.register(Registries.levelToLevelStem(ArcanusDimensions.POCKET_DIMENSION), new LevelStem(dimensionTypes.getOrThrow(ArcanusDimensionTypes.POCKET_DIMENSION), new FlatLevelSource(new FlatLevelGeneratorSettings(Optional.empty(), biomes.getOrThrow(Biomes.THE_VOID), List.of()))));
		});
	}
}
