package dev.cammiescorner.arcanus.fabric.common.util;

import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.world.entity.Entity;

public class PlayerHelper {

	public static boolean isFakePlayer(Entity entity) {
		return entity instanceof FakePlayer;
	}
}
