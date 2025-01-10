package dev.cammiescorner.arcanuscontinuum.mixin.common;

import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantValue")
@Mixin(Level.class)
public abstract class WorldMixin {
	@Shadow
	public abstract BlockState getBlockState(BlockPos pos);

	@Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z", at = @At("HEAD"), cancellable = true)
	private void arcanuscontinuum$noReplacingWardedBlocks(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> info) {
		if(state.isAir() && ArcanusComponents.isBlockWarded((Level) (Object) this, pos))
			info.setReturnValue(false);
	}

	@Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
	private void arcanuscontinuum$noBreakingWardedBlocks(BlockPos pos, boolean drop, Entity breakingEntity, int maxUpdateDepth, CallbackInfoReturnable<Boolean> info) {
		BlockState state = getBlockState(pos);

		if(state.isAir() && ArcanusComponents.isBlockWarded((Level) (Object) this, pos))
			info.setReturnValue(false);
	}
}
