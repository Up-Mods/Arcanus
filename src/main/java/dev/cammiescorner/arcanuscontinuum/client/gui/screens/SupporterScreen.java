package dev.cammiescorner.arcanuscontinuum.client.gui.screens;

import com.mojang.authlib.GameProfile;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.cammiescorner.arcanuscontinuum.common.util.StandardColors;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.HaloData;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.WizardData;
import dev.upcraft.datasync.api.util.Entitlements;
import dev.upcraft.datasync.api.util.GameProfileHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SupporterScreen extends Screen {
	private final Screen parent;

	private Entitlements userEntitlements;

	@Nullable
	private WizardData wizardData;

	@Nullable
	private HaloData haloData;
	private boolean haloEnabled;

	// primary group
	private EditBox magicColorField;

	// secondary group
	private EditBox haloColorField;

	public SupporterScreen(Screen parent, GameProfile clientProfile, Entitlements userEntitlements) {
		super(Component.empty());
		this.parent = parent;

		this.userEntitlements = userEntitlements;

		if (userEntitlements.keys().contains(WizardData.ID)) {
			this.wizardData = Arcanus.WIZARD_DATA.getOrDefault(clientProfile.getId(), WizardData.empty());
		}
		if (userEntitlements.keys().contains(HaloData.ID)) {
			this.haloData = Arcanus.HALO_DATA.getOrDefault(clientProfile.getId(), HaloData.empty());
			this.haloEnabled = this.haloData.shouldShow();
		}
	}

	public static void open(@Nullable Screen parent) {
		var clientProfile = GameProfileHelper.getClientProfile();
		var entitlements = Entitlements.getOrEmpty(clientProfile.getId());
		var isSupporter = entitlements.keys().contains(WizardData.ID) || entitlements.keys().contains(HaloData.ID);
		Minecraft.getInstance().setScreen(isSupporter ? new SupporterScreen(parent, clientProfile, entitlements) : new NotSupporterScreen(parent, clientProfile));
	}

	private boolean hasWizardData() {
		return wizardData != null;
	}

	private boolean hasHaloData() {
		return haloData != null;
	}

	private Component getHaloEnabledText() {
		var text = haloEnabled ? Component.translatable("config.arcanuscontinuum.supporter_settings.halo_enabled") : Component.translatable("config.arcanuscontinuum.supporter_settings.halo_disabled");
		var checkMark = haloEnabled ? Component.translatable("screen.arcanuscontinuum.check.enabled").withStyle(ChatFormatting.GREEN) : Component.translatable("screen.arcanuscontinuum.check.disabled").withStyle(ChatFormatting.RED);
		return Component.empty().append(checkMark).append(" ").append(text);
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

			haloColorField = addRenderableWidget(new EditBox(font, centerX + 11, centerY + yOffset - 10, 64, 20, Component.empty()));
			addRenderableWidget(Button.builder(getHaloEnabledText(), buttonWidget -> {
				haloEnabled = !haloEnabled;
				buttonWidget.setMessage(getHaloEnabledText());
			}).bounds(centerX - 11, centerY + yOffset + 30 - 10, 88, 20).build());
			haloColorField.setHint(Component.translatable("config.arcanuscontinuum.supporter_settings.halo_color"));
			haloColorField.setValue(String.format("#%06X", haloData.color().asInt(Color.Ordering.RGB)));
			haloColorField.setMaxLength(7);

			yOffset = 35;
		}

		if (hasWizardData()) {
			magicColorField = new EditBox(font, centerX + 11, centerY + yOffset - 10, 64, 20, Component.empty());
			addRenderableWidget(magicColorField);
			magicColorField.setHint(Component.translatable("config.arcanuscontinuum.supporter_settings.magic_color"));
			magicColorField.setValue(String.format("#%06X", wizardData.magicColor().asInt(Color.Ordering.RGB)));
			magicColorField.setMaxLength(7);
		}

		addRenderableWidget(Button.builder(Component.translatable("config.arcanuscontinuum.supporter_settings.save_and_exit"), buttonWidget -> {
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

			minecraft.setScreen(new SupporterSavingScreen(CompletableFuture.allOf(saveFutures.toArray(CompletableFuture[]::new)), () -> minecraft.setScreen(parent)));
		}).bounds(centerX - 55, height - 27, 110, 20).build());
	}

	@Override
	public void renderBackground(GuiGraphics graphics) {
		this.renderDirtBackground(graphics);
		graphics.setColor(0.125f, 0.125f, 0.125f, 1f);
		graphics.blit(Screen.BACKGROUND_LOCATION, 16, 32, 0, 0, width - 32, height - 65, 32, 32);
		graphics.setColor(1f, 1f, 1f, 1f);

		// top shadow
		graphics.fillGradient(RenderType.guiOverlay(), 16, 32, width - 16, 36, 0xFF000000, 0x00000000, 0);

		// bottom shadow
		graphics.fillGradient(RenderType.guiOverlay(), 16, height - 37, width - 16, height - 33, 0x00000000, 0xFF000000, 0);
	}

	private Optional<Color> getColorFromField(EditBox field) {
		String fieldText = field.getValue().trim().replace("#", "");
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

			graphics.drawString(font, "Halo Color", centerX - 74, centerY + yOffset - font.lineHeight / 2, 0xffffff, false);

			yOffset = 35;
		}

		if (hasWizardData()) {
			renderColorDisplay(graphics, centerX - 2, centerY + yOffset, getColorFromField(magicColorField));
			graphics.drawString(font, "Magic Color", centerX - 74, centerY + yOffset - font.lineHeight / 2, 0xffffff, false);
		}


		super.render(graphics, mouseX, mouseY, delta);
	}

	@Override
	public void onClose() {
		minecraft.setScreen(parent);
	}
}
