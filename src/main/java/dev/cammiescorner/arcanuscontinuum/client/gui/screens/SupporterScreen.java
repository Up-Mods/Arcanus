package dev.cammiescorner.arcanuscontinuum.client.gui.screens;

import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.HaloData;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.WizardData;
import dev.upcraft.datasync.api.util.Entitlements;
import dev.upcraft.datasync.api.util.GameProfileHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.recipe.book.RecipeBookWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.text.Text;

import java.util.UUID;

public class SupporterScreen extends Screen {
	private final ArcanusConfigScreen parent;
	private TextFieldWidget magicColorField;
	private TextFieldWidget haloColorField;
	private ToggleButtonWidget haloToggle;

	public SupporterScreen(ArcanusConfigScreen parent) {
		super(Text.empty());
		this.parent = parent;
	}

	@Override
	protected void init() {
		UUID playerId = GameProfileHelper.getClientProfile().getId();
		Color magicColor = WizardData.getOrEmpty(playerId).magicColor();
		Color haloColor = HaloData.getOrEmpty(playerId).color();
		int xMiddle = (int) (width * 0.5);
		int yMiddle = (int) (height * 0.5);
		int yOffset = 0;

		if(client != null && Entitlements.getOrEmpty(playerId).keys().contains(HaloData.ID)) {
			haloColorField = new TextFieldWidget(textRenderer, xMiddle - 75, yMiddle - 10, 150, 20, Text.empty());
			haloToggle = new ToggleButtonWidget(xMiddle + 80, yMiddle - 10, 20, 20, true);
			haloToggle.setTextureUV(1, 208, 13, 18, RecipeBookWidget.TEXTURE);
			yOffset = 10;

			addDrawableChild(haloColorField);
			addDrawableChild(haloToggle);
			haloColorField.setHint(Text.translatable("config.arcanuscontinuum.supporter_settings.halo_color"));
			haloColorField.setText(String.format("#%06X", haloColor.asInt(Color.Ordering.RGBA)));
		}

		magicColorField = new TextFieldWidget(textRenderer, (int) (width * 0.5) - 75, (int) (height * 0.5) + yOffset, 150, 20, Text.empty());
		addDrawableChild(magicColorField);
		magicColorField.setHint(Text.translatable("config.arcanuscontinuum.supporter_settings.magic_color"));
		magicColorField.setText(String.format("#%06X", magicColor.asInt(Color.Ordering.RGBA)));

		addDrawableChild(ButtonWidget.builder(Text.translatable("config.arcanuscontinuum.supporter_settings.done"), buttonWidget -> {
			if(client != null)
				client.setScreen(parent);
			else
				closeScreen();
		}).positionAndSize(xMiddle - 50, height - 27, 100, 20).build());
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, delta);
	}

	@Override
	public void closeScreen() {
		if(client != null)
			client.setScreen(parent);
		else
			super.closeScreen();
	}
}
