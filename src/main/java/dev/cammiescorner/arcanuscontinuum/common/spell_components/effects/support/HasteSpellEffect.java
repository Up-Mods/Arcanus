package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.support;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HasteSpellEffect extends SpellEffect {
	public HasteSpellEffect() {
		super(
			ArcanusConfig.SupportEffects.HasteEffectProperties.enabled,
			SpellType.SUPPORT,
			ArcanusConfig.SupportEffects.HasteEffectProperties.weight,
			ArcanusConfig.SupportEffects.HasteEffectProperties.manaCost,
			ArcanusConfig.SupportEffects.HasteEffectProperties.coolDown,
			ArcanusConfig.SupportEffects.HasteEffectProperties.minimumLevel
		);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;

			if(entityHit.getEntity() instanceof LivingEntity livingEntity)
				livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, ArcanusConfig.SupportEffects.HasteEffectProperties.baseEffectDuration, (int) ((effects.stream().filter(ArcanusSpellComponents.HASTE::is).count() - 1) * potency), true, false));
		}
	}
}
