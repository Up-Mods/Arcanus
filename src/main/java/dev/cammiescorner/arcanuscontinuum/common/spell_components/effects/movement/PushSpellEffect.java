package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.movement;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PushSpellEffect extends SpellEffect {
	public PushSpellEffect() {
		super(
			ArcanusConfig.MovementEffects.PushEffectProperties.enabled,
			SpellType.MOVEMENT,
			ArcanusConfig.MovementEffects.PushEffectProperties.weight,
			ArcanusConfig.MovementEffects.PushEffectProperties.manaCost,
			ArcanusConfig.MovementEffects.PushEffectProperties.coolDown,
			ArcanusConfig.MovementEffects.PushEffectProperties.minimumLevel
		);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;
			Entity entity = entityHit.getEntity();
			double amount = effects.stream().filter(ArcanusSpellComponents.PUSH::is).count() * ArcanusConfig.MovementEffects.PushEffectProperties.basePushAmount * potency;

			if(sourceEntity != null) {
				if(entity.equals(caster))
					entity.addDeltaMovement(sourceEntity.getLookAngle().scale(amount));
				else
					entity.addDeltaMovement(sourceEntity.position().subtract(entity.position()).normalize().scale(amount));

				entity.hurtMarked = true;
			}
		}
	}
}
