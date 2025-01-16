package dev.cammiescorner.arcanus.api.spells;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SpellEffect extends SpellComponent {
	private final SpellType type;

	public SpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, weight, manaCost, coolDown, minLevel);
		this.type = type;
	}

	public SpellType getType() {
		return type;
	}

	public abstract void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency);

	public boolean shouldTriggerOnceOnExplosion() {
		return false;
	}
}
