package dev.cammiescorner.arcanuscontinuum.common.blocks;

import dev.cammiescorner.arcanuscontinuum.common.blocks.entities.MagicBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

public class UnbreakableMagicBlock extends Block implements EntityBlock {
	public UnbreakableMagicBlock() {
		super(QuiltBlockSettings.copyOf(Blocks.BEDROCK)
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
		return new MagicBlockEntity(pos, state);
	}
}
