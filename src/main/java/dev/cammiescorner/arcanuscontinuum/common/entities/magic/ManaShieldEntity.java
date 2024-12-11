package dev.cammiescorner.arcanuscontinuum.common.entities.magic;

import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ManaShieldEntity extends Entity implements Targetable {
	private static final TrackedData<Integer> MAX_AGE = DataTracker.registerData(ManaShieldEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> TRUE_AGE = DataTracker.registerData(ManaShieldEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final ThreadLocal<Entity> COLLIDING_ENTITY = new ThreadLocal<>();
	public UUID ownerId = Util.NIL_UUID;

	public ManaShieldEntity(EntityType<? extends Entity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void tick() {
		if(!getWorld().isClient() && (getCaster() == null || !getCaster().isAlive())) {
			kill();
			return;
		}

		List<ManaShieldEntity> list = getWorld().getEntitiesByClass(ManaShieldEntity.class, getBoundingBox(), EntityPredicates.VALID_ENTITY);

		if(!list.isEmpty()) {
			list.sort(Comparator.comparingInt(ManaShieldEntity::getTrueAge).reversed());
			int i = getWorld().getGameRules().getInt(GameRules.MAX_ENTITY_CRAMMING);

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

		if(getWorld().getOtherEntities(this, getBoundingBox(), entity -> entity instanceof LivingEntity && entity.isAlive()).isEmpty() && getTrueAge() + 20 < getMaxAge())
			dataTracker.set(MAX_AGE, getTrueAge() + 20);

		if(getTrueAge() >= getMaxAge()) {
			kill();
			return;
		}

		super.tick();
		dataTracker.set(TRUE_AGE, getTrueAge() + 1);
	}

	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(MAX_AGE, 0);
		dataTracker.startTracking(TRUE_AGE, 0);
	}

	@Override
	public boolean doesRenderOnFire() {
		return false;
	}

	@Override
	public boolean isFireImmune() {
		return true;
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound tag) {
		dataTracker.set(MAX_AGE, tag.getInt("MaxAge"));
		dataTracker.set(TRUE_AGE, tag.getInt("TrueAge"));
		ownerId = tag.getUuid("OwnerId");
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound tag) {
		tag.putInt("MaxAge", getMaxAge());
		tag.putInt("TrueAge", getTrueAge());
		tag.putUuid("OwnerId", ownerId);
	}

	@Override
	public boolean isCollidable() {
		if(COLLIDING_ENTITY.get() == null)
			return true;

		return getTrueAge() + 20 < getMaxAge() && !COLLIDING_ENTITY.get().getBoundingBox().intersects(getBoundingBox());
	}

	@Override
	public boolean collides() {
		return !isRemoved();
	}

	private LivingEntity getCaster() {
		if(getWorld() instanceof ServerWorld serverWorld && serverWorld.getEntity(ownerId) instanceof LivingEntity caster)
			return caster;
		return null;
	}

	public int getMaxAge() {
		return dataTracker.get(MAX_AGE);
	}

	public void setMaxAge(int maxAge) {
		dataTracker.set(MAX_AGE, maxAge);
	}

	public int getTrueAge() {
		return dataTracker.get(TRUE_AGE);
	}

	public Color getColor() {
		return ArcanusComponents.getColor(this);
	}

	public void setColor(Color color) {
		ArcanusComponents.setColor(this, color);
	}

	public UUID getOwnerId() {
		return ownerId;
	}

	public void setProperties(UUID ownerId, Vec3d pos, Color color, int maxAge) {
		this.setPosition(pos);
		this.setColor(color);
		this.setMaxAge(maxAge);
		this.ownerId = ownerId;
	}
}
