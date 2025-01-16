package dev.cammiescorner.arcanuscontinuum.common.entities.magic;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class AggressorbEntity extends ThrowableProjectile implements Targetable {
	private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(AggressorbEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(AggressorbEntity.class, EntityDataSerializers.INT);
	private final List<SpellEffect> effects = new ArrayList<>();
	private final List<SpellGroup> groups = new ArrayList<>();
	private UUID casterId = Util.NIL_UUID;
	private UUID targetId = Util.NIL_UUID;
	private ItemStack stack = ItemStack.EMPTY;
	private int groupIndex = 0;
	private double potency = 1F;
	private boolean boundToTarget = true;

	public AggressorbEntity(EntityType<? extends ThrowableProjectile> variant, Level world) {
		super(variant, world);
		noPhysics = true;
		setNoGravity(true);
	}

	@Override
	public boolean canFreeze() {
		return super.canFreeze();
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(OWNER_ID, -1);
		entityData.define(TARGET_ID, -1);
	}

	@Override
	public void tick() {
		if(getCaster() == null || getTarget() == null || distanceToSqr(getTarget()) > (64 * 64)) {
			kill();
			return;
		}

		if(!level().isClientSide()) {
			if(entityData.get(TARGET_ID) == -1 && getTarget() != null)
				entityData.set(TARGET_ID, getTarget().getId());
		}

		if(isBoundToTarget()) {
			int orbCount = ArcanusComponents.aggressorbCount(getTarget());
			int orbIndex = ArcanusComponents.aggressorbIndex(getTarget(), this) + 1;
			double angle = Math.toRadians(360d / orbCount * orbIndex);
			double cosYaw = Math.cos(Math.toRadians(-getTarget().yBodyRot));
			double sinYaw = Math.sin(Math.toRadians(-getTarget().yBodyRot));
			double radius = getTarget().getBbHeight() / 1.5;
			double rotXZ = Math.sin(getTarget().tickCount * 0.1 + angle) * radius;
			double rotY = Math.cos(getTarget().tickCount * 0.1 + angle) * radius;
			Vec3 bodyYaw = new Vec3(sinYaw, 1, cosYaw);
			Vec3 offset = new Vec3(sinYaw, 0, cosYaw).scale(-0.75);
			Vec3 imInSpainWithoutTheA = bodyYaw.multiply(rotXZ, rotY, rotXZ).yRot((float) Math.toRadians(90));
			Vec3 targetPos = getTarget().position().add(0, radius, 0).add(imInSpainWithoutTheA).add(offset);
			Vec3 direction = targetPos.subtract(position());
			move(MoverType.SELF, direction);
		}
		else {
			noPhysics = false;
		}

		super.tick();
	}

	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		if(!isBoundToTarget() && !level().isClientSide()) {
			for(SpellEffect effect : new HashSet<>(effects))
				effect.effect(getCaster(), this, level(), entityHitResult, effects, stack, potency);

			SpellShape.castNext(getCaster(), position(), entityHitResult.getEntity(), (ServerLevel) level(), stack, groups, groupIndex, potency);

			kill();
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult blockHitResult) {
		if(!isBoundToTarget() && !level().isClientSide()) {
			for(SpellEffect effect : new HashSet<>(effects))
				effect.effect(getCaster(), this, level(), blockHitResult, effects, stack, potency);

			SpellShape.castNext(getCaster(), position(), this, (ServerLevel) level(), stack, groups, groupIndex, potency);

			super.onHitBlock(blockHitResult);
			kill();
		}
	}

	@Override
	public boolean isAttackable() {
		return true;
	}

	@Override
	public float getPickRadius() {
		return isBoundToTarget() ? 0f : 0.75f;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		Vec3 dir = getTarget().getEyePosition().subtract(position()).normalize();
		float pitch = (float) Math.toDegrees(Math.asin(-dir.y()));
		float yaw = (float) Math.toDegrees(-Math.atan2(dir.x(), dir.z()));

		setBoundToTarget(false);
		shootFromRotation(getTarget(), pitch, yaw, 0F, 3f, 1F);
		ArcanusComponents.removeAggressorbFromEntity(getTarget(), getUUID());

		return true;
	}

	@Override
	public void kill() {
		if(!level().isClientSide() && getTarget() != null)
			ArcanusComponents.removeAggressorbFromEntity(getTarget(), getUUID());

		super.kill();
	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		effects.clear();
		groups.clear();

		casterId = tag.getUUID("CasterId");
		targetId = tag.getUUID("TargetId");
		stack = ItemStack.of(tag.getCompound("ItemStack"));
		groupIndex = tag.getInt("GroupIndex");
		potency = tag.getDouble("Potency");
		boundToTarget = tag.getBoolean("BoundToTarget");

		ListTag effectList = tag.getList("Effects", Tag.TAG_STRING);
		ListTag groupsList = tag.getList("SpellGroups", Tag.TAG_COMPOUND);

		for(int i = 0; i < effectList.size(); i++)
			effects.add((SpellEffect) Arcanus.SPELL_COMPONENTS.get(new ResourceLocation(effectList.getString(i))));
		for(int i = 0; i < groupsList.size(); i++)
			groups.add(SpellGroup.fromNbt(groupsList.getCompound(i)));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		ListTag effectList = new ListTag();
		ListTag groupsList = new ListTag();

		tag.putUUID("CasterId", casterId);
		tag.putUUID("TargetId", targetId);
		tag.put("ItemStack", stack.save(new CompoundTag()));
		tag.putInt("GroupIndex", groupIndex);
		tag.putDouble("Potency", potency);
		tag.putBoolean("BoundToTarget", boundToTarget);

		for(SpellEffect effect : effects)
			effectList.add(StringTag.valueOf(Arcanus.SPELL_COMPONENTS.getKey(effect).toString()));
		for(SpellGroup group : groups)
			groupsList.add(group.toNbt());

		tag.put("Effects", effectList);
		tag.put("SpellGroups", groupsList);
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double sqDistance) {
		return sqDistance <= 64 * 64;
	}

	public LivingEntity getCaster() {
		if(level() instanceof ServerLevel serverWorld && serverWorld.getEntity(casterId) instanceof LivingEntity caster)
			return caster;
		else if(level().isClientSide() && level().getEntity(entityData.get(OWNER_ID)) instanceof LivingEntity caster)
			return caster;

		return null;
	}

	public LivingEntity getTarget() {
		if(level() instanceof ServerLevel serverWorld && serverWorld.getEntity(targetId) instanceof LivingEntity target)
			return target;
		else if(level().isClientSide() && level().getEntity(entityData.get(TARGET_ID)) instanceof LivingEntity target)
			return target;

		return null;
	}

	public boolean isBoundToTarget() {
		return boundToTarget;
	}

	public void setBoundToTarget(boolean boundToTarget) {
		this.boundToTarget = boundToTarget;
	}

	public void setProperties(@Nullable LivingEntity caster, LivingEntity target, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> groups, int groupIndex, double potency) {
		this.effects.clear();
		this.groups.clear();
		this.effects.addAll(effects);
		this.groups.addAll(groups);

		if(caster != null) {
			this.casterId = caster.getUUID();
			this.entityData.set(OWNER_ID, caster.getId());
			ArcanusHelper.copyMagicColor(this, caster);
		}

		ArcanusComponents.addAggressorbToEntity(target, getUUID());
		setBoundToTarget(true);

		this.targetId = target.getUUID();
		this.entityData.set(TARGET_ID, target.getId());
		this.stack = stack;
		this.groupIndex = groupIndex;
		this.potency = potency;
	}
}
