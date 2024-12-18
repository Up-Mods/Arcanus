package dev.cammiescorner.arcanuscontinuum.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.gui.screens.SpellcraftScreen;
import dev.cammiescorner.arcanuscontinuum.client.gui.util.UndoRedoStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class UndoRedoButtonWidget extends AbstractButton {
	private final PressAction onPress;
	private final UndoRedoStack undoRedoStack;
	private final boolean isUndo;

	public UndoRedoButtonWidget(int x, int y, boolean isUndo, UndoRedoStack stack, PressAction onPress) {
		super(x, y, 24, 16, Component.empty());
		this.isUndo = isUndo;
		this.onPress = onPress;
		this.undoRedoStack = stack;
		this.setTooltip(Tooltip.create(Arcanus.translate("screen", "tooltip", isUndo ? "undo" : "redo")));
	}

	@Override
	public void onPress() {
		onPress.onPress(this);
	}

	@Override
	public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

		int v = isUndo ? 192 : 208;
		active = isUndo ? undoRedoStack.canUndo() : undoRedoStack.canRedo();

		if(!active)
			gui.blit(SpellcraftScreen.BOOK_TEXTURE, getX(), getY(), 48, v, width, height, 256, 256);
		else if(!isHoveredOrFocused())
			gui.blit(SpellcraftScreen.BOOK_TEXTURE, getX(), getY(), 0, v, width, height, 256, 256);
		else
			gui.blit(SpellcraftScreen.BOOK_TEXTURE, getX(), getY(), 24, v, width, height, 256, 256);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput builder) {
		defaultButtonNarrationText(builder);
	}

	public interface PressAction {
		void onPress(UndoRedoButtonWidget buttonWidget);
	}
}
