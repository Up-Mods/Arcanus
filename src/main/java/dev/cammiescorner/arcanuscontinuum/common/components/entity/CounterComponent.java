package dev.cammiescorner.arcanuscontinuum.common.components.entity;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.cammiescorner.arcanuscontinuum.common.util.NBTHelper;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class CounterComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity entity;
	private final List<SpellEffect> effects = new ArrayList<>();
	private final List<SpellGroup> groups = new ArrayList<>();
	private UUID casterId = Util.NIL_UUID;
	private ItemStack stack = ItemStack.EMPTY;
	private Color color = Arcanus.DEFAULT_MAGIC_COLOUR;
	private int groupIndex = 0;
	private double potency = 1F;
	private long endTime = 0;

	public CounterComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		if(!hasCounterActive(entity.level()) && endTime != 0)
			removeCounter();
	}

	@Override
	public void writeSyncPacket(FriendlyByteBuf buf, ServerPlayer recipient) {
		buf.writeInt(color.asInt(Color.Ordering.ARGB));
		buf.writeLong(endTime);
	}

	@Override
	public void applySyncPacket(FriendlyByteBuf buf) {
		color = Color.fromInt(buf.readInt(), Color.Ordering.ARGB);
		endTime = buf.readLong();
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		effects.clear();
		groups.clear();

		casterId = tag.getUUID("CasterId");
		stack = ItemStack.of(tag.getCompound("ItemStack"));
		color = NBTHelper.readColor(tag, "Color");
		groupIndex = tag.getInt("GroupIndex");
		potency = tag.getDouble("Potency");
		endTime = tag.getLong("EndTime");

		ListTag effectList = tag.getList("Effects", Tag.TAG_STRING);
		ListTag groupsList = tag.getList("SpellGroups", Tag.TAG_COMPOUND);

		for(int i = 0; i < effectList.size(); i++)
			effects.add((SpellEffect) Arcanus.SPELL_COMPONENTS.get(new ResourceLocation(effectList.getString(i))));
		for(int i = 0; i < groupsList.size(); i++)
			groups.add(SpellGroup.fromNbt(groupsList.getCompound(i)));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		ListTag effectList = new ListTag();
		ListTag groupsList = new ListTag();

		tag.putUUID("CasterId", casterId);
		tag.put("ItemStack", stack.save(new CompoundTag()));
		NBTHelper.writeColor(tag, color, "Color");
		tag.putInt("GroupIndex", groupIndex);
		tag.putDouble("Potency", potency);
		tag.putLong("EndTime", endTime);

		for(SpellEffect effect : effects)
			effectList.add(StringTag.valueOf(Arcanus.SPELL_COMPONENTS.getKey(effect).toString()));
		for(SpellGroup group : groups)
			groupsList.add(group.toNbt());

		tag.put("Effects", effectList);
		tag.put("SpellGroups", groupsList);
	}

	public void removeCounter() {
		effects.clear();
		groups.clear();
		casterId = Util.NIL_UUID;
		stack = ItemStack.EMPTY;
		color = Arcanus.DEFAULT_MAGIC_COLOUR;
		groupIndex = 0;
		potency = 1;
		endTime = 0;

		entity.syncComponent(ArcanusComponents.COUNTER_COMPONENT);
	}

	public void setProperties(@Nullable LivingEntity caster, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> groups, int groupIndex, Color color, double potency, long worldTime) {
		this.effects.clear();
		this.groups.clear();
		this.effects.addAll(effects);
		this.groups.addAll(groups);

		if(caster != null)
			this.casterId = caster.getUUID();

		this.stack = stack;
		this.color = color;
		this.groupIndex = groupIndex;
		this.potency = potency;
		this.endTime = worldTime + ArcanusConfig.SpellShapes.CounterShapeProperties.baseEffectDuration;

		entity.syncComponent(ArcanusComponents.COUNTER_COMPONENT);
	}

	public void castCounter(LivingEntity attackingEntity) {
		if(entity.level() instanceof ServerLevel world) {
			EntityHitResult target = new EntityHitResult(attackingEntity);
			LivingEntity caster = world.getEntity(casterId) instanceof LivingEntity livingEntity ? livingEntity : null;

			for(SpellEffect effect : new HashSet<>(effects))
				effect.effect(caster, entity, entity.level(), target, effects, stack, potency);

			SpellShape.castNext(caster, target.getLocation(), entity, world, stack, groups, groupIndex, potency);
		}

		removeCounter();
	}

	public boolean hasCounterActive(Level world) {
		return world.getGameTime() <= endTime;
	}

	public Color getColor() {
		return color;
	}

	public long getEndTime() {
		return endTime;
	}
}
