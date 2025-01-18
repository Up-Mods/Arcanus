package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.attack;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WitheringSpellEffect extends SpellEffect {
	public WitheringSpellEffect() {
		super(
			ArcanusConfig.AttackEffects.WitheringEffectProperties.enabled,
			SpellType.ATTACK,
			ArcanusConfig.AttackEffects.WitheringEffectProperties.weight,
			ArcanusConfig.AttackEffects.WitheringEffectProperties.manaCost,
			ArcanusConfig.AttackEffects.WitheringEffectProperties.coolDown,
			ArcanusConfig.AttackEffects.WitheringEffectProperties.minimumLevel
		);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;
			Entity entity = entityHit.getEntity();

			if(entity instanceof Player playerTarget && caster instanceof Player playerCaster && !playerCaster.canHarmPlayer(playerTarget))
				return;

			if(entity instanceof LivingEntity livingEntity)
				livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, ArcanusConfig.AttackEffects.WitheringEffectProperties.baseEffectDuration * (int) (effects.stream().filter(ArcanusSpellComponents.WITHERING::is).count() * potency), 0, true, false));
		}
	}
}
