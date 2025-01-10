package dev.cammiescorner.arcanuscontinuum.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.items.StaffItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingScreen.class)
public abstract class CraftingScreenMixin extends AbstractContainerScreen<CraftingMenu> {
	public CraftingScreenMixin(CraftingMenu handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
	}

	@Inject(method = "render", at = @At(value = "INVOKE",
										target = "Lnet/minecraft/client/gui/screens/inventory/CraftingScreen;renderTooltip(Lnet/minecraft/client/gui/GuiGraphics;II)V"
	))
	private void arcanuscontinuum$render(GuiGraphics gui, int mouseX, int mouseY, float delta, CallbackInfo info) {
		PoseStack matrices = gui.pose();
		if(getMenu().getSlot(5).getItem().getItem() instanceof StaffItem) {
			float scale = 0.4F;
			matrices.pushPose();
			matrices.scale(scale, scale, 1F);

			if(getMenu().getSlot(2).getItem().isEmpty())
				gui.drawCenteredString(font, Arcanus.getSpellPatternAsText(0), (int) ((leftPos + 56) / scale), (int) ((topPos + 23) / scale), 0xffffff);
			if(getMenu().getSlot(3).getItem().isEmpty())
				gui.drawCenteredString(font, Arcanus.getSpellPatternAsText(1), (int) ((leftPos + 74) / scale), (int) ((topPos + 23) / scale), 0xffffff);
			if(getMenu().getSlot(6).getItem().isEmpty())
				gui.drawCenteredString(font, Arcanus.getSpellPatternAsText(2), (int) ((leftPos + 74) / scale), (int) ((topPos + 41) / scale), 0xffffff);
			if(getMenu().getSlot(9).getItem().isEmpty())
				gui.drawCenteredString(font, Arcanus.getSpellPatternAsText(3), (int) ((leftPos + 74) / scale), (int) ((topPos + 59) / scale), 0xffffff);
			if(getMenu().getSlot(8).getItem().isEmpty())
				gui.drawCenteredString(font, Arcanus.getSpellPatternAsText(4), (int) ((leftPos + 56) / scale), (int) ((topPos + 59) / scale), 0xffffff);
			if(getMenu().getSlot(7).getItem().isEmpty())
				gui.drawCenteredString(font, Arcanus.getSpellPatternAsText(5), (int) ((leftPos + 38) / scale), (int) ((topPos + 59) / scale), 0xffffff);
			if(getMenu().getSlot(4).getItem().isEmpty())
				gui.drawCenteredString(font, Arcanus.getSpellPatternAsText(6), (int) ((leftPos + 38) / scale), (int) ((topPos + 41) / scale), 0xffffff);
			if(getMenu().getSlot(1).getItem().isEmpty())
				gui.drawCenteredString(font, Arcanus.getSpellPatternAsText(7), (int) ((leftPos + 38) / scale), (int) ((topPos + 23) / scale), 0xffffff);

			matrices.popPose();
		}
	}
}
