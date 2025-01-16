package dev.cammiescorner.arcanus.common;

import dev.upcraft.sparkweave.api.platform.Services;
import net.minecraft.world.entity.Entity;

public class MainHelper {
	private static final MainDuck mainDuck = Services.getService(MainDuck.class);

	public static boolean isFakePlayer(Entity entity) {
		return mainDuck.isFakePlayer(entity);
	}
}
