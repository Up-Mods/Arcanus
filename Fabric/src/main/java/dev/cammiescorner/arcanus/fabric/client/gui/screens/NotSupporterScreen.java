package dev.cammiescorner.arcanus.fabric.client.gui.screens;

import com.mojang.authlib.GameProfile;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.util.Objects;

public class NotSupporterScreen extends Screen {

	private static final ResourceLocation DEMO_BACKGROUND_LOCATION = new ResourceLocation("textures/gui/demo_background.png");
	private static final String CAMMIE_KOFI_URL = "https://ko-fi.com/camellias";
	private static final String UP_KOFI_URL = "https://upcraft.dev/links/ko-fi";

	private final Screen parent;
	private final GameProfile currentPlayerProfile;
	private MultiLineLabel welcomeMessage = MultiLineLabel.EMPTY;
	private MultiLineLabel perksMessage = MultiLineLabel.EMPTY;

	public NotSupporterScreen(Screen parent, GameProfile currentPlayerProfile) {
		super(Component.translatable("screen.arcanus.support_us.title", Component.translatable("screen.arcanus.support_us.title2").withStyle(ChatFormatting.UNDERLINE)));
		this.parent = parent;
		this.currentPlayerProfile = currentPlayerProfile;
	}

	@Override
	protected void init() {

		var centerX = this.width / 2;
		var centerY = this.height / 2;

		// TODO maybe link to mod list page once it exists?
		welcomeMessage = MultiLineLabel.create(this.font, Component.translatable("screen.arcanus.support_us.welcome_message", currentPlayerProfile.getName()), 218);
		perksMessage = MultiLineLabel.create(this.font, Component.translatable("screen.arcanus.support_us.perks_message"), 218);

		this.addRenderableWidget(Button.builder(Component.translatable("screen.arcanus.support_us.button_support_cammie"), (button) -> {
			button.active = false;
			openLink(CAMMIE_KOFI_URL);
		}).bounds(centerX - 118, centerY + 56, 114, 20).build());
		this.addRenderableWidget(Button.builder(Component.translatable("screen.arcanus.support_us.button_support_up"), (button) -> {
			button.active = false;
			openLink(UP_KOFI_URL);
		}).bounds(centerX + 2, centerY + 56, 114, 20).build());

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, button -> this.minecraft.setScreen(parent)).bounds(centerX - 55, height - 27, 110, 20).build());
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(guiGraphics);

		int x = (this.width - 248) / 2 + 10;
		int y = (this.height - 166) / 2 + 8;
		guiGraphics.drawString(this.font, this.title, x, y, 0x1F1F1F, false);
		y = welcomeMessage.renderLeftAlignedNoShadow(guiGraphics, x, y + 14, 12, 0x4F4F4F);
		Objects.requireNonNull(this.font);
		perksMessage.renderLeftAlignedNoShadow(guiGraphics, x, y + 20, 9, 0x1F1F1F);

		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public void renderBackground(GuiGraphics guiGraphics) {
		renderDirtBackground(guiGraphics);

		int centerX = (this.width - 248) / 2;
		int centerY = (this.height - 166) / 2;
		guiGraphics.blit(DEMO_BACKGROUND_LOCATION, centerX, centerY, 0, 0, 248, 166);
	}

	private static void openLink(String target) {
		var container = FabricLoader.getInstance().getModContainer(FabricMain.MOD_ID).orElseThrow();
		var meta = container.getMetadata();
		try {
			var uri = new URIBuilder(target).addParameter("source", "mod:%s/%s".formatted(meta.getId(), meta.getVersion())).build();
			Util.getPlatform().openUri(uri);
		}
		catch(URISyntaxException e) {
			FabricMain.LOGGER.error("Failed to create URI", e);
			Util.getPlatform().openUri(target);
		}
	}

	@Override
	public void onClose() {
		minecraft.setScreen(parent);
	}
}
