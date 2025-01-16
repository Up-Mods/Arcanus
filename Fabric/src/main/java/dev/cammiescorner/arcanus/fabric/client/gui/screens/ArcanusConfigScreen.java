package dev.cammiescorner.arcanus.fabric.client.gui.screens;

import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ArcanusConfigScreen extends ConfigScreen {
	public ArcanusConfigScreen(@Nullable ConfigScreen configScreen, ResourcefulConfig config) {
		super(configScreen, config);
	}

	@Override
	protected void createFooter() {
		super.createFooter();

		addRenderableWidget(Button.builder(Component.translatable("config.arcanus.supporter_settings").withStyle(ChatFormatting.AQUA), buttonWidget -> SupporterScreen.open(this)).bounds(width / 2 - 55, height - 27, 110, 20).build());
	}
}
