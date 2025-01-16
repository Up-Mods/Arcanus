package dev.cammiescorner.arcanus.fabric.common.entities.magic;

import dev.cammiescorner.arcanus.api.entities.Targetable;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ManaShieldEntity extends Entity implements Targetable {
	private static final EntityDataAccessor<Integer> MAX_AGE = SynchedEntityData.defineId(ManaShieldEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> TRUE_AGE = SynchedEntityData.defineId(ManaShieldEntity.class, EntityDataSerializers.INT);
	public static final ThreadLocal<Entity> COLLIDING_ENTITY = new ThreadLocal<>();
	public UUID ownerId = Util.NIL_UUID;

	public ManaShieldEntity(EntityType<? extends Entity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public void tick() {
		if(!level().isClientSide() && (getCaster() == null || !getCaster().isAlive())) {
			kill();
			return;
		}

		List<ManaShieldEntity> list = level().getEntitiesOfClass(ManaShieldEntity.class, getBoundingBox(), EntitySelector.ENTITY_STILL_ALIVE);

		if(!list.isEmpty()) {
			list.sort(Comparator.comparingInt(ManaShieldEntity::getTrueAge).reversed());
			int i = level().getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);

			if(i > 0 && list.size() > i - 1) {
				int j = 0;

				for(ManaShieldEntity ignored : list)
					++j;

				if(j > i - 1) {
					kill();
					return;
				}
			}
		}

		if(level().getEntities(this, getBoundingBox(), entity -> entity instanceof LivingEntity && entity.isAlive()).isEmpty() && getTrueAge() + 20 < getMaxAge())
			entityData.set(MAX_AGE, getTrueAge() + 20);

		if(getTrueAge() >= getMaxAge()) {
			kill();
			return;
		}

		super.tick();
		entityData.set(TRUE_AGE, getTrueAge() + 1);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(MAX_AGE, 0);
		entityData.define(TRUE_AGE, 0);
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
		entityData.set(MAX_AGE, tag.getInt("MaxAge"));
		entityData.set(TRUE_AGE, tag.getInt("TrueAge"));
		ownerId = tag.getUUID("OwnerId");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putInt("MaxAge", getMaxAge());
		tag.putInt("TrueAge", getTrueAge());
		tag.putUUID("OwnerId", ownerId);
	}

	@Override
	public boolean canBeCollidedWith() {
		if(COLLIDING_ENTITY.get() == null)
			return true;

		return getTrueAge() + 20 < getMaxAge() && !COLLIDING_ENTITY.get().getBoundingBox().intersects(getBoundingBox());
	}

	@Override
	public boolean isPickable() {
		return !isRemoved();
	}

	private LivingEntity getCaster() {
		if(level() instanceof ServerLevel serverWorld && serverWorld.getEntity(ownerId) instanceof LivingEntity caster)
			return caster;
		return null;
	}

	public int getMaxAge() {
		return entityData.get(MAX_AGE);
	}

	public void setMaxAge(int maxAge) {
		entityData.set(MAX_AGE, maxAge);
	}

	public int getTrueAge() {
		return entityData.get(TRUE_AGE);
	}

	public UUID getOwnerId() {
		return ownerId;
	}

	public void setProperties(UUID ownerId, Vec3 pos, int maxAge) {
		this.setPos(pos);
		this.setMaxAge(maxAge);
		this.ownerId = ownerId;
	}
}
