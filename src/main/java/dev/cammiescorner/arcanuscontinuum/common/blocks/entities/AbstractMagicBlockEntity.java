package dev.cammiescorner.arcanuscontinuum.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractMagicBlockEntity extends BlockEntity {
	public AbstractMagicBlockEntity(BlockEntityType<? extends AbstractMagicBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
}
