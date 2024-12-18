package dev.cammiescorner.arcanuscontinuum.common.components.entity;

import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class LastCastTimeComponent implements AutoSyncedComponent {
	private final LivingEntity entity;
	private long lastCastTime = 0;

	public LastCastTimeComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		lastCastTime = tag.getLong("LastTimeCast");
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putLong("LastTimeCast", lastCastTime);
	}

	public long getLastCastTime() {
		return lastCastTime;
	}

	public void setLastCastTime(long lastCastTime) {
		this.lastCastTime = lastCastTime;
		ArcanusComponents.LAST_CAST_TIME_COMPONENT.sync(entity);
	}
}
