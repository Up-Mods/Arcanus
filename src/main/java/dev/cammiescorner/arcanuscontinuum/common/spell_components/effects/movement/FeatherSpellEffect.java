package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.movement;

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

public class FeatherSpellEffect extends SpellEffect {
	public FeatherSpellEffect() {
		super(
			ArcanusConfig.MovementEffects.FeatherEffectProperties.enabled,
			SpellType.MOVEMENT,
			ArcanusConfig.MovementEffects.FeatherEffectProperties.weight,
			ArcanusConfig.MovementEffects.FeatherEffectProperties.manaCost,
			ArcanusConfig.MovementEffects.FeatherEffectProperties.coolDown,
			ArcanusConfig.MovementEffects.FeatherEffectProperties.minimumLevel
		);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;

			if(entityHit.getEntity() instanceof LivingEntity livingEntity)
				livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, ArcanusConfig.MovementEffects.FeatherEffectProperties.baseEffectDuration * (int) (effects.stream().filter(ArcanusSpellComponents.FEATHER::is).count() * potency), 0, true, false));
		}
	}
}
