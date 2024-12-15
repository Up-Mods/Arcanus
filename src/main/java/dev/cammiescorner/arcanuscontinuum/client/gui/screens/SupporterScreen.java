package dev.cammiescorner.arcanuscontinuum.client.gui.screens;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.cammiescorner.arcanuscontinuum.common.util.StandardColors;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.HaloData;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.WizardData;
import dev.upcraft.datasync.api.util.Entitlements;
import dev.upcraft.datasync.api.util.GameProfileHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SupporterScreen extends Screen {
	private final ArcanusConfigScreen parent;

	private Entitlements userEntitlements;

	@Nullable
	private WizardData wizardData;

	@Nullable
	private HaloData haloData;
	private boolean haloEnabled;

	// primary group
	private TextFieldWidget magicColorField;

	// secondary group
	private TextFieldWidget haloColorField;

	public SupporterScreen(ArcanusConfigScreen parent) {
		super(Text.empty());
		this.parent = parent;

		UUID playerId = GameProfileHelper.getClientProfile().getId();
		this.userEntitlements = Entitlements.getOrEmpty(playerId);

		if (userEntitlements.keys().contains(WizardData.ID)) {
			this.wizardData = Arcanus.WIZARD_DATA.getOrDefault(playerId, WizardData.empty());
		}
		if (userEntitlements.keys().contains(HaloData.ID)) {
			this.haloData = Arcanus.HALO_DATA.getOrDefault(playerId, HaloData.empty());
			this.haloEnabled = this.haloData.shouldShow();
		}
	}

	private boolean hasWizardData() {
		return wizardData != null;
	}

	private boolean hasHaloData() {
		return haloData != null;
	}

	private Text getHaloEnabledText() {
		var text = haloEnabled ? Text.translatable("config.arcanuscontinuum.supporter_settings.halo_enabled") : Text.translatable("config.arcanuscontinuum.supporter_settings.halo_disabled");
		var checkMark = haloEnabled ? Text.translatable("screen.arcanuscontinuum.check.enabled").formatted(Formatting.GREEN) : Text.translatable("screen.arcanuscontinuum.check.disabled").formatted(Formatting.RED);
		return Text.empty().append(checkMark).append(" ").append(text);
	}

	@Override
	protected void init() {
		int centerX = width / 2;
		int centerY = height / 2;
		int yOffset = 0;

		if (hasHaloData()) {
			if (hasWizardData()) {
				yOffset = -35;
			}

			haloColorField = addDrawableChild(new TextFieldWidget(textRenderer, centerX + 11, centerY + yOffset - 10, 64, 20, Text.empty()));
			addDrawableChild(ButtonWidget.builder(getHaloEnabledText(), buttonWidget -> {
				haloEnabled = !haloEnabled;
				buttonWidget.setMessage(getHaloEnabledText());
			}).positionAndSize(centerX - 11, centerY + yOffset + 30 - 10, 88, 20).build());
			haloColorField.setHint(Text.translatable("config.arcanuscontinuum.supporter_settings.halo_color"));
			haloColorField.setText(String.format("#%06X", haloData.color().asInt(Color.Ordering.RGB)));
			haloColorField.setMaxLength(7);

			yOffset = 35;
		}

		if (hasWizardData()) {
			magicColorField = new TextFieldWidget(textRenderer, centerX + 11, centerY + yOffset - 10, 64, 20, Text.empty());
			addDrawableChild(magicColorField);
			magicColorField.setHint(Text.translatable("config.arcanuscontinuum.supporter_settings.magic_color"));
			magicColorField.setText(String.format("#%06X", wizardData.magicColor().asInt(Color.Ordering.RGB)));
			magicColorField.setMaxLength(7);
		}

		addDrawableChild(ButtonWidget.builder(Text.translatable("config.arcanuscontinuum.supporter_settings.save_and_exit"), buttonWidget -> {
			List<CompletableFuture<Void>> saveFutures = new ArrayList<>();
			if (hasHaloData()) {
				var newHaloData = getColorFromField(haloColorField).map(haloData::withColor).orElse(haloData).withEnabled(haloEnabled);
				if (!newHaloData.equals(haloData)) {
					saveFutures.add(Arcanus.HALO_DATA.setData(newHaloData));
				}
			}

			if (hasWizardData()) {
				var newWizardData = getColorFromField(magicColorField).map(WizardData::new).orElse(wizardData);
				if (!newWizardData.equals(wizardData)) {
					saveFutures.add(Arcanus.WIZARD_DATA.setData(newWizardData));
				}
			}

			client.setScreen(new SupporterSavingScreen(CompletableFuture.allOf(saveFutures.toArray(CompletableFuture[]::new)), () -> client.setScreen(parent)));
		}).positionAndSize(centerX - 55, height - 27, 110, 20).build());
	}

	@Override
	public void renderBackground(GuiGraphics graphics) {
		this.renderBackgroundTexture(graphics);
		graphics.setShaderColor(0.125f, 0.125f, 0.125f, 1f);
		graphics.drawTexture(Screen.OPTIONS_BACKGROUND_TEXTURE, 16, 32, 0, 0, width - 32, height - 65, 32, 32);
		graphics.setShaderColor(1f, 1f, 1f, 1f);

		// top shadow
		graphics.fillGradient(RenderLayer.getGuiOverlay(), 16, 32, width - 16, 36, 0xFF000000, 0x00000000, 0);

		// bottom shadow
		graphics.fillGradient(RenderLayer.getGuiOverlay(), 16, height - 37, width - 16, height - 33, 0x00000000, 0xFF000000, 0);
	}

	private Optional<Color> getColorFromField(TextFieldWidget field) {
		String fieldText = field.getText().trim().replace("#", "");
		if (fieldText.length() != 6) {
			return Optional.of(StandardColors.BLACK);
		}

		try {
			var rgb = Integer.parseInt(fieldText, 16);
			return Optional.of(Color.fromInt(rgb, Color.Ordering.RGB));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private void renderColorDisplay(GuiGraphics graphics, int x, int y, Optional<Color> color) {
		graphics.fill(x - 9, y - 9, x + 9, y + 9, 0xFFF0F0F0);
		color.ifPresent(colorValue -> graphics.fill(x - 8, y - 8, x + 8, y + 8, colorValue.asIntARGB()));
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		renderBackground(graphics);

		int centerX = width / 2;
		int centerY = height / 2;

		int yOffset = 0;
		if (hasHaloData()) {
			if (hasWizardData()) {
				yOffset = -35;
			}
			renderColorDisplay(graphics, centerX - 2, centerY + yOffset, getColorFromField(haloColorField));

			graphics.drawText(textRenderer, "Halo Color", centerX - 74, centerY + yOffset - textRenderer.fontHeight / 2, 0xffffff, false);

			yOffset = 35;
		}

		if (hasWizardData()) {
			renderColorDisplay(graphics, centerX - 2, centerY + yOffset, getColorFromField(magicColorField));
			graphics.drawText(textRenderer, "Magic Color", centerX - 74, centerY + yOffset - textRenderer.fontHeight / 2, 0xffffff, false);
		}


		super.render(graphics, mouseX, mouseY, delta);
	}

	@Override
	public void closeScreen() {
		client.setScreen(parent);
	}
}
