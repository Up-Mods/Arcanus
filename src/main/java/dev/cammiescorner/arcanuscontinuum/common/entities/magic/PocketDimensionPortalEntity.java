package dev.cammiescorner.arcanuscontinuum.common.entities.magic;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.common.components.level.PocketDimensionComponent;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class PocketDimensionPortalEntity extends Entity implements Targetable {
	private static final EntityDataAccessor<Integer> TRUE_AGE = SynchedEntityData.defineId(PocketDimensionPortalEntity.class, EntityDataSerializers.INT);
	private UUID casterId = Util.NIL_UUID;
	private double pullStrength;

	public PocketDimensionPortalEntity(EntityType<?> variant, Level world) {
		super(variant, world);
	}

	@Override
	public void tick() {
		var caster = getCaster();
		if (!level().isClientSide() && (caster == null || !caster.isAlive()) || getTrueAge() > ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.baseLifeSpan + 20) {
			kill();
			return;
		}

		if (getTrueAge() <= ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.baseLifeSpan) {
			AABB box = new AABB(0, 0, 0, 0, 0, 0).inflate(4 + pullStrength).move(position());
			double boxRadius = box.getXsize() / 2;
			double boxRadiusSq = boxRadius * boxRadius;

			// TODO tag for entities that are immune to portals, which should include portals themselves
			// TODO should probably also exclude fake players
			if (caster instanceof ServerPlayer serverCaster) {
				if (getTrueAge() > ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.portalGrowTime) {
					level().getEntities(this, getBoundingBox(), entity -> entity.canChangeDimensions() && !entity.isSpectator() && !(entity instanceof PocketDimensionPortalEntity) && (!(entity instanceof Player player) || !ArcanusComponents.hasPortalCoolDown(player))).forEach(entity -> {
						PocketDimensionComponent.get(getServer()).teleportToPocketDimension(serverCaster.getGameProfile(), entity);
					});

					if (ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.canSuckEntitiesIn) {
						level().getEntities(this, box, entity -> entity.isAlive() && !entity.isSpectator() && !(entity instanceof PocketDimensionPortalEntity) && !(entity instanceof Player player && (player.isCreative() || ArcanusComponents.hasPortalCoolDown(player)))).forEach(entity -> {
							double distanceSq = position().distanceToSqr(entity.position());

							if (distanceSq <= boxRadiusSq && distanceSq != 0) {
								Vec3 direction = position().subtract(entity.position()).normalize();
								double inverseSq = 1 / distanceSq;

								entity.addDeltaMovement(direction.scale(inverseSq));
								entity.hurtMarked = true;
							}
						});
					}
				}
			}
			else {
				for (int i = 0; i < boxRadius * 2; ++i) {
					double particleX = position().x() + random.nextGaussian() * boxRadius;
					double particleY = position().y();
					double particleZ = position().z() + random.nextGaussian() * boxRadius;
					Vec3 particlePos = new Vec3(particleX, particleY, particleZ);
					Vec3 particleVelocity = particlePos.subtract(position());

					if (particlePos.distanceToSqr(position()) <= boxRadiusSq) {
						level().addParticle(ParticleTypes.PORTAL, particleX, particleY, particleZ, particleVelocity.x(), particleVelocity.y(), particleVelocity.z());
					}
				}
			}


		}

		super.tick();
		entityData.set(TRUE_AGE, getTrueAge() + 1);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(TRUE_AGE, 0);
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		casterId = tag.getUUID("CasterId");
		pullStrength = tag.getDouble("PullStrength");
		entityData.set(TRUE_AGE, tag.getInt("TrueAge"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putUUID("CasterId", casterId);
		tag.putDouble("PullStrength", pullStrength);
		tag.putInt("TrueAge", entityData.get(TRUE_AGE));
	}

	private Player getCaster() {
		var server = getServer();
		if (server != null) {
			for (ServerLevel serverWorld : server.getAllLevels()) {
				if (serverWorld.getEntity(casterId) instanceof Player caster) {
					return caster;
				}
			}
		}

		return null;
	}

	public int getTrueAge() {
		return entityData.get(TRUE_AGE);
	}

	public void setProperties(UUID casterId, Vec3 pos, double pullStrength) {
		setPos(pos);
		this.casterId = casterId;
		this.pullStrength = pullStrength;
	}
}
