package dev.cammiescorner.arcanus.fabric.mixin.common;

import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlock.class)
public class FallingBlockMixin {
	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void arcanus$wardedBlocksCantFall(BlockState state, ServerLevel world, BlockPos pos, RandomSource random, CallbackInfo info) {
		if(ArcanusComponents.isBlockWarded(world, pos))
			info.cancel();
	}
}
