package dev.cammiescorner.arcanuscontinuum.datagen.common;

import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusBlockTags;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ArcanusBlockTagsProvider extends FabricTagProvider.BlockTagProvider {

	public ArcanusBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider arg) {
		getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
			.add(Blocks.CHISELED_BOOKSHELF)
			.add(ArcanusBlocks.ARCANE_WORKBENCH.get())
			.add(ArcanusBlocks.MAGIC_BLOCK.get());

		getOrCreateTagBuilder(BlockTags.WITHER_IMMUNE)
			.add(ArcanusBlocks.UNBREAKABLE_MAGIC_BLOCK.get())
			.add(ArcanusBlocks.SPATIAL_RIFT_EXIT.get())
			.add(ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get());

		getOrCreateTagBuilder(BlockTags.DRAGON_IMMUNE)
			.add(ArcanusBlocks.UNBREAKABLE_MAGIC_BLOCK.get())
			.add(ArcanusBlocks.SPATIAL_RIFT_EXIT.get())
			.add(ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get());

		// Ender Dragon cannot destroy blocks with this tag, but can fly straight through.
		getOrCreateTagBuilder(BlockTags.DRAGON_TRANSPARENT)
			.add(ArcanusBlocks.MAGIC_BLOCK.get());

		// Blocks in this tag do not let fluids or honey drip through.
		getOrCreateTagBuilder(BlockTags.IMPERMEABLE)
			.add(ArcanusBlocks.MAGIC_BLOCK.get())
			.add(ArcanusBlocks.UNBREAKABLE_MAGIC_BLOCK.get())
			.add(ArcanusBlocks.SPATIAL_RIFT_EXIT.get())
			.add(ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get());

		getOrCreateTagBuilder(ConventionalBlockTags.MOVEMENT_RESTRICTED)
			.add(ArcanusBlocks.MAGIC_BLOCK.get())
			.add(ArcanusBlocks.UNBREAKABLE_MAGIC_BLOCK.get())
			.add(ArcanusBlocks.SPATIAL_RIFT_EXIT.get())
			.add(ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get());

		getOrCreateTagBuilder(ArcanusBlockTags.WARDING_NOT_ALLOWED)
			.add(Blocks.PISTON_HEAD)
			.add(Blocks.MOVING_PISTON)
			.add(ArcanusBlocks.MAGIC_BLOCK.get());
	}
}
