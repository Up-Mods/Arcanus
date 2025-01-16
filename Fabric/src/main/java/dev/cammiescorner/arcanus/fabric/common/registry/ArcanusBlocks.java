package dev.cammiescorner.arcanus.fabric.common.registry;

import dev.cammiescorner.arcanus.fabric.common.blocks.*;
import dev.cammiescorner.fabric.common.blocks.*;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.cammiescorner.arcanus.common.blocks.*;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

public class ArcanusBlocks {
	public static final RegistryHandler<Block> BLOCKS = RegistryHandler.create(Registries.BLOCK, FabricMain.MOD_ID);

	public static final RegistrySupplier<Block> MAGIC_DOOR = BLOCKS.register("magic_door", MagicDoorBlock::new);
	public static final RegistrySupplier<Block> ARCANE_WORKBENCH = BLOCKS.register("arcane_workbench", ArcaneWorkbenchBlock::new);
	public static final RegistrySupplier<Block> MAGIC_BLOCK = BLOCKS.register("magic_block", MagicBlock::new);
	public static final RegistrySupplier<Block> SPATIAL_RIFT_WALL = BLOCKS.register("spatial_rift_wall", SpatialRiftWallBlock::new);
	public static final RegistrySupplier<Block> SPATIAL_RIFT_EXIT_EDGE = BLOCKS.register("spatial_rift_exit_edge", SpatialRiftExitEdgeBlock::new);
	public static final RegistrySupplier<Block> SPATIAL_RIFT_EXIT = BLOCKS.register("spatial_rift_exit", SpatialRiftExitBlock::new);
}
