package dev.cammiescorner.arcanuscontinuum.common.entities.magic;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class EntangledOrb extends Entity implements Targetable {
	private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(EntangledOrb.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(EntangledOrb.class, EntityDataSerializers.INT);
	private final List<SpellEffect> effects = new ArrayList<>();
	private final List<SpellGroup> groups = new ArrayList<>();
	private UUID casterId = Util.NIL_UUID;
	private UUID targetId = Util.NIL_UUID;
	private ItemStack stack = ItemStack.EMPTY;
	private int groupIndex = 0;
	private double potency = 1F;

	public EntangledOrb(EntityType<?> variant, Level world) {
		super(variant, world);
		this.noPhysics = true;
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(OWNER_ID, -1);
		entityData.define(TARGET_ID, -1);
	}

	@Override
	public void tick() {
		LivingEntity caster = getCaster();
		LivingEntity target = getTarget();

		if(caster == null || (!level().isClientSide && !ArcanusComponents.getGuardianOrbId(caster).equals(getUUID())) || target == null || caster.distanceToSqr(target) > 32 * 32) {
			kill();
			return;
		}

		if(!level().isClientSide() && entityData.get(TARGET_ID) == -1)
			entityData.set(TARGET_ID, target.getId());

		setYRot(target.getYRot());
		setYBodyRot(target.yBodyRot);
		setYHeadRot(target.yHeadRot);
		setXRot(target.getXRot());

		double cosYaw = Math.cos(Math.toRadians(-target.yBodyRot));
		double sinYaw = Math.sin(Math.toRadians(-target.yBodyRot));
		Vec3 rotation = new Vec3(sinYaw, 0, cosYaw).scale(target.getBbWidth() / 2 + 0.2).yRot((float) Math.toRadians(105));
		Vec3 targetPos = target.position().add(rotation.x(), target.getEyeHeight(target.getPose()) - 0.2, rotation.z());
		Vec3 direction = targetPos.subtract(position());
		move(MoverType.SELF, direction.scale(0.25f));

		if(tickCount % 8 == 0) {
			Vec3 vel = (direction.lengthSqr() <= 1 ? direction : direction.normalize()).scale(0.125f);
			level().addParticle(ParticleTypes.END_ROD, getX(), getY() + getBbHeight() / 2, getZ(), vel.x(), vel.y(), vel.z());
		}

		if(tickCount % 100 == 0 && ArcanusComponents.drainMana(caster, ArcanusConfig.SpellShapes.EntangledOrbShapeProperties.baseManaDrain * effects.size(), false)) {
			EntityHitResult hitResult = new EntityHitResult(target);

			for(SpellEffect effect : new HashSet<>(effects))
				effect.effect(caster, this, level(), hitResult, effects, stack, potency);
		}

		super.tick();
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		kill();
		return true;
	}

	@Override
	public void remove(RemovalReason reason) {
		if(reason == RemovalReason.KILLED && !level().isClientSide() && getCaster() != null && getTarget() != null)
			SpellShape.castNext(getCaster(), getTarget().position(), getTarget(), (ServerLevel) level(), stack, groups, groupIndex, potency);

		super.remove(reason);
	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		effects.clear();
		groups.clear();

		casterId = tag.getUUID("CasterId");
		targetId = tag.getUUID("TargetId");
		stack = ItemStack.of(tag.getCompound("ItemStack"));
		groupIndex = tag.getInt("GroupIndex");
		potency = tag.getDouble("Potency");

		ListTag effectList = tag.getList("Effects", Tag.TAG_STRING);
		ListTag groupsList = tag.getList("SpellGroups", Tag.TAG_COMPOUND);

		for(int i = 0; i < effectList.size(); i++)
			effects.add((SpellEffect) Arcanus.SPELL_COMPONENTS.get(new ResourceLocation(effectList.getString(i))));
		for(int i = 0; i < groupsList.size(); i++)
			groups.add(SpellGroup.fromNbt(groupsList.getCompound(i)));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		ListTag effectList = new ListTag();
		ListTag groupsList = new ListTag();

		tag.putUUID("CasterId", casterId);
		tag.putUUID("TargetId", targetId);
		tag.put("ItemStack", stack.save(new CompoundTag()));
		tag.putInt("GroupIndex", groupIndex);
		tag.putDouble("Potency", potency);

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

	public UUID getCasterId() {
		return casterId;
	}

	public LivingEntity getCaster() {
		if(level() instanceof ServerLevel serverWorld && serverWorld.getEntity(getCasterId()) instanceof LivingEntity caster)
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

	public void setProperties(@Nullable LivingEntity caster, LivingEntity target, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> groups, int groupIndex, double potency) {
		this.effects.clear();
		this.groups.clear();
		this.effects.addAll(effects);
		this.groups.addAll(groups);

		if(caster != null) {
			this.casterId = caster.getUUID();
			this.entityData.set(OWNER_ID, caster.getId());

			ArcanusComponents.setGuardianOrbManaLock(caster, getUUID(), effects.size());
			ArcanusHelper.copyMagicColor(this, caster);
		}

		this.targetId = target.getUUID();
		this.entityData.set(TARGET_ID, target.getId());
		this.stack = stack;
		this.groupIndex = groupIndex;
		this.potency = potency;
	}
}
