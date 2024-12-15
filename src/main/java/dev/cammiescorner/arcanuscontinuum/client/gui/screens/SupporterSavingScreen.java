package dev.cammiescorner.arcanuscontinuum.client.gui.screens;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;

public class SupporterSavingScreen extends Screen {

	private final CompletableFuture<Void> saveFuture;
	private final Runnable onComplete;

	public SupporterSavingScreen(CompletableFuture<Void> saveFuture, Runnable onComplete) {
		super(Text.empty());
		this.saveFuture = saveFuture;
		this.onComplete = onComplete;
	}

	@Override
	protected void init() {
		super.init();

		saveFuture.exceptionally(throwable -> {
			Arcanus.LOGGER.error("Unable to save supporter data", throwable);
			return null;
		}).thenRunAsync(onComplete, client);
	}

	@Override
	public void renderBackground(GuiGraphics graphics) {
		this.renderBackgroundTexture(graphics);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		this.renderBackground(graphics);

		int centerX = width / 2;
		int centerY = height / 2;

		graphics.drawCenteredShadowedText(textRenderer, Text.translatable("screen.arcanuscontinuum.supporter_settings.saving"), centerX, centerY, 0xFFFFFFFF);

		super.render(graphics, mouseX, mouseY, delta);
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
