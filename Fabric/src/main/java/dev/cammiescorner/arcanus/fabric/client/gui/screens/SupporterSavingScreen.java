package dev.cammiescorner.arcanus.fabric.client.gui.screens;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

public class SupporterSavingScreen extends Screen {

	private final CompletableFuture<Void> saveFuture;
	private final Runnable onComplete;

	public SupporterSavingScreen(CompletableFuture<Void> saveFuture, Runnable onComplete) {
		super(Component.empty());
		this.saveFuture = saveFuture;
		this.onComplete = onComplete;
	}

	@Override
	protected void init() {
		super.init();

		saveFuture.exceptionally(throwable -> {
			FabricMain.LOGGER.error("Unable to save supporter data", throwable);
			return null;
		}).thenRunAsync(onComplete, minecraft);
	}

	@Override
	public void renderBackground(GuiGraphics graphics) {
		this.renderDirtBackground(graphics);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		this.renderBackground(graphics);

		int centerX = width / 2;
		int centerY = height / 2;

		graphics.drawCenteredString(font, Component.translatable("screen.arcanus.supporter_settings.saving"), centerX, centerY, 0xFFFFFFFF);

		super.render(graphics, mouseX, mouseY, delta);
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
