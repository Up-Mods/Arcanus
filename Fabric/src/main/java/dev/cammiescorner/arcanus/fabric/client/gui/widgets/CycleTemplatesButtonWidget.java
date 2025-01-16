package dev.cammiescorner.arcanus.fabric.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.arcanus.fabric.common.util.WorkbenchMode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CycleTemplatesButtonWidget extends AbstractButton {
	private final PressAction onPress;
	private static final ResourceLocation TEXTURE = WorkbenchMode.CUSTOMIZE.getTexture();
	public final boolean isUp;

	public CycleTemplatesButtonWidget(int x, int y, boolean isUp, PressAction onPress) {
		super(x, y, 16, 16, Component.empty());
		this.isUp = isUp;
		this.onPress = onPress;
		this.setTooltip(Tooltip.create(isUp ? Component.translatable("screen.arcanus.tooltip.cycle_up") : Component.translatable("screen.arcanus.tooltip.cycle_down")));
	}

	@Override
	public void onPress() {
		onPress.onPress(this);
	}

	@Override
	public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

		int v = isUp ? 184 : 200;

		if(!isHoveredOrFocused()) {
			gui.blit(TEXTURE, getX(), getY(), 24, v, width, height, 256, 256);
		}
		else {
			gui.blit(TEXTURE, getX(), getY(), 40, v, width, height, 256, 256);
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput builder) {
		defaultButtonNarrationText(builder);
	}

	public interface PressAction {
		void onPress(CycleTemplatesButtonWidget buttonWidget);
	}
}
