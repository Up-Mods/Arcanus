package dev.cammiescorner.arcanuscontinuum.client.gui.screens;

import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.WizardData;
import dev.upcraft.datasync.api.util.GameProfileHelper;
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

		if(WizardData.isSupporter(GameProfileHelper.getClientProfile().getId())) {
			addRenderableWidget(Button.builder(Component.translatable("config.arcanuscontinuum.supporter_settings"), buttonWidget -> {
				if(minecraft != null)
					minecraft.setScreen(new SupporterScreen(this));
			}).bounds(width / 2 - 55, height - 27, 110, 20).build());
		}
	}
}
