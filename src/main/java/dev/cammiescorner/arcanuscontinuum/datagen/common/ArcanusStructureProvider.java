package dev.cammiescorner.arcanuscontinuum.datagen.common;

import com.mojang.datafixers.util.Pair;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.data.*;
import dev.cammiescorner.arcanuscontinuum.common.structures.WizardTowerProcessor;
import dev.cammiescorner.arcanuscontinuum.common.util.datagen.DynamicRegistryEntryProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.Structures;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;

public class ArcanusStructureProvider extends DynamicRegistryEntryProvider {

	@Override
	protected void generate(RegistrySetBuilder builder) {

		builder.add(Registries.PROCESSOR_LIST, context -> {
			context.register(ArcanusStructureProcessors.WIZARD_TOWER_PROCESSORS, new StructureProcessorList(List.of(new WizardTowerProcessor())));
		});

		builder.add(Registries.TEMPLATE_POOL, context -> {
			var pools = context.lookup(Registries.TEMPLATE_POOL);
			var processorLists = context.lookup(Registries.PROCESSOR_LIST);

			var emptyPool = pools.getOrThrow(Pools.EMPTY);
			var wizardTowerProcessors = processorLists.getOrThrow(ArcanusStructureProcessors.WIZARD_TOWER_PROCESSORS);

			context.register(ArcanusStructurePools.WIZARD_TOWER, new StructureTemplatePool(emptyPool, List.of(Pair.of(StructurePoolElement.single(Arcanus.id("wizard_tower").toString(), wizardTowerProcessors), 1)), StructureTemplatePool.Projection.RIGID));
		});

		builder.add(Registries.STRUCTURE, context -> {
			var biomes = context.lookup(Registries.BIOME);
			var pools = context.lookup(Registries.TEMPLATE_POOL);

			context.register(ArcanusStructures.WIZARD_TOWER, new JigsawStructure(
				Structures.structure(biomes.getOrThrow(ArcanusBiomeTags.HAS_WIZARD_TOWER), TerrainAdjustment.BEARD_THIN),
				pools.getOrThrow(ArcanusStructurePools.WIZARD_TOWER),
				1,
				ConstantHeight.of(VerticalAnchor.absolute(0)),
				false,
				Heightmap.Types.WORLD_SURFACE_WG
			));
		});

		builder.add(Registries.STRUCTURE_SET, context -> {
			var structures = context.lookup(Registries.STRUCTURE);
			context.register(ArcanusStructureSets.WIZARD_TOWER, new StructureSet(structures.getOrThrow(ArcanusStructures.WIZARD_TOWER), new RandomSpreadStructurePlacement(67, 19, RandomSpreadType.LINEAR, 203785912)));
		});
	}
}
