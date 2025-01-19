package dev.cammiescorner.arcanuscontinuum.mixin.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player {
	@Shadow protected Vec3 deltaMovementOnPreviousTick;

	public AbstractClientPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) { super(level, pos, yRot, gameProfile); }

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void timeSlow(CallbackInfo info) {
		// TODO figure out how to smooth this
		if(level().getGameTime() % 2 == 0) {
			deltaMovementOnPreviousTick = getDeltaMovement();
			setOldPosAndRot();
			tickCount++;
			info.cancel();
		}
	}
}
