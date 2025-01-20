package dev.cammiescorner.arcanuscontinuum.common.entities.magic;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class Beam extends Entity implements Targetable {
	private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(Beam.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> MAX_AGE = SynchedEntityData.defineId(Beam.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> IS_ON_ENTITY = SynchedEntityData.defineId(Beam.class, EntityDataSerializers.BOOLEAN);
	private final List<SpellEffect> effects = new ArrayList<>();
	private final List<SpellGroup> groups = new ArrayList<>();
	private UUID casterId = Util.NIL_UUID;
	private ItemStack stack = ItemStack.EMPTY;
	private int groupIndex = 0;
	private double potency = 1F;

	public Beam(EntityType<?> variant, Level world) {
		super(variant, world);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(OWNER_ID, -1);
		entityData.define(MAX_AGE, 40);
		entityData.define(IS_ON_ENTITY, false);
	}

	@Override
	public void tick() {
		if(tickCount >= entityData.get(MAX_AGE) || (getCaster() == null || distanceToSqr(getCaster()) > 273 || !getCaster().isAlive()) || (entityData.get(IS_ON_ENTITY) ? getVehicle() == null : level().isEmptyBlock(blockPosition()))) {
			if(!level().isClientSide())
				kill();

			return;
		}

		if(!level().isClientSide() && entityData.get(OWNER_ID) == -1 && getCaster() != null)
			entityData.set(OWNER_ID, getCaster().getId());

		super.tick();
	}

	@Override
	public void kill() {
		if(!level().isClientSide() && getCaster() != null) {
			if(distanceToSqr(getCaster()) <= 273) {
				HitResult target = entityData.get(IS_ON_ENTITY) && getVehicle() != null ? new EntityHitResult(getVehicle()) : new BlockHitResult(position(), Direction.UP, blockPosition(), true);

				for(SpellEffect effect : new HashSet<>(effects))
					effect.effect(getCaster(), this, level(), target, effects, stack, potency + 0.15);

				if(target instanceof EntityHitResult entityHit)
					SpellShape.castNext(getCaster(), target.getLocation(), entityHit.getEntity(), (ServerLevel) level(), stack, groups, groupIndex, potency);
				else
					SpellShape.castNext(getCaster(), target.getLocation(), this, (ServerLevel) level(), stack, groups, groupIndex, potency);
			}
		}

		super.kill();
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
		effects.clear();
		groups.clear();

		entityData.set(OWNER_ID, tag.getInt("OwnerId"));
		entityData.set(MAX_AGE, tag.getInt("MaxAge"));
		entityData.set(IS_ON_ENTITY, tag.getBoolean("IsOnBoolean"));
		casterId = tag.getUUID("CasterId");
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

		tag.putInt("OwnerId", entityData.get(OWNER_ID));
		tag.putInt("MaxAge", entityData.get(MAX_AGE));
		tag.putBoolean("IsOnBoolean", entityData.get(IS_ON_ENTITY));
		tag.putUUID("CasterId", casterId);
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

	public Vec3 getBeamPos(float tickDelta) {
		return getVehicle() != null ? getVehicle().getPosition(tickDelta).add(0, getVehicle().getBbHeight() / 2, 0) : getPosition(tickDelta);
	}

	public float getBeamProgress(float tickDelta) {
		return (tickCount + tickDelta) / (float) entityData.get(MAX_AGE);
	}

	public LivingEntity getCaster() {
		if(level() instanceof ServerLevel serverWorld && serverWorld.getEntity(casterId) instanceof LivingEntity caster)
			return caster;
		else if(level().isClientSide() && level().getEntity(entityData.get(OWNER_ID)) instanceof LivingEntity caster)
			return caster;

		return null;
	}

	public void setProperties(@Nullable LivingEntity caster, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> groups, int groupIndex, int maxAge, double potency, boolean isOnEntity) {
		this.effects.clear();
		this.groups.clear();
		this.effects.addAll(effects);
		this.groups.addAll(groups);

		if(caster != null) {
			casterId = caster.getUUID();
			entityData.set(OWNER_ID, caster.getId());
			ArcanusHelper.copyMagicColor(this, caster);
		}

		this.stack = stack;
		this.groupIndex = groupIndex;
		entityData.set(MAX_AGE, maxAge);
		this.potency = potency;
		entityData.set(IS_ON_ENTITY, isOnEntity);
	}
}
