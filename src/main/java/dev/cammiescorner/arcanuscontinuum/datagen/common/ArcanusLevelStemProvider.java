package dev.cammiescorner.arcanuscontinuum.datagen.common;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.function.BiConsumer;

public class ArcanusLevelStemProvider extends FabricCodecDataProvider<LevelStem> {

	public ArcanusLevelStemProvider(FabricDataOutput dataOutput) {
		super(dataOutput, PackOutput.Target.DATA_PACK, Registries.LEVEL_STEM.location().getPath(), LevelStem.CODEC);
	}

	@Override
	protected void configure(BiConsumer<ResourceLocation, LevelStem> provider) {
//		provider.accept(ArcanusDimensions.POCKET_DIMENSION.location(), new LevelStem(dimensionTypes.getOrThrow(ArcanusDimensionTypes.POCKET_DIMENSION), new FlatLevelSource(new FlatLevelGeneratorSettings(Optional.empty(), biomes.getOrThrow(Biomes.THE_VOID), List.of()))));

//		builder.add(Registries.LEVEL_STEM, context -> {
//			var dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
//			var biomes = context.lookup(Registries.BIOME);
//			context.register(Registries.levelToLevelStem(ArcanusDimensions.POCKET_DIMENSION), new LevelStem(dimensionTypes.getOrThrow(ArcanusDimensionTypes.POCKET_DIMENSION), new FlatLevelSource(new FlatLevelGeneratorSettings(Optional.empty(), biomes.getOrThrow(Biomes.THE_VOID), List.of()))));
//		});
	}

	@Override
	public String getName() {
		return Arcanus.id("level_stems").toString();
	}
}
