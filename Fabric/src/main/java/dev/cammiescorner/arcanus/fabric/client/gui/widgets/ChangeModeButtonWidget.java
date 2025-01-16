package dev.cammiescorner.arcanus.fabric.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.arcanus.fabric.common.util.WorkbenchMode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ChangeModeButtonWidget extends AbstractButton {
	private final PressAction onPress;
	private final ResourceLocation texture;

	public ChangeModeButtonWidget(int x, int y, WorkbenchMode mode, PressAction onPress) {
		super(x, y, 24, 16, Component.empty());
		this.onPress = onPress;
		this.texture = mode.getTexture();
		this.setTooltip(Tooltip.create(Component.translatable("screen.arcanus.tooltip.change_screens")));
	}

	@Override
	public void onPress() {
		onPress.onPress(this);
	}

	@Override
	public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

		if(!isHoveredOrFocused()) {
			gui.blit(texture, getX(), getY(), 24, 168, width, height, 256, 256);
		}
		else {
			gui.blit(texture, getX(), getY(), 48, 168, width, height, 256, 256);
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput builder) {
		defaultButtonNarrationText(builder);
	}

	public interface PressAction {
		void onPress(ChangeModeButtonWidget buttonWidget);
	}
}
