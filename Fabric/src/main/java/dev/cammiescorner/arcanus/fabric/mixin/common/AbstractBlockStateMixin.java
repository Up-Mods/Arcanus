package dev.cammiescorner.arcanus.fabric.mixin.common;

import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class AbstractBlockStateMixin {
	@Inject(method = "getDestroyProgress", at = @At("HEAD"), cancellable = true)
	private void arcanus$wardedBlockBreakStatus(Player player, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Float> info) {
		if(ArcanusComponents.isBlockWarded((Level) world, pos))
			info.setReturnValue(0f);
	}
}
