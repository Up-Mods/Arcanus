package dev.cammiescorner.arcanuscontinuum.datagen.common;

import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.world.level.block.Blocks;

public class ArcanusBlockLootProvider extends FabricBlockLootTableProvider {

	public ArcanusBlockLootProvider(FabricDataOutput dataOutput) {
		super(dataOutput);
	}

	@Override
	public void generate() {
		dropSelf(Blocks.CHISELED_BOOKSHELF);
		dropSelf(ArcanusBlocks.ARCANE_WORKBENCH.get());
		createDoorTable(ArcanusBlocks.MAGIC_DOOR.get());
	}
}
