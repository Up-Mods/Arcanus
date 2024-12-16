package dev.cammiescorner.arcanuscontinuum.common.util;

import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.Entity;

public class PlayerHelper {

	public static boolean isFakePlayer(Entity entity) {
		return entity instanceof FakePlayer;
	}
}
