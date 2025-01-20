package dev.cammiescorner.arcanuscontinuum.common.entities.magic;

import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class TemporalDilationField extends Entity implements Targetable {
	private static final EntityDataAccessor<Integer> TRUE_AGE = SynchedEntityData.defineId(TemporalDilationField.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> MAX_AGE = SynchedEntityData.defineId(TemporalDilationField.class, EntityDataSerializers.INT);

	public TemporalDilationField(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public void tick() {
		super.tick();

		if(getAge() > getMaxAge())
			kill();

		incrementAge();
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(TRUE_AGE, 0);
		entityData.define(MAX_AGE, 100);
	}

	@Override
	public boolean canBeHitByProjectile() {
		return false;
	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		entityData.set(TRUE_AGE, tag.getInt("TrueAge"));
		entityData.set(MAX_AGE, tag.getInt("MaxAge"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putInt("TrueAge", getAge());
		tag.putInt("MaxAge", getMaxAge());
	}

	public int getAge() {
		return entityData.get(TRUE_AGE);
	}

	public void incrementAge() {
		entityData.set(TRUE_AGE, getAge() + 1);
	}

	public int getMaxAge() {
		return entityData.get(MAX_AGE);
	}

	public void extendMaxAge(int extendAgeBy) {
		entityData.set(MAX_AGE, entityData.get(MAX_AGE) + extendAgeBy);
	}
}
