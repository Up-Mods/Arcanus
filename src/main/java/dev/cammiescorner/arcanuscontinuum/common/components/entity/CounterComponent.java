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
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
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
		if(!hasCounterActive(entity.getWorld()) && endTime != 0)
			removeCounter();
	}

	@Override
	public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
		buf.writeInt(color.asInt(Color.Ordering.ARGB));
		buf.writeLong(endTime);
	}

	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		color = Color.fromInt(buf.readInt(), Color.Ordering.ARGB);
		endTime = buf.readLong();
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		effects.clear();
		groups.clear();

		casterId = tag.getUuid("CasterId");
		stack = ItemStack.fromNbt(tag.getCompound("ItemStack"));
		color = NBTHelper.readColor(tag, "Color");
		groupIndex = tag.getInt("GroupIndex");
		potency = tag.getDouble("Potency");
		endTime = tag.getLong("EndTime");

		NbtList effectList = tag.getList("Effects", NbtElement.STRING_TYPE);
		NbtList groupsList = tag.getList("SpellGroups", NbtElement.COMPOUND_TYPE);

		for(int i = 0; i < effectList.size(); i++)
			effects.add((SpellEffect) Arcanus.SPELL_COMPONENTS.get(new Identifier(effectList.getString(i))));
		for(int i = 0; i < groupsList.size(); i++)
			groups.add(SpellGroup.fromNbt(groupsList.getCompound(i)));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList effectList = new NbtList();
		NbtList groupsList = new NbtList();

		tag.putUuid("CasterId", casterId);
		tag.put("ItemStack", stack.writeNbt(new NbtCompound()));
		NBTHelper.writeColor(tag, color, "Color");
		tag.putInt("GroupIndex", groupIndex);
		tag.putDouble("Potency", potency);
		tag.putLong("EndTime", endTime);

		for(SpellEffect effect : effects)
			effectList.add(NbtString.of(Arcanus.SPELL_COMPONENTS.getId(effect).toString()));
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
			this.casterId = caster.getUuid();

		this.stack = stack;
		this.color = color;
		this.groupIndex = groupIndex;
		this.potency = potency;
		this.endTime = worldTime + ArcanusConfig.SpellShapes.CounterShapeProperties.baseEffectDuration;

		entity.syncComponent(ArcanusComponents.COUNTER_COMPONENT);
	}

	public void castCounter(LivingEntity attackingEntity) {
		if(entity.getWorld() instanceof ServerWorld world) {
			EntityHitResult target = new EntityHitResult(attackingEntity);
			LivingEntity caster = world.getEntity(casterId) instanceof LivingEntity livingEntity ? livingEntity : null;

			for(SpellEffect effect : new HashSet<>(effects))
				effect.effect(caster, entity, entity.getWorld(), target, effects, stack, potency);

			SpellShape.castNext(caster, target.getPos(), entity, world, stack, groups, groupIndex, potency);
		}

		removeCounter();
	}

	public boolean hasCounterActive(World world) {
		return world.getTime() <= endTime;
	}

	public Color getColor() {
		return color;
	}

	public long getEndTime() {
		return endTime;
	}
}
