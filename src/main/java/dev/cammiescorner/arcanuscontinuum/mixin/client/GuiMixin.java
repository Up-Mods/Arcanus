package dev.cammiescorner.arcanuscontinuum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public class GuiMixin {
	@Shadow private int screenWidth;
	@Shadow private int screenHeight;

	@WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 0))
	private void arcanuscontinuum$moveCrosshair(GuiGraphics instance, ResourceLocation id, int x, int y, int u, int v, int w, int h, Operation<Void> original) {
		PoseStack matrices = instance.pose();
		matrices.pushPose();
		matrices.translate((screenWidth - 15) / 2F, (screenHeight - 15) / 2F, 0);
		original.call(instance, id, 0, 0, u, v, w, h);
		matrices.popPose();
	}
}
