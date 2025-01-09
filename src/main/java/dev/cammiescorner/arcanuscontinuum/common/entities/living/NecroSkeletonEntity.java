package dev.cammiescorner.arcanuscontinuum.common.entities.living;

import dev.cammiescorner.arcanuscontinuum.common.entities.Summon;
import dev.cammiescorner.arcanuscontinuum.common.entities.goals.CasterHurtByTargetGoal;
import dev.cammiescorner.arcanuscontinuum.common.entities.goals.CasterHurtTargetGoal;
import dev.cammiescorner.arcanuscontinuum.common.entities.goals.FollowCasterGoal;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.UUID;

public class NecroSkeletonEntity extends AbstractSkeleton implements Summon {
	private static final UUID HEALTH_UUID = UUID.fromString("65691cf4-6e7e-445f-8e5c-bb37a2b660d4");
	private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(NecroSkeletonEntity.class, EntityDataSerializers.INT);;
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private UUID ownerId = Util.NIL_UUID;
	private UUID persistentAngerTarget = Util.NIL_UUID;

	public NecroSkeletonEntity(EntityType<? extends AbstractSkeleton> entityType, Level world) {
		super(entityType, world);
		Arrays.fill(armorDropChances, 0);
		Arrays.fill(handDropChances, 0);
		xpReward = 0;
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, false));
		goalSelector.addGoal(2, new FollowCasterGoal<>(this, 1, 8f, 3f, false));
		goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8f));
		goalSelector.addGoal(4, new LookAtPlayerGoal(this, Monster.class, 8f));
		goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new CasterHurtByTargetGoal<>(this));
		targetSelector.addGoal(2, new CasterHurtTargetGoal<>(this));
		targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
		targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
		targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Monster.class, true, otherEntity -> !(otherEntity instanceof NecroSkeletonEntity necroSkeleton) || !necroSkeleton.getOwnerId().equals(getOwnerId())));
		targetSelector.addGoal(6, new ResetUniversalAngerTargetGoal<>(this, true));
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_REMAINING_ANGER_TIME, 0);
	}

	@Override
	public void tick() {
		if(!level().isClientSide() && (getCaster() == null || !getCaster().isAlive()))
			kill();

		super.tick();
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if(level() instanceof ServerLevel serverLevel)
			updatePersistentAnger(serverLevel, true);
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
		readPersistentAngerSaveData(this.level(), tag);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putUUID("OwnerId", ownerId);
		addPersistentAngerSaveData(tag);
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
	}

	@Override
	public LivingEntity getCaster() {
		if(level() instanceof ServerLevel serverWorld && serverWorld.getEntity(ownerId) instanceof LivingEntity caster)
			return caster;

		return null;
	}

	@Override
	public UUID getOwnerId() {
		return ownerId;
	}

	@Override
	public void setOwner(LivingEntity entity) {
		ownerId = entity.getUUID();
	}

	@Override
	public boolean wantsToAttack(LivingEntity target, LivingEntity caster) {
		if(target instanceof Player playerTarget && caster instanceof Player playerCaster && !playerCaster.canHarmPlayer(playerTarget))
			return false;
		else if(target instanceof AbstractHorse horse && horse.isTamed())
			return false;

		return !(target instanceof TamableAnimal tamable) || !tamable.isTame();
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

	@Override
	public int getRemainingPersistentAngerTime() {
		return entityData.get(DATA_REMAINING_ANGER_TIME);
	}

	@Override
	public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
		entityData.set(DATA_REMAINING_ANGER_TIME, remainingPersistentAngerTime);
	}

	@Override
	public @Nullable UUID getPersistentAngerTarget() {
		return persistentAngerTarget;
	}

	@Override
	public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {
		this.persistentAngerTarget = persistentAngerTarget;
	}

	@Override
	public void startPersistentAngerTimer() {
		setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(random));
	}
}
