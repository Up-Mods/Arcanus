package dev.cammiescorner.arcanuscontinuum.common.compat;

import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.client.gui.screens.ArcanusConfigScreen;

public class ModMenuCompat implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			ResourcefulConfig config = Arcanus.configurator.getConfig(ArcanusConfig.class);

			if(config == null)
				return null;

			return new ArcanusConfigScreen(null, config);
		};
	}
}
