package dev.cammiescorner.arcanus.fabric.common.blocks;

import dev.cammiescorner.arcanus.fabric.common.blocks.entities.SpatialRiftWallBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SpatialRiftWallBlock extends Block implements EntityBlock {
	public SpatialRiftWallBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.BEDROCK)
			.sound(SoundType.GLASS)
			.lightLevel(value -> 12)
			.noOcclusion()
			.isValidSpawn(Blocks::never)
			.isRedstoneConductor(Blocks::never)
			.isSuffocating(Blocks::never)
			.isViewBlocking(Blocks::never)
		);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return true;
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
		return 1F;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SpatialRiftWallBlockEntity(pos, state);
	}
}
