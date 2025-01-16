package dev.cammiescorner.arcanus.api.entities;

import dev.cammiescorner.arcanus.common.MainHelper;
import net.minecraft.world.entity.Entity;

public interface Targetable {
	default boolean arcanus$canBeTargeted() {
		if(this instanceof Entity self)
			return !MainHelper.isFakePlayer(self);

		return false;
	}
}
