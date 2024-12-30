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
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class AreaOfEffectEntity extends Entity implements Targetable {
	private UUID casterId = Util.NIL_UUID;
	private ItemStack stack = ItemStack.EMPTY;
	private List<SpellEffect> effects = new ArrayList<>();
	private List<SpellGroup> spellGroups = new ArrayList<>();
	private int groupIndex;
	private double potency;
	private int trueAge;
	private boolean isFocused = true;

	public AreaOfEffectEntity(EntityType<?> variant, Level world) {
		super(variant, world);
	}

	@Override
	public void tick() {
		if(!level().isClientSide() && (getCaster() == null || !getCaster().isAlive())) {
			kill();
			return;
		}

		List<AreaOfEffectEntity> list = level().getEntitiesOfClass(AreaOfEffectEntity.class, getBoundingBox(), EntitySelector.ENTITY_STILL_ALIVE);

		if(!list.isEmpty()) {
			int i = level().getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);

			if(i > 0 && list.size() > i - 1) {
				int j = 0;

				for(AreaOfEffectEntity ignored : list)
					++j;

				if(j > i - 1) {
					kill();
					return;
				}
			}
		}

		if(!level().isClientSide()) {
			if(trueAge <= 90 && trueAge > 0) {
				if(trueAge % 30 == 0) {
					AABB box = new AABB(-2, 0, -2, 2, 2.5, 2).move(position());

					for(SpellEffect effect : new HashSet<>(effects)) {
						if(effect.shouldTriggerOnceOnExplosion())
							continue;

						level().getEntitiesOfClass(Entity.class, box, entity -> entity.isAlive() && !entity.isSpectator() && entity instanceof Targetable targetable && targetable.arcanuscontinuum$canBeTargeted()).forEach(entity -> {
							effect.effect(getCaster(), this, level(), new EntityHitResult(entity), effects, stack, potency);
						});
					}

					SpellShape.castNext(getCaster(), position(), this, (ServerLevel) level(), stack, spellGroups, groupIndex, potency);

					if(!isFocused)
						setYRot(getYRot() + 110 + random.nextInt(21));
				}

				if(trueAge % 50 == 0) {
					for(SpellEffect effect : new HashSet<>(effects))
						if(effect.shouldTriggerOnceOnExplosion())
							effect.effect(getCaster(), this, level(), new EntityHitResult(this), effects, stack, potency);
				}
			}

			if(trueAge >= ArcanusConfig.SpellShapes.AOEShapeProperties.baseLifeSpan)
				kill();
		}

		super.tick();
		trueAge++;
	}

	@Override
	protected void defineSynchedData() {

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
		spellGroups.clear();

		casterId = tag.getUUID("CasterId");
		stack = ItemStack.of(tag.getCompound("ItemStack"));
		groupIndex = tag.getInt("GroupIndex");
		potency = tag.getDouble("Potency");
		trueAge = tag.getInt("TrueAge");

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
		tag.putInt("TrueAge", trueAge);

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

	public int getTrueAge() {
		return trueAge;
	}

	public void setProperties(UUID casterId, Entity sourceEntity, Vec3 pos, ItemStack stack, List<SpellEffect> effects, double potency, List<SpellGroup> groups, int groupIndex) {
		setPosRaw(pos.x(), pos.y(), pos.z());
		setYRot(sourceEntity.getYRot());
		setXRot(sourceEntity.getXRot());
		this.casterId = casterId;
		this.isFocused = sourceEntity instanceof AreaOfEffectEntity aoe ? aoe.isFocused : sourceEntity.getUUID().equals(casterId) && sourceEntity.isShiftKeyDown();
		this.stack = stack;
		this.effects = effects;
		this.spellGroups = groups;
		this.groupIndex = groupIndex;
		this.potency = potency;
		this.trueAge = random.nextInt(3);
	}
}
