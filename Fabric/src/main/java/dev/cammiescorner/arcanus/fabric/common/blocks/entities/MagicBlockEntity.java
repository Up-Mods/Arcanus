package dev.cammiescorner.arcanus.fabric.common.blocks.entities;

import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MagicBlockEntity extends AbstractMagicBlockEntity {

	public MagicBlockEntity(BlockPos pos, BlockState state) {
		super(ArcanusBlockEntities.MAGIC_BLOCK.get(), pos, state);
	}
}
