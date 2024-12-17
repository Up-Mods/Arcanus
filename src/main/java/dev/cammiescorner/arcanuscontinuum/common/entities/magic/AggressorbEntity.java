package dev.cammiescorner.arcanuscontinuum.common.entities.magic;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class AggressorbEntity extends ThrownEntity implements Targetable {
	private static final TrackedData<Integer> OWNER_ID = DataTracker.registerData(AggressorbEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> TARGET_ID = DataTracker.registerData(AggressorbEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private final List<SpellEffect> effects = new ArrayList<>();
	private final List<SpellGroup> groups = new ArrayList<>();
	private UUID casterId = Util.NIL_UUID;
	private UUID targetId = Util.NIL_UUID;
	private ItemStack stack = ItemStack.EMPTY;
	private int groupIndex = 0;
	private double potency = 1F;
	private boolean boundToTarget = true;

	public AggressorbEntity(EntityType<? extends ThrownEntity> variant, World world) {
		super(variant, world);
		noClip = true;
		setNoGravity(true);
	}

	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(OWNER_ID, -1);
		dataTracker.startTracking(TARGET_ID, -1);
	}

	@Override
	public void tick() {
		if(getCaster() == null || getTarget() == null || squaredDistanceTo(getTarget()) > (64 * 64)) {
			kill();
			return;
		}

		if(!getWorld().isClient()) {
			if(dataTracker.get(TARGET_ID) == -1 && getTarget() != null)
				dataTracker.set(TARGET_ID, getTarget().getId());
		}

		if(isBoundToTarget()) {
			int orbCount = ArcanusComponents.aggressorbCount(getTarget());
			int orbIndex = ArcanusComponents.aggressorbIndex(getTarget(), this) + 1;
			double angle = Math.toRadians(360d / orbCount * orbIndex);
			double cosYaw = Math.cos(Math.toRadians(-getTarget().bodyYaw));
			double sinYaw = Math.sin(Math.toRadians(-getTarget().bodyYaw));
			double radius = getTarget().getHeight() / 1.5;
			double rotXZ = Math.sin(getTarget().age * 0.1 + angle) * radius;
			double rotY = Math.cos(getTarget().age * 0.1 + angle) * radius;
			Vec3d bodyYaw = new Vec3d(sinYaw, 1, cosYaw);
			Vec3d offset = new Vec3d(sinYaw, 0, cosYaw).multiply(-0.75);
			Vec3d imInSpainWithoutTheA = bodyYaw.multiply(rotXZ, rotY, rotXZ).rotateY((float) Math.toRadians(90));
			Vec3d targetPos = getTarget().getPos().add(0, radius, 0).add(imInSpainWithoutTheA).add(offset);
			Vec3d direction = targetPos.subtract(getPos());
			move(MovementType.SELF, direction);
		}
		else {
			noClip = false;
		}

		super.tick();
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		if(!isBoundToTarget() && !getWorld().isClient()) {
			for(SpellEffect effect : new HashSet<>(effects))
				effect.effect(getCaster(), this, getWorld(), entityHitResult, effects, stack, potency);

			SpellShape.castNext(getCaster(), getPos(), entityHitResult.getEntity(), (ServerWorld) getWorld(), stack, groups, groupIndex, potency);

			kill();
		}
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		if(!isBoundToTarget() && !getWorld().isClient()) {
			for(SpellEffect effect : new HashSet<>(effects))
				effect.effect(getCaster(), this, getWorld(), blockHitResult, effects, stack, potency);

			SpellShape.castNext(getCaster(), getPos(), this, (ServerWorld) getWorld(), stack, groups, groupIndex, potency);

			super.onBlockHit(blockHitResult);
			kill();
		}
	}

	@Override
	public boolean isAttackable() {
		return true;
	}

	@Override
	public float getTargetingMargin() {
		return isBoundToTarget() ? 0f : 0.75f;
	}

	@Override
	public boolean collides() {
		return true;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		Vec3d dir = getTarget().getEyePos().subtract(getPos()).normalize();
		float pitch = (float) Math.toDegrees(Math.asin(-dir.getY()));
		float yaw = (float) Math.toDegrees(-Math.atan2(dir.getX(), dir.getZ()));

		setBoundToTarget(false);
		setProperties(getTarget(), pitch, yaw, 0F, 3f, 1F);
		ArcanusComponents.removeAggressorbFromEntity(getTarget(), getUuid());

		return true;
	}

	@Override
	public void kill() {
		if(!getWorld().isClient() && getTarget() != null)
			ArcanusComponents.removeAggressorbFromEntity(getTarget(), getUuid());

		super.kill();
	}

	@Override
	public boolean doesRenderOnFire() {
		return false;
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound tag) {
		effects.clear();
		groups.clear();

		casterId = tag.getUuid("CasterId");
		targetId = tag.getUuid("TargetId");
		stack = ItemStack.fromNbt(tag.getCompound("ItemStack"));
		groupIndex = tag.getInt("GroupIndex");
		potency = tag.getDouble("Potency");
		boundToTarget = tag.getBoolean("BoundToTarget");

		NbtList effectList = tag.getList("Effects", NbtElement.STRING_TYPE);
		NbtList groupsList = tag.getList("SpellGroups", NbtElement.COMPOUND_TYPE);

		for(int i = 0; i < effectList.size(); i++)
			effects.add((SpellEffect) Arcanus.SPELL_COMPONENTS.get(new Identifier(effectList.getString(i))));
		for(int i = 0; i < groupsList.size(); i++)
			groups.add(SpellGroup.fromNbt(groupsList.getCompound(i)));
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound tag) {
		NbtList effectList = new NbtList();
		NbtList groupsList = new NbtList();

		tag.putUuid("CasterId", casterId);
		tag.putUuid("TargetId", targetId);
		tag.put("ItemStack", stack.writeNbt(new NbtCompound()));
		tag.putInt("GroupIndex", groupIndex);
		tag.putDouble("Potency", potency);
		tag.putBoolean("BoundToTarget", boundToTarget);

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

	public LivingEntity getCaster() {
		if(getWorld() instanceof ServerWorld serverWorld && serverWorld.getEntity(casterId) instanceof LivingEntity caster)
			return caster;
		else if(getWorld().isClient() && getWorld().getEntityById(dataTracker.get(OWNER_ID)) instanceof LivingEntity caster)
			return caster;

		return null;
	}

	public LivingEntity getTarget() {
		if(getWorld() instanceof ServerWorld serverWorld && serverWorld.getEntity(targetId) instanceof LivingEntity target)
			return target;
		else if(getWorld().isClient() && getWorld().getEntityById(dataTracker.get(TARGET_ID)) instanceof LivingEntity target)
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
			this.casterId = caster.getUuid();
			this.dataTracker.set(OWNER_ID, caster.getId());
			ArcanusHelper.copyMagicColor(this, caster);
		}

		ArcanusComponents.addAggressorbToEntity(target, getUuid());
		setBoundToTarget(true);

		this.targetId = target.getUuid();
		this.dataTracker.set(TARGET_ID, target.getId());
		this.stack = stack;
		this.groupIndex = groupIndex;
		this.potency = potency;
	}
}
