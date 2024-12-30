package dev.cammiescorner.arcanuscontinuum.datagen.common;

import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusBiomeTags;
import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

public class ArcanusBiomeTagsProvider extends FabricTagProvider<Biome> {

	public ArcanusBiomeTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.BIOME, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider arg) {
		getOrCreateTagBuilder(ArcanusBiomeTags.C_HAS_VILLAGE)
			.forceAddTag(BiomeTags.HAS_VILLAGE_DESERT)
			.forceAddTag(BiomeTags.HAS_VILLAGE_PLAINS)
			.forceAddTag(BiomeTags.HAS_VILLAGE_SAVANNA)
			.forceAddTag(BiomeTags.HAS_VILLAGE_SNOWY)
			.forceAddTag(BiomeTags.HAS_VILLAGE_TAIGA);

		getOrCreateTagBuilder(ArcanusBiomeTags.HAS_WIZARD_TOWER)
			.addOptionalTag(ArcanusBiomeTags.C_HAS_VILLAGE)
			.forceAddTag(BiomeTags.HAS_PILLAGER_OUTPOST)
			.forceAddTag(BiomeTags.HAS_SWAMP_HUT)
			.forceAddTag(BiomeTags.HAS_WOODLAND_MANSION)
			.forceAddTag(BiomeTags.IS_HILL)
			.forceAddTag(BiomeTags.IS_MOUNTAIN)
			.forceAddTag(BiomeTags.IS_TAIGA)
			.forceAddTag(BiomeTags.IS_JUNGLE)
			.forceAddTag(BiomeTags.IS_FOREST)
			.forceAddTag(BiomeTags.IS_SAVANNA)
			.add(Biomes.MUSHROOM_FIELDS)
			.add(Biomes.ICE_SPIKES)
			.add(Biomes.SUNFLOWER_PLAINS)
			.add(Biomes.MANGROVE_SWAMP);

		getOrCreateTagBuilder(BiomeTags.WITHOUT_PATROL_SPAWNS)
			.add(ArcanusBiomes.POCKET_DIMENSION);

		getOrCreateTagBuilder(BiomeTags.WITHOUT_ZOMBIE_SIEGES)
			.add(ArcanusBiomes.POCKET_DIMENSION);

		getOrCreateTagBuilder(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS)
			.add(ArcanusBiomes.POCKET_DIMENSION);

		getOrCreateTagBuilder(ArcanusBiomeTags.IS_POCKET_DIMENSION)
			.add(ArcanusBiomes.POCKET_DIMENSION);

		getOrCreateTagBuilder(BiomeTags.MINESHAFT_BLOCKING)
			.add(ArcanusBiomes.POCKET_DIMENSION);
	}
}
