package dev.cammiescorner.arcanuscontinuum.common.components.entity;

import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class SlowTimeComponent  implements AutoSyncedComponent {
	private final Entity entity;
	private boolean slowTime;
	private boolean blockUpdates;
	private int blockUpdatesInterval = 20;

	public SlowTimeComponent(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		slowTime = tag.getBoolean("SlowTime");
		blockUpdates = tag.getBoolean("BlockUpdates");
		blockUpdatesInterval = tag.getInt("BlockUpdatesInterval");
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean("SlowTime", slowTime);
		tag.putBoolean("BlockUpdates", blockUpdates);
		tag.putInt("BlockUpdatesInterval", blockUpdatesInterval);
	}

	public boolean isTimeSlowed() {
		return slowTime;
	}

	public void setSlowTime(boolean slowTime) {
		this.slowTime = slowTime;
		entity.syncComponent(ArcanusComponents.SLOW_TIME_COMPONENT);
	}

	public boolean areUpdatesBlocked() {
		return blockUpdates;
	}

	public void setBlockUpdates(boolean blockUpdates) {
		this.blockUpdates = blockUpdates;
		entity.syncComponent(ArcanusComponents.SLOW_TIME_COMPONENT);
	}

	public int getBlockUpdatesInterval() {
		return blockUpdatesInterval;
	}

	public void setBlockUpdatesInterval(int interval) {
		this.blockUpdatesInterval = interval;
		entity.syncComponent(ArcanusComponents.SLOW_TIME_COMPONENT);
	}
}
