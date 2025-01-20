package dev.cammiescorner.arcanuscontinuum.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Timer.class)
public abstract class TimerMixin {
	@Shadow @Final @Mutable private float msPerTick;
	@Unique float ticksPerSecond;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void captureTickRate(float ticksPerSecond, long lastMs, CallbackInfo info) {
		this.ticksPerSecond = ticksPerSecond;
	}

	@Inject(method = "advanceTime", at = @At("HEAD"))
	private void slowDownTicks(long gameTime, CallbackInfoReturnable<Integer> info) {
		LocalPlayer player = Minecraft.getInstance().player;

		// TODO tie to being in a time dilation entity
		if(player != null && player.position().distanceTo(new Vec3(0, 146, 0)) < 16)
			msPerTick = 1000f / (ticksPerSecond / 2f);
		else
			msPerTick = 1000f / ticksPerSecond;
	}
}
