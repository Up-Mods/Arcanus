package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.attack;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusMobEffects;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManaLockSpellEffect extends SpellEffect {
	public ManaLockSpellEffect() {
		super(
			ArcanusConfig.AttackEffects.ManaLockEffectProperties.enabled,
			SpellType.ATTACK,
			ArcanusConfig.AttackEffects.ManaLockEffectProperties.weight,
			ArcanusConfig.AttackEffects.ManaLockEffectProperties.manaCost,
			ArcanusConfig.AttackEffects.ManaLockEffectProperties.coolDown,
			ArcanusConfig.AttackEffects.ManaLockEffectProperties.minimumLevel
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
				livingEntity.addEffect(new MobEffectInstance(ArcanusMobEffects.MANA_LOCK.get(), ArcanusConfig.AttackEffects.ManaLockEffectProperties.baseEffectDuration, (int) ((effects.stream().filter(ArcanusSpellComponents.MANA_LOCK::is).count() - 1) * potency), true, false));
		}
	}
}
