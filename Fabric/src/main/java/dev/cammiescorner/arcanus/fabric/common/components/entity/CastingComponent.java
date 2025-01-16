package dev.cammiescorner.arcanus.fabric.common.components.entity;

import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class CastingComponent implements AutoSyncedComponent {
	private final LivingEntity entity;
	private boolean casting = false;

	public CastingComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		casting = tag.getBoolean("IsCasting");
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean("IsCasting", casting);
	}

	public boolean isCasting() {
		return casting;
	}

	public void setCasting(boolean casting) {
		this.casting = casting;

		ArcanusComponents.CASTING_COMPONENT.sync(entity);
	}
}
