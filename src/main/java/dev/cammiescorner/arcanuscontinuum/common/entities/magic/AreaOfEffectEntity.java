package dev.cammiescorner.arcanuscontinuum.common.entities.magic;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

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

	public AreaOfEffectEntity(EntityType<?> variant, World world) {
		super(variant, world);
	}

	@Override
	public void tick() {
		if(!getWorld().isClient() && (getCaster() == null || !getCaster().isAlive())) {
			kill();
			return;
		}

		List<AreaOfEffectEntity> list = getWorld().getEntitiesByClass(AreaOfEffectEntity.class, getBoundingBox(), EntityPredicates.VALID_ENTITY);

		if(!list.isEmpty()) {
			int i = getWorld().getGameRules().getInt(GameRules.MAX_ENTITY_CRAMMING);

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

		if(!getWorld().isClient()) {
			if(trueAge <= 90 && trueAge > 0) {
				if(trueAge % 30 == 0) {
					Box box = new Box(-2, 0, -2, 2, 2.5, 2).offset(getPos());

					for(SpellEffect effect : new HashSet<>(effects)) {
						if(effect.shouldTriggerOnceOnExplosion())
							continue;

						getWorld().getEntitiesByClass(Entity.class, box, entity -> entity.isAlive() && !entity.isSpectator() && entity instanceof Targetable targetable && targetable.arcanuscontinuum$canBeTargeted()).forEach(entity -> {
							effect.effect(getCaster(), this, getWorld(), new EntityHitResult(entity), effects, stack, potency);
						});
					}

					SpellShape.castNext(getCaster(), getPos(), this, (ServerWorld) getWorld(), stack, spellGroups, groupIndex, potency);

					if(!isFocused)
						setYaw(getYaw() + 110 + random.nextInt(21));
				}

				if(trueAge % 50 == 0) {
					for(SpellEffect effect : new HashSet<>(effects))
						if(effect.shouldTriggerOnceOnExplosion())
							effect.effect(getCaster(), this, getWorld(), new EntityHitResult(this), effects, stack, potency);
				}
			}

			if(trueAge >= ArcanusConfig.SpellShapes.AOEShapeProperties.baseLifeSpan)
				kill();
		}

		super.tick();
		trueAge++;
	}

	@Override
	protected void initDataTracker() {

	}

	@Override
	public boolean doesRenderOnFire() {
		return false;
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound tag) {
		effects.clear();
		spellGroups.clear();

		casterId = tag.getUuid("CasterId");
		stack = ItemStack.fromNbt(tag.getCompound("ItemStack"));
		groupIndex = tag.getInt("GroupIndex");
		potency = tag.getDouble("Potency");
		trueAge = tag.getInt("TrueAge");

		NbtList effectList = tag.getList("Effects", NbtElement.STRING_TYPE);
		NbtList groupsList = tag.getList("SpellGroups", NbtElement.COMPOUND_TYPE);

		for(int i = 0; i < effectList.size(); i++)
			effects.add((SpellEffect) Arcanus.SPELL_COMPONENTS.get(new Identifier(effectList.getString(i))));
		for(int i = 0; i < groupsList.size(); i++)
			spellGroups.add(SpellGroup.fromNbt(groupsList.getCompound(i)));
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound tag) {
		NbtList effectList = new NbtList();
		NbtList groupsList = new NbtList();

		tag.putUuid("CasterId", casterId);
		tag.put("ItemStack", stack.writeNbt(new NbtCompound()));
		tag.putInt("GroupIndex", groupIndex);
		tag.putDouble("Potency", potency);
		tag.putInt("TrueAge", trueAge);

		for(SpellEffect effect : effects)
			effectList.add(NbtString.of(Arcanus.SPELL_COMPONENTS.getId(effect).toString()));
		for(SpellGroup group : spellGroups)
			groupsList.add(group.toNbt());

		tag.put("Effects", effectList);
		tag.put("SpellGroups", groupsList);
	}

	public UUID getCasterId() {
		return casterId;
	}

	private LivingEntity getCaster() {
		if(getWorld() instanceof ServerWorld serverWorld && serverWorld.getEntity(casterId) instanceof LivingEntity caster)
			return caster;

		return null;
	}

	public Color getColor() {
		return ArcanusComponents.getColor(this);
	}

	public void setColor(Color color) {
		ArcanusComponents.setColor(this, color);
	}

	public int getTrueAge() {
		return trueAge;
	}

	public void setProperties(UUID casterId, Entity sourceEntity, Vec3d pos, ItemStack stack, List<SpellEffect> effects, double potency, List<SpellGroup> groups, int groupIndex, Color color) {
		setPos(pos.getX(), pos.getY(), pos.getZ());
		setYaw(sourceEntity.getYaw());
		setPitch(sourceEntity.getPitch());
		this.casterId = casterId;
		this.isFocused = sourceEntity instanceof AreaOfEffectEntity aoe ? aoe.isFocused : sourceEntity.getUuid().equals(casterId) && sourceEntity.isSneaking();
		this.stack = stack;
		this.effects = effects;
		this.spellGroups = groups;
		this.groupIndex = groupIndex;
		this.potency = potency;
		this.setColor(color);
		this.trueAge = random.nextInt(3);
	}
}
