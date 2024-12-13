package dev.cammiescorner.arcanuscontinuum.common.entities.living;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.task.LookAroundTask;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.AvoidEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NecroSkeletonEntity extends AbstractSkeletonEntity implements SmartBrainOwner<NecroSkeletonEntity> {
	private static final UUID HEALTH_UUID = UUID.fromString("65691cf4-6e7e-445f-8e5c-bb37a2b660d4");
	private UUID ownerId = Util.NIL_UUID;

	public NecroSkeletonEntity(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
		super(entityType, world);
		Arrays.fill(armorDropChances, 0);
		Arrays.fill(handDropChances, 0);
		experiencePoints = 0;
	}

	@Override
	public void tick() {
		if(!getWorld().isClient() && (getCaster() == null || !getCaster().isAlive()))
			kill();

		super.tick();
	}

	@Override
	protected SoundEvent getStepSound() {
		return SoundEvents.ENTITY_SKELETON_STEP;
	}

	@Override
	protected boolean isAffectedByDaylight() {
		return false;
	}

	@Override
	public boolean isTeammate(Entity other) {
		return super.isTeammate(other) || other.getUuid().equals(ownerId);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound tag) {
		super.readCustomDataFromNbt(tag);
		ownerId = tag.getUuid("OwnerId");
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound tag) {
		super.writeCustomDataToNbt(tag);
		tag.putUuid("OwnerId", ownerId);
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return AbstractSkeletonEntity.createAttributes()
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1)
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 0);
	}

	@Override
	protected void mobTick() {
		if(age > 1200)
			kill();

		super.mobTick();
		tickBrain(this);
	}

	@Override
	protected Brain.Profile<?> createBrainProfile() {
		return new SmartBrainProvider<>(this);
	}

	@Override
	public List<ExtendedSensor<NecroSkeletonEntity>> getSensors() {
		return ObjectArrayList.of(
				new NearbyPlayersSensor<NecroSkeletonEntity>().setPredicate((target, skeleton) -> !(getCaster() instanceof PlayerEntity owner) || owner.shouldDamagePlayer(target)),
				new NearbyLivingEntitySensor<NecroSkeletonEntity>().setPredicate((target, skeleton) ->
						!target.getUuid().equals(ownerId) &&
						(!(target instanceof NecroSkeletonEntity other) || !other.ownerId.equals(ownerId)) &&
						(!(target instanceof Tameable tameable) || !ownerId.equals(tameable.getOwnerUuid())) &&
						(target instanceof IronGolemEntity || target instanceof WolfEntity || target instanceof HostileEntity)
				)
		);
	}

	@Override
	public BrainActivityGroup<NecroSkeletonEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(
				new AvoidEntity<>().avoiding(entity -> entity instanceof WolfEntity),
				new LookAroundTask(40, 300),
				new MoveToWalkTarget<>()
		);
	}

	@Override
	public BrainActivityGroup<NecroSkeletonEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
				new FirstApplicableBehaviour<NecroSkeletonEntity>(
						new TargetOrRetaliate<>().isAllyIf(Entity::isTeammate),
						new SetPlayerLookTarget<>(),
						new SetRandomLookTarget<>(),
						new FollowEntity<>().following(self -> {
							if(getWorld() instanceof ServerWorld server)
								return server.getEntity(ownerId);

							return null;
						}).teleportToTargetAfter(12)
				),
				new OneRandomBehaviour<>(
						new SetRandomWalkTarget<>().speedModifier(1),
						new Idle<>().runFor(entity -> entity.getRandom().rangeClosed(30, 60))
				)
		);
	}

	@Override
	public BrainActivityGroup<NecroSkeletonEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(
				new InvalidateAttackTarget<>(),
				new FirstApplicableBehaviour<>(
						new AnimatableMeleeAttack<>(0).whenStarting(entity -> setAttacking(true)).whenStarting(entity -> setAttacking(false))
				)
		);
	}

	private LivingEntity getCaster() {
		if(getWorld() instanceof ServerWorld serverWorld && serverWorld.getEntity(ownerId) instanceof LivingEntity caster)
			return caster;

		return null;
	}

	public void setMaxHealth(double health) {
		EntityAttributeInstance healthAttr = getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

		if(healthAttr != null) {
			if(healthAttr.getModifier(HEALTH_UUID) != null)
				healthAttr.removeModifier(HEALTH_UUID);

			healthAttr.addPersistentModifier(new EntityAttributeModifier(HEALTH_UUID, "Health modifier", health, EntityAttributeModifier.Operation.ADDITION));
			setHealth(getMaxHealth());
		}
	}

	public UUID getOwnerId() {
		return ownerId;
	}

	public void setOwner(LivingEntity entity) {
		ownerId = entity.getUuid();
	}
}
