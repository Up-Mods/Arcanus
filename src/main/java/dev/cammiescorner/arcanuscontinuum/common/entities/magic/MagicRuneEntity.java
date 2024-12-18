package dev.cammiescorner.arcanuscontinuum.common.entities.magic;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class MagicRuneEntity extends Entity implements Targetable {
	private UUID casterId = Util.NIL_UUID;
	private ItemStack stack = ItemStack.EMPTY;
	private List<SpellEffect> effects = new ArrayList<>();
	private List<SpellGroup> spellGroups = new ArrayList<>();
	private int groupIndex;
	private double potency;

	public MagicRuneEntity(EntityType<?> variant, Level world) {
		super(variant, world);
	}

	@Override
	public void tick() {
		if(!level().isClientSide() && (getCaster() == null || !getCaster().isAlive())) {
			kill();
			return;
		}

		if(level() instanceof ServerLevel serverWorld && tickCount > ArcanusConfig.SpellShapes.RuneShapeProperties.delay) {
			LivingEntity entity = level().getNearestEntity(LivingEntity.class, TargetingConditions.forNonCombat().selector(livingEntity -> livingEntity.isAlive() && !livingEntity.isSpectator() && livingEntity instanceof Targetable targetable && targetable.arcanuscontinuum$canBeTargeted()), null, getX(), getY(), getZ(), new AABB(-0.5, 0, -0.5, 0.5, 0.2, 0.5).move(position()));

			if(entity != null) {
				for(SpellEffect effect : new HashSet<>(effects))
					effect.effect(getCaster(), this, level(), new EntityHitResult(entity), effects, stack, potency);

				SpellShape.castNext(getCaster(), position(), this, serverWorld, stack, spellGroups, groupIndex, potency);
				kill();
			}
		}

		super.tick();
	}

	@Override
	protected void defineSynchedData() {

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
		effects.clear();
		spellGroups.clear();

		casterId = tag.getUUID("CasterId");
		stack = ItemStack.of(tag.getCompound("ItemStack"));
		groupIndex = tag.getInt("GroupIndex");
		potency = tag.getDouble("Potency");

		ListTag effectList = tag.getList("Effects", Tag.TAG_STRING);
		ListTag groupsList = tag.getList("SpellGroups", Tag.TAG_COMPOUND);

		for(int i = 0; i < effectList.size(); i++)
			effects.add((SpellEffect) Arcanus.SPELL_COMPONENTS.get(new ResourceLocation(effectList.getString(i))));
		for(int i = 0; i < groupsList.size(); i++)
			spellGroups.add(SpellGroup.fromNbt(groupsList.getCompound(i)));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		ListTag effectList = new ListTag();
		ListTag groupsList = new ListTag();

		tag.putUUID("CasterId", casterId);
		tag.put("ItemStack", stack.save(new CompoundTag()));
		tag.putInt("GroupIndex", groupIndex);
		tag.putDouble("Potency", potency);

		for(SpellEffect effect : effects)
			effectList.add(StringTag.valueOf(Arcanus.SPELL_COMPONENTS.getKey(effect).toString()));
		for(SpellGroup group : spellGroups)
			groupsList.add(group.toNbt());

		tag.put("Effects", effectList);
		tag.put("SpellGroups", groupsList);
	}

	public UUID getCasterId() {
		return casterId;
	}

	private LivingEntity getCaster() {
		if(level() instanceof ServerLevel serverWorld && serverWorld.getEntity(casterId) instanceof LivingEntity caster)
			return caster;

		return null;
	}

	public void setProperties(UUID casterId, Entity sourceEntity, Vec3 pos, ItemStack stack, List<SpellEffect> effects, double potency, List<SpellGroup> groups, int groupIndex) {
		setPosRaw(pos.x(), pos.y(), pos.z());
		setYRot(sourceEntity.getYRot());
		setXRot(sourceEntity.getXRot());
		this.casterId = casterId;
		this.stack = stack;
		this.effects = effects;
		this.spellGroups = groups;
		this.groupIndex = groupIndex;
		this.potency = potency;
	}
}
