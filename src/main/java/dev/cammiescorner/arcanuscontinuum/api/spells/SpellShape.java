package dev.cammiescorner.arcanuscontinuum.api.spells;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SpellShape extends SpellComponent {
	public static SpellShape empty() {
		return (SpellShape) ArcanusSpellComponents.EMPTY.get();
	}
	private final double manaMultiplier;
	private final double potencyModifier;

	public SpellShape(boolean isEnabled, Weight weight, double manaCost, double manaMultiplier, int coolDown, int minLevel, double potencyModifier) {
		super(isEnabled, weight, manaCost, coolDown, minLevel);
		this.manaMultiplier = manaMultiplier;
		this.potencyModifier = potencyModifier;
	}

	public double getPotencyModifier() {
		return potencyModifier;
	}

	public double getManaMultiplier() {
		return manaMultiplier - 1;
	}

	public String getPotencyModifierAsString() {
		return (getPotencyModifier() < 0 ? "" : "+") + Arcanus.format(getPotencyModifier() * 100) + "%";
	}

	public String getManaMultiplierAsString() {
		return (getManaMultiplier() < 0 ? "" : "+") + Arcanus.format(getManaMultiplier() * 100) + "%";
	}

	public abstract void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency);

	public static void castNext(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		if(spellGroups.size() <= groupIndex + 1)
			return;

		SpellGroup group = spellGroups.get(groupIndex + 1);
		group.shape().cast(caster, castFrom, castSource, world, stack, group.effects(), spellGroups, groupIndex + 1, potency);
	}
}
