package dev.cammiescorner.arcanus.fabric.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.arcanus.fabric.client.gui.widgets.ChangeModeButtonWidget;
import dev.cammiescorner.arcanus.fabric.client.gui.widgets.CycleTemplatesButtonWidget;
import dev.cammiescorner.arcanus.fabric.common.items.StaffItem;
import dev.cammiescorner.arcanus.fabric.common.screens.ArcaneWorkbenchScreenHandler;
import dev.cammiescorner.arcanus.fabric.common.util.WorkbenchMode;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ArcaneWorkbenchScreen extends AbstractContainerScreen<ArcaneWorkbenchScreenHandler> {
	public ArcaneWorkbenchScreen(ArcaneWorkbenchScreenHandler handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init() {
		super.init();
		leftPos = (width - 176) / 2;
		topPos = (height - 166) / 2;

		if(menu.getMode() == WorkbenchMode.CUSTOMIZE) {
			addRenderableWidget(new CycleTemplatesButtonWidget(leftPos + 10, topPos + 13, true, this::cycleTemplate));
			addRenderableWidget(new CycleTemplatesButtonWidget(leftPos + 10, topPos + 57, false, this::cycleTemplate));
		}

		addRenderableWidget(new ChangeModeButtonWidget(leftPos + 148, topPos + 60, menu.getMode(), this::changeMode));
	}

	@Override
	public void rebuildWidgets() {
		super.rebuildWidgets();
	}

	private void cycleTemplate(CycleTemplatesButtonWidget widget) {
		if(minecraft != null && minecraft.gameMode != null)
			minecraft.gameMode.handleInventoryButtonClick(menu.containerId, widget.isUp ? 1 : 2);
	}

	private void changeMode(ChangeModeButtonWidget widget) {
		if(minecraft != null && minecraft.gameMode != null)
			minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0);
	}

	@Override
	protected void renderBg(GuiGraphics gui, float delta, int mouseX, int mouseY) {
		this.renderBackground(gui);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		gui.blit(getTexture(), leftPos, topPos, 0, 0, 176, 166, 256, 256);

		if(getMenu().getMode() == WorkbenchMode.CUSTOMIZE) {
			if(getMenu().getSlot(2).getItem().isEmpty())
				gui.blit(getTexture(), leftPos + 95, topPos + 24, 176, 0, 16, 16);
			if(getMenu().getSlot(3).getItem().isEmpty())
				gui.blit(getTexture(), leftPos + 95, topPos + 46, 176, 0, 16, 16);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
		PoseStack matrices = gui.pose();

		if(getMenu().getMode() == WorkbenchMode.SPELLBINDING && getMenu().getSlot(5).getItem().getItem() instanceof StaffItem) {
			float scale = 0.4F;
			matrices.pushPose();
			matrices.scale(scale, scale, 1F);

			if(getMenu().getSlot(2).getItem().isEmpty())
				gui.drawCenteredString(font, FabricMain.getSpellPatternAsText(0), (int) (52 / scale), (int) (17 / scale), 0xffffff);
			if(getMenu().getSlot(3).getItem().isEmpty())
				gui.drawCenteredString(font, FabricMain.getSpellPatternAsText(1), (int) (72 / scale), (int) (17 / scale), 0xffffff);
			if(getMenu().getSlot(6).getItem().isEmpty())
				gui.drawCenteredString(font, FabricMain.getSpellPatternAsText(2), (int) (72 / scale), (int) (37 / scale), 0xffffff);
			if(getMenu().getSlot(9).getItem().isEmpty())
				gui.drawCenteredString(font, FabricMain.getSpellPatternAsText(3), (int) (72 / scale), (int) (57 / scale), 0xffffff);
			if(getMenu().getSlot(8).getItem().isEmpty())
				gui.drawCenteredString(font, FabricMain.getSpellPatternAsText(4), (int) (52 / scale), (int) (57 / scale), 0xffffff);
			if(getMenu().getSlot(7).getItem().isEmpty())
				gui.drawCenteredString(font, FabricMain.getSpellPatternAsText(5), (int) (32 / scale), (int) (57 / scale), 0xffffff);
			if(getMenu().getSlot(4).getItem().isEmpty())
				gui.drawCenteredString(font, FabricMain.getSpellPatternAsText(6), (int) (32 / scale), (int) (37 / scale), 0xffffff);
			if(getMenu().getSlot(1).getItem().isEmpty())
				gui.drawCenteredString(font, FabricMain.getSpellPatternAsText(7), (int) (32 / scale), (int) (17 / scale), 0xffffff);

			matrices.popPose();
		}
		else if(getMenu().getMode() == WorkbenchMode.CUSTOMIZE) {
			gui.renderItem(getMenu().getTemplate().getDefaultInstance(), 10, 35);
		}

		renderTooltip(gui, mouseX - leftPos, mouseY - topPos);
	}

	public ResourceLocation getTexture() {
		return getMenu().getMode().getTexture();
	}
}
