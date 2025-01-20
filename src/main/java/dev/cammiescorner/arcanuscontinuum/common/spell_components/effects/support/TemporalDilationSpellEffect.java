package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.support;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.TemporalDilationField;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusEntities;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
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
		Entity castSource = sourceEntity != null ? sourceEntity : caster;

		if(!level.isClientSide() && castSource != null) {
			TemporalDilationField dilationField = ArcanusEntities.TEMPORAL_DILATION_FIELD.get().create(level);
			double count = effects.stream().filter(ArcanusSpellComponents.TEMPORAL_DILATION::is).count() * potency;

			if(dilationField != null) {
				dilationField.setPos(target.getLocation());
				dilationField.setBoundingBox(dilationField.getBoundingBox().inflate(count - 1)); // TODO not inflating bounding box for some reason?
				dilationField.extendMaxAge(((int) count - 1) * 20);
				ArcanusHelper.copyMagicColor(dilationField, caster);
				level.addFreshEntity(dilationField);
			}
		}
	}
}
