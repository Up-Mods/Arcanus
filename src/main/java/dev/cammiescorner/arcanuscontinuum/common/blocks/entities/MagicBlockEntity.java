package dev.cammiescorner.arcanuscontinuum.common.blocks.entities;

import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MagicBlockEntity extends BlockEntity {

	public MagicBlockEntity(BlockPos pos, BlockState state) {
		super(ArcanusBlockEntities.MAGIC_BLOCK.get(), pos, state);
	}
}
