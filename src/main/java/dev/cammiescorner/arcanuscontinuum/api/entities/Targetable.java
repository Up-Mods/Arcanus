package dev.cammiescorner.arcanuscontinuum.api.entities;

import dev.cammiescorner.arcanuscontinuum.common.util.PlayerHelper;
import net.minecraft.world.entity.Entity;

public interface Targetable {
	default boolean arcanus$canBeTargeted() {
		if(this instanceof Entity self) {
			return !PlayerHelper.isFakePlayer(self);
		}

		return false;
	}
}
