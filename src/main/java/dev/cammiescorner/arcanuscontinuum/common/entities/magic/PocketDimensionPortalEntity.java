package dev.cammiescorner.arcanuscontinuum.common.entities.magic;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class PocketDimensionPortalEntity extends Entity implements Targetable {
	private static final TrackedData<Integer> TRUE_AGE = DataTracker.registerData(PocketDimensionPortalEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private UUID casterId = Util.NIL_UUID;
	private double pullStrength;

	public PocketDimensionPortalEntity(EntityType<?> variant, World world) {
		super(variant, world);
	}

	@Override
	public void tick() {
		if (!getWorld().isClient() && (getCaster() == null || !getCaster().isAlive()) || getTrueAge() > ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.baseLifeSpan + 20) {
			kill();
			return;
		}

		if (getTrueAge() <= ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.baseLifeSpan) {
			Box box = new Box(0, 0, 0, 0, 0, 0).expand(4 + pullStrength).offset(getPos());
			double boxRadius = box.getXLength() / 2;
			double boxRadiusSq = boxRadius * boxRadius;

			// TODO tag for entities that are immune to portals, which should include portals themselves
			if (!getWorld().isClient()) {
				if (getTrueAge() > ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.portalGrowTime) {
					getWorld().getOtherEntities(this, getBoundingBox(), entity -> entity.canUsePortals() && !entity.isSpectator() && !(entity instanceof PocketDimensionPortalEntity) && (!(entity instanceof PlayerEntity player) || !ArcanusComponents.hasPortalCoolDown(player))).forEach(entity -> {
						ArcanusComponents.teleportToPocketDimension(getServer(), getCaster(), entity);
					});

					if (ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.canSuckEntitiesIn) {
						getWorld().getOtherEntities(this, box, entity -> entity.isAlive() && !entity.isSpectator() && !(entity instanceof PocketDimensionPortalEntity) && !(entity instanceof PlayerEntity player && (player.isCreative() || ArcanusComponents.hasPortalCoolDown(player)))).forEach(entity -> {
							double distanceSq = getPos().squaredDistanceTo(entity.getPos());

							if (distanceSq <= boxRadiusSq && distanceSq != 0) {
								Vec3d direction = getPos().subtract(entity.getPos()).normalize();
								double inverseSq = 1 / distanceSq;

								entity.addVelocity(direction.multiply(inverseSq));
								entity.velocityModified = true;
							}
						});
					}
				}
			}
			else {
				for (int i = 0; i < boxRadius * 2; ++i) {
					double particleX = getPos().getX() + random.nextGaussian() * boxRadius;
					double particleY = getPos().getY();
					double particleZ = getPos().getZ() + random.nextGaussian() * boxRadius;
					Vec3d particlePos = new Vec3d(particleX, particleY, particleZ);
					Vec3d particleVelocity = particlePos.subtract(getPos());

					if (particlePos.squaredDistanceTo(getPos()) <= boxRadiusSq) {
						getWorld().addParticle(ParticleTypes.PORTAL, particleX, particleY, particleZ, particleVelocity.getX(), particleVelocity.getY(), particleVelocity.getZ());
					}
				}
			}


		}

		super.tick();
		dataTracker.set(TRUE_AGE, getTrueAge() + 1);
	}

	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(TRUE_AGE, 0);
	}

	@Override
	public boolean collides() {
		return true;
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound tag) {
		casterId = tag.getUuid("CasterId");
		pullStrength = tag.getDouble("PullStrength");
		dataTracker.set(TRUE_AGE, tag.getInt("TrueAge"));
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound tag) {
		tag.putUuid("CasterId", casterId);
		tag.putDouble("PullStrength", pullStrength);
		tag.putInt("TrueAge", dataTracker.get(TRUE_AGE));
	}

	private PlayerEntity getCaster() {
		var server = getServer();
		if (server != null) {
			for (ServerWorld serverWorld : server.getWorlds()) {
				if (serverWorld.getEntity(casterId) instanceof PlayerEntity caster) {
					return caster;
				}
			}
		}

		return null;
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

	public void setProperties(UUID casterId, Vec3d pos, double pullStrength, Color color) {
		setPosition(pos);
		this.casterId = casterId;
		this.pullStrength = pullStrength;
		this.setColor(color);
	}
}
