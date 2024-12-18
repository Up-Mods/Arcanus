package dev.cammiescorner.arcanuscontinuum.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonBaseBlock.class)
public class PistonBlockMixin {
	@ModifyExpressionValue(method = "isPushable", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroySpeed(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"
	))
	private static float arcanuscontinuum$dontMoveWardedBlocks(float original, BlockState state, Level world, BlockPos pos, Direction direction, boolean canBreak, Direction pistonFacing) {
		return ArcanusComponents.isBlockWarded(world, pos) ? -1f : original;
	}
}
