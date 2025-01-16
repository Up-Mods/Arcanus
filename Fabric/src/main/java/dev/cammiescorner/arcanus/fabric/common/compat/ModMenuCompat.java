package dev.cammiescorner.arcanus.fabric.common.compat;

import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.cammiescorner.arcanus.ArcanusConfig;
import dev.cammiescorner.arcanus.fabric.client.gui.screens.ArcanusConfigScreen;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;

public class ModMenuCompat implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			ResourcefulConfig config = FabricMain.configurator.getConfig(ArcanusConfig.class);

			if(config == null)
				return null;

			return new ArcanusConfigScreen(null, config);
		};
	}
}
