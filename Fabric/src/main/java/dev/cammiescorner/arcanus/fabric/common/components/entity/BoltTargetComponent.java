package dev.cammiescorner.arcanus.fabric.common.components.entity;

import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class BoltTargetComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity entity;
	private Vec3 pos = Vec3.ZERO;
	private int age;
	private boolean shouldRender;

	public BoltTargetComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		if(age >= 3 && shouldRender)
			setShouldRender(false);

		age++;
	}

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
		pos = new Vec3(tag.getDouble("PosX"), tag.getDouble("PosY"), tag.getDouble("PosZ"));
		shouldRender = tag.getBoolean("ShouldRender");
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
		tag.putDouble("PosX", pos.x());
		tag.putDouble("PosY", pos.y());
		tag.putDouble("PosZ", pos.z());
		tag.putBoolean("ShouldRender", shouldRender);
	}

	public Vec3 getPos() {
		return pos;
	}

	public void setPos(Vec3 pos) {
		this.pos = pos;
		ArcanusComponents.BOLT_TARGET.sync(entity);
	}

	public boolean shouldRender() {
		return shouldRender;
	}

	public void setShouldRender(boolean shouldRender) {
		this.shouldRender = shouldRender;
		ArcanusComponents.BOLT_TARGET.sync(entity);
	}

	public void setAge(int age) {
		this.age = age;
	}
}
