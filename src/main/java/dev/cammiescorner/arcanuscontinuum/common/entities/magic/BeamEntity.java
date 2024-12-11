package dev.cammiescorner.arcanuscontinuum.common.entities.magic;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class BeamEntity extends Entity implements Targetable {
	private static final TrackedData<Integer> OWNER_ID = DataTracker.registerData(BeamEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> MAX_AGE = DataTracker.registerData(BeamEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> IS_ON_ENTITY = DataTracker.registerData(BeamEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private final List<SpellEffect> effects = new ArrayList<>();
	private final List<SpellGroup> groups = new ArrayList<>();
	private UUID casterId = Util.NIL_UUID;
	private ItemStack stack = ItemStack.EMPTY;
	private int groupIndex = 0;
	private double potency = 1F;

	public BeamEntity(EntityType<?> variant, World world) {
		super(variant, world);
	}

	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(OWNER_ID, -1);
		dataTracker.startTracking(MAX_AGE, 40);
		dataTracker.startTracking(IS_ON_ENTITY, false);
	}

	@Override
	public void tick() {
		if(age >= dataTracker.get(MAX_AGE) || (getCaster() == null || squaredDistanceTo(getCaster()) > 273 || !getCaster().isAlive()) || (dataTracker.get(IS_ON_ENTITY) ? getVehicle() == null : getWorld().isAir(getBlockPos()))) {
			if(!getWorld().isClient())
				kill();

			return;
		}

		if(!getWorld().isClient() && dataTracker.get(OWNER_ID) == -1 && getCaster() != null)
			dataTracker.set(OWNER_ID, getCaster().getId());

		super.tick();
	}

	@Override
	public void kill() {
		if(!getWorld().isClient() && getCaster() != null) {
			if(squaredDistanceTo(getCaster()) <= 273) {
				HitResult target = dataTracker.get(IS_ON_ENTITY) && getVehicle() != null ? new EntityHitResult(getVehicle()) : new BlockHitResult(getPos(), Direction.UP, getBlockPos(), true);

				for(SpellEffect effect : new HashSet<>(effects))
					effect.effect(getCaster(), this, getWorld(), target, effects, stack, potency + 0.15);

				if(target instanceof EntityHitResult entityHit)
					SpellShape.castNext(getCaster(), target.getPos(), entityHit.getEntity(), (ServerWorld) getWorld(), stack, groups, groupIndex, potency);
				else
					SpellShape.castNext(getCaster(), target.getPos(), this, (ServerWorld) getWorld(), stack, groups, groupIndex, potency);
			}
		}

		super.kill();
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
		effects.clear();
		groups.clear();

		dataTracker.set(OWNER_ID, tag.getInt("OwnerId"));
		dataTracker.set(MAX_AGE, tag.getInt("MaxAge"));
		dataTracker.set(IS_ON_ENTITY, tag.getBoolean("IsOnBoolean"));
		casterId = tag.getUuid("CasterId");
		stack = ItemStack.fromNbt(tag.getCompound("ItemStack"));
		groupIndex = tag.getInt("GroupIndex");
		potency = tag.getDouble("Potency");

		NbtList effectList = tag.getList("Effects", NbtElement.STRING_TYPE);
		NbtList groupsList = tag.getList("SpellGroups", NbtElement.COMPOUND_TYPE);

		for(int i = 0; i < effectList.size(); i++)
			effects.add((SpellEffect) Arcanus.SPELL_COMPONENTS.get(new Identifier(effectList.getString(i))));
		for(int i = 0; i < groupsList.size(); i++)
			groups.add(SpellGroup.fromNbt(groupsList.getCompound(i)));
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound tag) {
		NbtList effectList = new NbtList();
		NbtList groupsList = new NbtList();

		tag.putInt("OwnerId", dataTracker.get(OWNER_ID));
		tag.putInt("MaxAge", dataTracker.get(MAX_AGE));
		tag.putBoolean("IsOnBoolean", dataTracker.get(IS_ON_ENTITY));
		tag.putUuid("CasterId", casterId);
		tag.put("ItemStack", stack.writeNbt(new NbtCompound()));
		tag.putInt("GroupIndex", groupIndex);
		tag.putDouble("Potency", potency);

		for(SpellEffect effect : effects)
			effectList.add(NbtString.of(Arcanus.SPELL_COMPONENTS.getId(effect).toString()));
		for(SpellGroup group : groups)
			groupsList.add(group.toNbt());

		tag.put("Effects", effectList);
		tag.put("SpellGroups", groupsList);
	}

	@Override
	public boolean shouldRender(double sqDistance) {
		return sqDistance <= 64 * 64;
	}

	public int getColour() {
		return ArcanusComponents.getColour(this);
	}

	public void setColour(int colour) {
		ArcanusComponents.setColour(this, colour);
	}

	public Vec3d getBeamPos(float tickDelta) {
		return getVehicle() != null ? getVehicle().getLerpedPos(tickDelta).add(0, getVehicle().getHeight() / 2, 0) : getLerpedPos(tickDelta);
	}

	public float getBeamProgress(float tickDelta) {
		return (age + tickDelta) / (float) dataTracker.get(MAX_AGE);
	}

	public LivingEntity getCaster() {
		if(getWorld() instanceof ServerWorld serverWorld && serverWorld.getEntity(casterId) instanceof LivingEntity caster)
			return caster;
		else if(getWorld().isClient() && getWorld().getEntityById(dataTracker.get(OWNER_ID)) instanceof LivingEntity caster)
			return caster;

		return null;
	}

	public void setProperties(@Nullable LivingEntity caster, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> groups, int groupIndex, int maxAge, int colour, double potency, boolean isOnEntity) {
		this.effects.clear();
		this.groups.clear();
		this.effects.addAll(effects);
		this.groups.addAll(groups);
		setColour(colour);

		if(caster != null) {
			casterId = caster.getUuid();
			dataTracker.set(OWNER_ID, caster.getId());
		}

		this.stack = stack;
		this.groupIndex = groupIndex;
		dataTracker.set(MAX_AGE, maxAge);
		this.potency = potency;
		dataTracker.set(IS_ON_ENTITY, isOnEntity);
	}
}
