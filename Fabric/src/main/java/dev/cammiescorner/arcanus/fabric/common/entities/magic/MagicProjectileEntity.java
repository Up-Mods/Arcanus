package dev.cammiescorner.arcanus.fabric.common.entities.magic;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.cammiescorner.arcanus.ArcanusConfig;
import dev.cammiescorner.arcanus.api.entities.Targetable;
import dev.cammiescorner.arcanus.api.spells.SpellEffect;
import dev.cammiescorner.arcanus.api.spells.SpellGroup;
import dev.cammiescorner.arcanus.api.spells.SpellShape;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusSpellComponents;
import dev.cammiescorner.arcanus.fabric.common.util.ArcanusHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MagicProjectileEntity extends AbstractArrow implements Targetable {
	private ItemStack stack = ItemStack.EMPTY;
	private List<SpellEffect> effects = new ArrayList<>();
	private List<SpellGroup> spellGroups = new ArrayList<>();
	private int groupIndex;
	private double potency;

	public MagicProjectileEntity(EntityType<? extends AbstractArrow> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public void tick() {
		int lifeSpan = ArcanusConfig.SpellShapes.ProjectileShapeProperties.baseLifeSpan;

		if(level() instanceof ServerLevel server && (getOwner() == null || !getOwner().isAlive() || (ArcanusSpellComponents.PROJECTILE.is(getShape()) && tickCount >= lifeSpan))) {
			EntityHitResult target = new EntityHitResult(this);

			for(SpellEffect effect : new HashSet<>(effects))
				effect.effect((LivingEntity) getOwner(), this, level(), target, effects, stack, potency);

			if(getOwner() instanceof LivingEntity caster)
				SpellShape.castNext(caster, target.getLocation(), null, server, stack, spellGroups, groupIndex, potency);

			kill();
			return;
		}

		super.tick();
	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	protected void onHitEntity(EntityHitResult target) {
		if(level() instanceof ServerLevel server) {
			for(SpellEffect effect : new HashSet<>(effects))
				effect.effect((LivingEntity) getOwner(), this, level(), target, effects, stack, potency);

			if(getOwner() instanceof LivingEntity caster)
				SpellShape.castNext(caster, target.getLocation(), target.getEntity(), server, stack, spellGroups, groupIndex, potency);
		}

		playSound(getHitGroundSoundEvent(), 1F, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
		kill();
	}

	@Override
	protected void onHitBlock(BlockHitResult target) {
		if(getOwner() instanceof LivingEntity caster && level() instanceof ServerLevel server) {
			for(SpellEffect effect : new HashSet<>(effects))
				effect.effect(caster, this, level(), target, effects, stack, potency);

			SpellShape.castNext(caster, target.getLocation(), this, server, stack, spellGroups, groupIndex, potency);
		}

		super.onHitBlock(target);
		kill();
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		effects.clear();
		spellGroups.clear();

		stack = ItemStack.of(tag.getCompound("ItemStack"));
		potency = tag.getDouble("Potency");
		groupIndex = tag.getInt("GroupIndex");

		ListTag effectList = tag.getList("Effects", Tag.TAG_STRING);
		ListTag groupsList = tag.getList("SpellGroups", Tag.TAG_COMPOUND);

		for(int i = 0; i < effectList.size(); i++)
			effects.add((SpellEffect) FabricMain.SPELL_COMPONENTS.get(new ResourceLocation(effectList.getString(i))));
		for(int i = 0; i < groupsList.size(); i++)
			spellGroups.add(SpellGroup.fromNbt(groupsList.getCompound(i)));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		ListTag effectList = new ListTag();
		ListTag groupsList = new ListTag();

		tag.put("ItemStack", stack.save(new CompoundTag()));
		tag.putDouble("Potency", potency);
		tag.putInt("GroupIndex", groupIndex);

		for(SpellEffect effect : effects)
			effectList.add(StringTag.valueOf(FabricMain.SPELL_COMPONENTS.getKey(effect).toString()));
		for(SpellGroup group : spellGroups)
			groupsList.add(group.toNbt());

		tag.put("Effects", effectList);
		tag.put("SpellGroups", groupsList);
	}

	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return super.getDefaultHitGroundSoundEvent();
	}

	@Override
	protected float getWaterInertia() {
		return 1F;
	}

	@Override
	protected ItemStack getPickupItem() {
		return ItemStack.EMPTY;
	}

	public SpellShape getShape() {
		return ArcanusComponents.getSpellShape(this);
	}

	public void setProperties(Entity caster, @Nullable Entity castSource, SpellShape shape, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> groups, int groupIndex, double potency, float speed, boolean noGravity) {
		Entity sourceEntity = castSource != null ? castSource : caster;
		shootFromRotation(sourceEntity, sourceEntity.getXRot(), sourceEntity.getYRot(), 0F, speed, 1F);
		setOwner(caster);
		setPosRaw(sourceEntity.getX(), sourceEntity.getEyeY(), sourceEntity.getZ());
		setNoGravity(noGravity);
		setBaseDamage(0);
		ArcanusComponents.setSpellShape(this, shape);
		this.stack = stack;
		this.effects = effects;
		this.spellGroups = groups;
		this.groupIndex = groupIndex;
		this.potency = potency;
		ArcanusHelper.copyMagicColor(this, caster);
	}
}
