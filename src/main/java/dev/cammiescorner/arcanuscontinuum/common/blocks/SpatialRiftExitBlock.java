package dev.cammiescorner.arcanuscontinuum.common.blocks;

import dev.cammiescorner.arcanuscontinuum.common.blocks.entities.SpatialRiftExitBlockEntity;
import dev.cammiescorner.arcanuscontinuum.common.components.level.PocketDimensionComponent;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SpatialRiftExitBlock extends Block implements EntityBlock {
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public SpatialRiftExitBlock() {
		super(BlockBehaviour.Properties.copy(ArcanusBlocks.SPATIAL_RIFT_WALL.get()).sound(SoundType.STONE).lightLevel(value -> 7));
		registerDefaultState(getStateDefinition().any().setValue(ACTIVE, false));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if(!world.isClientSide() && !PocketDimensionComponent.get(world).teleportOutOfPocketDimension(player))
			return InteractionResult.FAIL;

		return InteractionResult.sidedSuccess(world.isClientSide());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		if(state.getValue(ACTIVE))
			return new SpatialRiftExitBlockEntity(pos, state);

		return null;
	}
}
