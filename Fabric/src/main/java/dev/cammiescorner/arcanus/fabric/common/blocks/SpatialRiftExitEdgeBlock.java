package dev.cammiescorner.arcanus.fabric.common.blocks;

import dev.cammiescorner.arcanus.fabric.common.blocks.entities.SpatialRiftWallBlockEntity;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class SpatialRiftExitEdgeBlock extends HorizontalDirectionalBlock implements EntityBlock {
	public static final BooleanProperty CORNER = BooleanProperty.create("corner");

	public SpatialRiftExitEdgeBlock() {
		super(BlockBehaviour.Properties.copy(ArcanusBlocks.SPATIAL_RIFT_WALL.get())
				.sound(SoundType.STONE)
				.lightLevel(value -> 9)
		);
		registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(CORNER, false));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return true;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING).add(CORNER);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SpatialRiftWallBlockEntity(pos, state);
	}
}
