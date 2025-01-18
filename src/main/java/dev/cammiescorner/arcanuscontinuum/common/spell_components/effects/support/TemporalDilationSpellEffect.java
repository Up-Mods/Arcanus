package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.support;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TemporalDilationSpellEffect extends SpellEffect {
	public TemporalDilationSpellEffect() {
		super(
			ArcanusConfig.SupportEffects.TemporalDilationEffectProperties.enabled,
			SpellType.SUPPORT,
			ArcanusConfig.SupportEffects.TemporalDilationEffectProperties.weight,
			ArcanusConfig.SupportEffects.TemporalDilationEffectProperties.manaCost,
			ArcanusConfig.SupportEffects.TemporalDilationEffectProperties.coolDown,
			ArcanusConfig.SupportEffects.TemporalDilationEffectProperties.minimumLevel
		);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {

	}
}
