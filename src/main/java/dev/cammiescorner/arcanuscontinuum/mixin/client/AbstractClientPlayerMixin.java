package dev.cammiescorner.arcanuscontinuum.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player {
	public AbstractClientPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) { super(level, pos, yRot, gameProfile); }

	@WrapWithCondition(method = "tick", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/world/entity/player/Player;tick()V"
	))
	private boolean timeSlow(Player instance) {
		// TODO figure out how to smooth this & tie it to being in a time dilation field
		if(level().getGameTime() % 2 == 0) {
			setOldPosAndRot();
			tickCount++;
			return false;
		}

		return true;
	}
}
