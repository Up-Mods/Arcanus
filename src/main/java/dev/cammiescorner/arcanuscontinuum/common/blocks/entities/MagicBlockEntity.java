package dev.cammiescorner.arcanuscontinuum.common.blocks.entities;

import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class MagicBlockEntity extends BlockEntity {

	public MagicBlockEntity(BlockPos pos, BlockState state) {
		super(ArcanusBlockEntities.MAGIC_BLOCK.get(), pos, state);
	}
}
