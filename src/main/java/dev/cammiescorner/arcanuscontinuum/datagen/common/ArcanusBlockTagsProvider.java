package dev.cammiescorner.arcanuscontinuum.datagen.common;

import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
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
	}
}
