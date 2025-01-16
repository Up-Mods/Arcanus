package dev.cammiescorner.arcanus.fabric.common;

import dev.cammiescorner.arcanus.common.MainDuck;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.world.entity.Entity;

public class FabricMainDuck implements MainDuck {
	@Override
	public boolean isFakePlayer(Entity entity) {
		return entity instanceof FakePlayer;;
	}
}
