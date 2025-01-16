package dev.cammiescorner.arcanus.fabric.mixin.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusMobEffects;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MouseHandler.class)
public class MouseMixin {
	@Shadow @Final private Minecraft minecraft;

	@ModifyArg(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"), index = 0)
	public double arcanus$invertMouseX(double x) {
		if(minecraft.player != null && minecraft.player.hasEffect(ArcanusMobEffects.DISCOMBOBULATE.get()))
			return -x;

		return x;
	}

	@ModifyArg(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"), index = 1)
	public double arcanus$invertMouseY(double y) {
		if(minecraft.player != null && minecraft.player.hasEffect(ArcanusMobEffects.DISCOMBOBULATE.get()))
			return -y;

		return y;
	}

	@ModifyArg(method = "onPress", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/InputConstants$Type;getOrCreate(I)Lcom/mojang/blaze3d/platform/InputConstants$Key;"), index = 0)
	public int arcanus$invertMouseButtons(int i) {
		if(minecraft.player != null && minecraft.player.hasEffect(ArcanusMobEffects.DISCOMBOBULATE.get())) {
			return switch(i) {
				case 0 -> {
					KeyMapping.set(InputConstants.Type.MOUSE.getOrCreate(0), false);
					KeyMapping.set(InputConstants.Type.MOUSE.getOrCreate(1), true);
					yield 1;
				}
				case 1 -> {
					KeyMapping.set(InputConstants.Type.MOUSE.getOrCreate(1), false);
					KeyMapping.set(InputConstants.Type.MOUSE.getOrCreate(0), true);
					yield 0;
				}
				default -> i;
			};
		}

		return i;
	}
}
