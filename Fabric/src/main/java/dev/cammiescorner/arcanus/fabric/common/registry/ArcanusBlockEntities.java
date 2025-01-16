package dev.cammiescorner.arcanus.fabric.common.registry;

import dev.cammiescorner.arcanus.fabric.common.blocks.entities.*;
import dev.cammiescorner.fabric.common.blocks.entities.*;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.cammiescorner.arcanus.common.blocks.entities.*;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ArcanusBlockEntities {
	public static final RegistryHandler<BlockEntityType<?>> BLOCK_ENTITY_TYPES = RegistryHandler.create(Registries.BLOCK_ENTITY_TYPE, Arcanus.MOD_ID);

	public static final RegistrySupplier<BlockEntityType<MagicDoorBlockEntity>> MAGIC_DOOR = BLOCK_ENTITY_TYPES.register("magic_door", () -> FabricBlockEntityTypeBuilder.create(MagicDoorBlockEntity::new, ArcanusBlocks.MAGIC_DOOR.get()).build());
	public static final RegistrySupplier<BlockEntityType<ArcaneWorkbenchBlockEntity>> ARCANE_WORKBENCH = BLOCK_ENTITY_TYPES.register("arcane_workbench", () -> FabricBlockEntityTypeBuilder.create(ArcaneWorkbenchBlockEntity::new, ArcanusBlocks.ARCANE_WORKBENCH.get()).build());
	public static final RegistrySupplier<BlockEntityType<MagicBlockEntity>> MAGIC_BLOCK = BLOCK_ENTITY_TYPES.register("magic_block", () -> FabricBlockEntityTypeBuilder.create(MagicBlockEntity::new, ArcanusBlocks.MAGIC_BLOCK.get()).build());
	public static final RegistrySupplier<BlockEntityType<SpatialRiftExitBlockEntity>> SPATIAL_RIFT_EXIT = BLOCK_ENTITY_TYPES.register("spatial_rift_exit", () -> FabricBlockEntityTypeBuilder.create(SpatialRiftExitBlockEntity::new, ArcanusBlocks.SPATIAL_RIFT_EXIT.get()).build());
	public static final RegistrySupplier<BlockEntityType<SpatialRiftWallBlockEntity>> SPATIAL_RIFT_WALL = BLOCK_ENTITY_TYPES.register("spatial_rift_wall", () -> FabricBlockEntityTypeBuilder.create(SpatialRiftWallBlockEntity::new, ArcanusBlocks.SPATIAL_RIFT_WALL.get(), ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get()).build());
}
