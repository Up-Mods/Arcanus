package dev.cammiescorner.arcanuscontinuum.common.blocks;

import dev.cammiescorner.arcanuscontinuum.common.blocks.entities.ArcaneWorkbenchBlockEntity;
import dev.upcraft.sparkweave.api.registry.block.BlockItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArcaneWorkbenchBlock extends HorizontalDirectionalBlock implements EntityBlock, BlockItemProvider {
	private static final VoxelShape SHAPE = Shapes.or(
			Shapes.box(0, 0.1875, 0, 1, 0.8125, 1),
			Shapes.box(0, 0, 0, 0.125, 0.1875, 0.125),
			Shapes.box(0.875, 0, 0, 1, 0.1875, 0.125),
			Shapes.box(0, 0, 0.875, 0.125, 0.1875, 1),
			Shapes.box(0.875, 0, 0.875, 1, 0.1875, 1)
	);

	public ArcaneWorkbenchBlock() {
		super(Properties.of().mapColor(DyeColor.RED).strength(2F, 3F).lightLevel(value -> 12).noOcclusion().randomTicks());
		registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if(!world.isClientSide && world.getBlockEntity(pos) instanceof ArcaneWorkbenchBlockEntity arcaneWorkbench)
			player.openMenu(arcaneWorkbench);

		return InteractionResult.SUCCESS;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos blockPos, RandomSource random) {
		Direction direction = state.getValue(FACING);
		Vec3 d = Vec3.atLowerCornerOf(blockPos);
		List<Vec3> vec3ds = switch(direction) {
			case NORTH -> List.of(new Vec3(0.0625, 1.125, 0.8125), new Vec3(0.3125, 1.1875, 0.9375), new Vec3(0.6875, 1.1875, 0.9375), new Vec3(0.9375, 1.125, 0.8125));
			case SOUTH -> List.of(new Vec3(0.0625, 1.125, 0.1875), new Vec3(0.3125, 1.1875, 0.0625), new Vec3(0.6875, 1.1875, 0.0625), new Vec3(0.9375, 1.125, 0.1875));
			case WEST -> List.of(new Vec3(0.8125, 1.125, 0.0625), new Vec3(0.9375, 1.1875, 0.3125), new Vec3(0.9375, 1.1875, 0.6875), new Vec3(0.8125, 1.125, 0.9375));
			case EAST -> List.of(new Vec3(0.1875, 1.125, 0.0625), new Vec3(0.0625, 1.1875, 0.3125), new Vec3(0.0625, 1.1875, 0.6875), new Vec3(0.1875, 1.125, 0.9375));
			default -> List.of();
		};
		float f = random.nextFloat();

		for(Vec3 vec3d : vec3ds) {
			Vec3 pos = vec3d.add(d);

			if(f < 0.3F)
				world.addParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);

			world.addParticle(ParticleTypes.SMALL_FLAME, pos.x, pos.y, pos.z, 0, 0, 0);
		}

		if(f < 0.17F)
			world.playLocalSound(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		VoxelShape shape = switch(state.getValue(FACING)) {
			case NORTH -> Shapes.or(Shapes.box(0, 0.8125, 0.75, 0.125, 1, 0.875), Shapes.box(0.25, 0.8125, 0.875, 0.375, 1.0625, 1), Shapes.box(0.625, 0.8125, 0.875, 0.75, 1.0625, 1), Shapes.box(0.875, 0.8125, 0.75, 1, 1, 0.875));
			case SOUTH -> Shapes.or(Shapes.box(0, 0.8125, 0.125, 0.125, 1, 0.25), Shapes.box(0.25, 0.8125, 0, 0.375, 1.0625, 0.125), Shapes.box(0.625, 0.8125, 0, 0.75, 1.0625, 0.125), Shapes.box(0.875, 0.8125, 0.125, 1, 1, 0.25));
			case WEST -> Shapes.or(Shapes.box(0.75, 0.8125, 0, 0.875, 1, 0.125), Shapes.box(0.875, 0.8125, 0.25, 1, 1.0625, 0.375), Shapes.box(0.875, 0.8125, 0.625, 1, 1.0625, 0.75), Shapes.box(0.75, 0.8125, 0.875, 0.875, 1, 1));
			case EAST -> Shapes.or(Shapes.box(0.125, 0.8125, 0, 0.25, 1, 0.125), Shapes.box(0, 0.8125, 0.25, 0.125, 1.0625, 0.375), Shapes.box(0, 0.8125, 0.625, 0.125, 1.0625, 0.75), Shapes.box(0.125, 0.8125, 0.875, 0.25, 1, 1));
			default -> Shapes.empty();
		};

		return Shapes.or(SHAPE, shape);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ArcaneWorkbenchBlockEntity(pos, state);
	}
}
