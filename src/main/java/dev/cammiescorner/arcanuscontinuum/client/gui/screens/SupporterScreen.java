package dev.cammiescorner.arcanuscontinuum.client.gui.screens;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
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
import net.minecraft.client.render.RenderLayer;
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
		int yOffset = 15;

		if(client != null && Entitlements.getOrEmpty(playerId).keys().contains(HaloData.ID)) {
			haloColorField = new TextFieldWidget(textRenderer, xMiddle + 11, yMiddle - 35, 64, 20, Text.empty());
			haloToggle = new ToggleButtonWidget(xMiddle + 80, yMiddle - 35, 20, 20, true);
			haloToggle.setTextureUV(1, 208, 13, 18, RecipeBookWidget.TEXTURE);
			yOffset = 5;

			addDrawableChild(haloColorField);
			addDrawableChild(haloToggle);
			haloColorField.setHint(Text.translatable("config.arcanuscontinuum.supporter_settings.halo_color"));
			haloColorField.setText(String.format("#%06X", haloColor.asInt(Color.Ordering.RGB)));
			haloColorField.setMaxLength(7);
		}

		magicColorField = new TextFieldWidget(textRenderer, xMiddle + 11, yMiddle - yOffset, 64, 20, Text.empty());
		addDrawableChild(magicColorField);
		magicColorField.setHint(Text.translatable("config.arcanuscontinuum.supporter_settings.magic_color"));
		magicColorField.setText(String.format("#%06X", magicColor.asInt(Color.Ordering.RGB)));
		magicColorField.setMaxLength(7);

		addDrawableChild(ButtonWidget.builder(Text.translatable("config.arcanuscontinuum.supporter_settings.done"), buttonWidget -> {
			if(haloColorField != null && haloToggle != null) {
				int newHaloColor;

				try {
					String fieldText = haloColorField.getText().replace("#", "");
					newHaloColor = Integer.parseInt(fieldText, 16);
				}
				catch(NumberFormatException e) {
					Arcanus.LOGGER.warn("Halo Color value {} is invalid! Must be a hex code!", haloColorField.getText());
					return;
				}

				Arcanus.HALO_DATA.setData(new HaloData(Color.fromInt(newHaloColor, Color.Ordering.RGB), haloToggle.isToggled()));
			}

			int newMagicColor;

			try {
				String fieldText = magicColorField.getText().replace("#", "");
				newMagicColor = Integer.parseInt(fieldText, 16);
			}
			catch(NumberFormatException e) {
				Arcanus.LOGGER.warn("Magic Color value {} is invalid! Must be a hex code!", magicColorField.getText());
				return;
			}

			Arcanus.WIZARD_DATA.setData(new WizardData(Color.fromInt(newMagicColor, Color.Ordering.RGB)));

			if(client != null)
				client.setScreen(parent);
			else
				closeScreen();
		}).positionAndSize(xMiddle - 50, height - 27, 100, 20).build());
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		renderBackground(graphics);
		graphics.setShaderColor(0.125f, 0.125f, 0.125f, 1f);
		graphics.drawTexture(Screen.OPTIONS_BACKGROUND_TEXTURE, 16, 32, 0, 0, width - 32, height - 65, 32, 32);
		graphics.setShaderColor(1f, 1f, 1f, 1f);
		graphics.fillGradient(RenderLayer.getGuiOverlay(), 16, 32, width - 16, 36, -16777216, 0, 0); // top shadow
		graphics.fillGradient(RenderLayer.getGuiOverlay(), 16, height - 37, width - 16, height - 33, 0, -16777216, 0); // bottom shadow

		int xMiddle = (int) (width * 0.5);
		int yMiddle = (int) (height * 0.5);

		graphics.drawText(textRenderer, "Halo Color:", xMiddle - 50, yMiddle - 29, 0xffffff, false);
		graphics.drawText(textRenderer, "Magic Color:", xMiddle - 55, yMiddle + 1, 0xffffff, false);
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
