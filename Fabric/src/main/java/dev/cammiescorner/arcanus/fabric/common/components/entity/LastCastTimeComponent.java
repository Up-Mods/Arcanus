package dev.cammiescorner.arcanus.fabric.common.components.entity;

import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class LastCastTimeComponent implements AutoSyncedComponent {
	private final LivingEntity entity;
	private long lastCastTime = 0;

	public LastCastTimeComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
		lastCastTime = tag.getLong("LastTimeCast");
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
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
