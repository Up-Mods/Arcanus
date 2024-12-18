package dev.cammiescorner.arcanuscontinuum.common.entities.living;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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

public class NecroSkeletonEntity extends AbstractSkeleton implements SmartBrainOwner<NecroSkeletonEntity> {
	private static final UUID HEALTH_UUID = UUID.fromString("65691cf4-6e7e-445f-8e5c-bb37a2b660d4");
	private UUID ownerId = Util.NIL_UUID;

	public NecroSkeletonEntity(EntityType<? extends AbstractSkeleton> entityType, Level world) {
		super(entityType, world);
		Arrays.fill(armorDropChances, 0);
		Arrays.fill(handDropChances, 0);
		xpReward = 0;
	}

	@Override
	public void tick() {
		if(!level().isClientSide() && (getCaster() == null || !getCaster().isAlive()))
			kill();

		super.tick();
	}

	@Override
	protected SoundEvent getStepSound() {
		return SoundEvents.SKELETON_STEP;
	}

	@Override
	protected boolean isSunBurnTick() {
		return false;
	}

	@Override
	public boolean isAlliedTo(Entity other) {
		return super.isAlliedTo(other) || other.getUUID().equals(ownerId);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		ownerId = tag.getUUID("OwnerId");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putUUID("OwnerId", ownerId);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return AbstractSkeleton.createAttributes()
				.add(Attributes.ATTACK_DAMAGE, 1)
				.add(Attributes.MAX_HEALTH, 0);
	}

	@Override
	protected void customServerAiStep() {
		if(tickCount > 1200)
			kill();

		super.customServerAiStep();
		tickBrain(this);
	}

	@Override
	protected Brain.Provider<?> brainProvider() {
		return new SmartBrainProvider<>(this);
	}

	@Override
	public List<ExtendedSensor<NecroSkeletonEntity>> getSensors() {
		return ObjectArrayList.of(
				new NearbyPlayersSensor<NecroSkeletonEntity>().setPredicate((target, skeleton) -> !(getCaster() instanceof Player owner) || owner.canHarmPlayer(target)),
				new NearbyLivingEntitySensor<NecroSkeletonEntity>().setPredicate((target, skeleton) ->
						!target.getUUID().equals(ownerId) &&
						(!(target instanceof NecroSkeletonEntity other) || !other.ownerId.equals(ownerId)) &&
						(!(target instanceof OwnableEntity tameable) || !ownerId.equals(tameable.getOwnerUUID())) &&
						(target instanceof IronGolem || target instanceof Wolf || target instanceof Monster)
				)
		);
	}

	@Override
	public BrainActivityGroup<NecroSkeletonEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(
				new AvoidEntity<>().avoiding(entity -> entity instanceof Wolf),
				new LookAtTargetSink(40, 300),
				new MoveToWalkTarget<>()
		);
	}

	@Override
	public BrainActivityGroup<NecroSkeletonEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
				new FirstApplicableBehaviour<NecroSkeletonEntity>(
						new TargetOrRetaliate<>().isAllyIf(Entity::isAlliedTo),
						new SetPlayerLookTarget<>(),
						new SetRandomLookTarget<>(),
						new FollowEntity<>().following(self -> {
							if(level() instanceof ServerLevel server)
								return server.getEntity(ownerId);

							return null;
						}).teleportToTargetAfter(12)
				),
				new OneRandomBehaviour<>(
						new SetRandomWalkTarget<>().speedModifier(1),
						new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
				)
		);
	}

	@Override
	public BrainActivityGroup<NecroSkeletonEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(
				new InvalidateAttackTarget<>(),
				new FirstApplicableBehaviour<>(
						new AnimatableMeleeAttack<>(0).whenStarting(entity -> setAggressive(true)).whenStarting(entity -> setAggressive(false))
				)
		);
	}

	private LivingEntity getCaster() {
		if(level() instanceof ServerLevel serverWorld && serverWorld.getEntity(ownerId) instanceof LivingEntity caster)
			return caster;

		return null;
	}

	public void setMaxHealth(double health) {
		AttributeInstance healthAttr = getAttribute(Attributes.MAX_HEALTH);

		if(healthAttr != null) {
			if(healthAttr.getModifier(HEALTH_UUID) != null)
				healthAttr.removeModifier(HEALTH_UUID);

			healthAttr.addPermanentModifier(new AttributeModifier(HEALTH_UUID, "Health modifier", health, AttributeModifier.Operation.ADDITION));
			setHealth(getMaxHealth());
		}
	}

	public UUID getOwnerId() {
		return ownerId;
	}

	public void setOwner(LivingEntity entity) {
		ownerId = entity.getUUID();
	}
}
