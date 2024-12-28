package dev.cammiescorner.arcanuscontinuum.api.spells;

import dev.cammiescorner.arcanuscontinuum.api.entities.ArcanusEntityAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Spell {
	private final List<SpellGroup> groups;
	private final String name;

	public Spell(List<SpellGroup> groups, String name) {
		this.groups = groups;
		this.name = name;
	}

	public Spell() {
		this(List.of(new SpellGroup(SpellShape.empty(), List.of(), List.of())), "Blank");
	}

	public static Spell fromNbt(CompoundTag nbt) {
		List<SpellGroup> groups = new ArrayList<>();
		ListTag nbtList = nbt.getList("ComponentGroups", Tag.TAG_COMPOUND);

		for(int i = 0; i < nbtList.size(); i++) {
			SpellGroup group = SpellGroup.fromNbt(nbtList.getCompound(i));

			if(group.isEmpty())
				return new Spell();

			groups.add(group);
		}

		return new Spell(groups, nbt.getString("Name"));
	}

	public CompoundTag toNbt() {
		CompoundTag nbt = new CompoundTag();
		ListTag nbtList = new ListTag();

		for(SpellGroup group : groups)
			nbtList.add(group.toNbt());

		nbt.put("ComponentGroups", nbtList);
		nbt.putString("Name", name != null ? name : "Empty");

		return nbt;
	}

	public List<SpellGroup> getComponentGroups() {
		return groups;
	}

	public String getName() {
		return name;
	}

	public boolean isEmpty() {
		return groups.isEmpty() || groups.get(0).isEmpty();
	}

	public Weight getWeight() {
		int averageWeightIndex = 0;

		if(!groups.isEmpty()) {
			int i = 0;

			for(SpellGroup group : groups) {
				if(group.isEmpty())
					continue;

				averageWeightIndex += group.getAverageWeight().ordinal();
				i++;
			}

			averageWeightIndex = Math.round(averageWeightIndex / (float) i);
		}

		return Weight.values()[averageWeightIndex];
	}

	public double getManaCost() {
		double manaCost = 0;

		for(SpellGroup group : groups)
			manaCost += group.getManaCost();

		return manaCost * getManaMultiplier();
	}

	public double getManaMultiplier() {
		double manaMultiplier = 1;

		for(SpellGroup group : groups)
			manaMultiplier += group.shape().getManaMultiplier();

		return manaMultiplier;
	}

	public int getCoolDown() {
		int coolDown = 0;

		if(!groups.isEmpty())
			for(SpellGroup group : groups)
				coolDown += group.getCoolDown();

		return coolDown;
	}

	public Stream<SpellComponent> components() {
		return groups.stream().flatMap(SpellGroup::getAllComponents);
	}

	public void cast(LivingEntity caster, ServerLevel world, ItemStack stack) {
		List<SpellGroup> groups = getComponentGroups();

		if(groups.isEmpty())
			return;

		if(groups.stream().flatMap(SpellGroup::getAllComponents).anyMatch(Predicate.not(SpellComponent::isEnabled))) {
			caster.sendSystemMessage(Component.translatable("text.arcanuscontinuum.disabled_component").withStyle(ChatFormatting.RED));
			return;
		}

		// start casting the spell
		SpellGroup firstGroup = groups.get(0);
		firstGroup.shape().cast(caster, caster.position(), null, world, stack, firstGroup.effects(), groups, 0, caster.getAttributeValue(ArcanusEntityAttributes.SPELL_POTENCY.get()));
	}
}
