package dev.cammiescorner.arcanuscontinuum.common.entities.magic;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class SmiteEntity extends Entity implements Targetable {
	private final List<UUID> hasHit = new ArrayList<>();
	private UUID casterId = Util.NIL_UUID;
	private ItemStack stack = ItemStack.EMPTY;
	private List<SpellEffect> effects = new ArrayList<>();
	private double potency;

	public SmiteEntity(EntityType<? extends Entity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public void tick() {
		if (!level().isClientSide() && (getCaster() == null || !getCaster().isAlive())) {
			kill();
			return;
		}

		if (!level().isClientSide()) {
			if (tickCount <= 9) {
				AABB box = new AABB(getX() - 4, getY() - 1, getZ() - 4, getX() + 4, (level().getHeight() + 2048) - getY(), getZ() + 4);

				for (SpellEffect effect : new HashSet<>(effects)) {
					if (effect.shouldTriggerOnceOnExplosion()) {
						effect.effect(getCaster(), this, level(), new EntityHitResult(this), effects, stack, potency);
						continue;
					}

					level().getEntitiesOfClass(Entity.class, box, entity -> entity.isAlive() && !entity.isSpectator() && entity instanceof Targetable targetable && targetable.arcanuscontinuum$canBeTargeted()).forEach(entity -> {
						if (!hasHit.contains(entity.getUUID())) {
							effect.effect(getCaster(), this, level(), new EntityHitResult(entity), effects, stack, potency);

							hasHit.add(entity.getUUID());
						}
					});
				}
			}

			if (tickCount > 23) {
				kill();
			}
		} else {
			clientTick();
		}

		super.tick();
	}

	@Environment(EnvType.CLIENT)
	public void clientTick() {
		if (tickCount == 1) {
			level().playLocalSound(getX(), getY(), getZ(), ArcanusSoundEvents.SMITE, SoundSource.NEUTRAL, Mth.clamp(1 - (distanceTo(Minecraft.getInstance().player) / 100F), 0, 1), (1F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F, false);
		}
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
		hasHit.clear();

		casterId = tag.getUUID("CasterId");
		stack = ItemStack.of(tag.getCompound("ItemStack"));
		potency = tag.getDouble("Potency");

		ListTag effectList = tag.getList("Effects", Tag.TAG_STRING);
		ListTag entityList = tag.getList("HasHit", Tag.TAG_INT_ARRAY);

		for (int i = 0; i < effectList.size(); i++) {
			effects.add((SpellEffect) Arcanus.SPELL_COMPONENTS.get(new ResourceLocation(effectList.getString(i))));
		}
		for (Tag nbtElement : entityList) {
			hasHit.add(NbtUtils.loadUUID(nbtElement));
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		ListTag effectList = new ListTag();
		ListTag entityList = new ListTag();

		tag.putUUID("CasterId", casterId);
		tag.put("ItemStack", stack.save(new CompoundTag()));
		tag.putDouble("Potency", potency);

		for (SpellEffect effect : effects) {
			effectList.add(StringTag.valueOf(Arcanus.SPELL_COMPONENTS.getKey(effect).toString()));
		}
		for (UUID uuid1 : hasHit) {
			entityList.add(NbtUtils.createUUID(uuid1));
		}

		tag.put("Effects", effectList);
		tag.put("HasHit", entityList);
	}

	public UUID getCasterId() {
		return casterId;
	}

	private LivingEntity getCaster() {
		if (level() instanceof ServerLevel serverWorld && serverWorld.getEntity(casterId) instanceof LivingEntity caster) {
			return caster;
		}

		return null;
	}

	public void setProperties(UUID casterId, Entity sourceEntity, Vec3 pos, ItemStack stack, List<SpellEffect> effects, double potency) {
		this.setPos(pos);
		this.setNoGravity(true);
		this.setYRot(sourceEntity.getYRot());
		this.setXRot(sourceEntity.getXRot());
		this.casterId = casterId;
		this.stack = stack;
		this.effects = effects;
		this.potency = potency;
	}
}
