package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.attack;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DiscombobulateSpellEffect extends SpellEffect {
	public DiscombobulateSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, World world, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;
			Entity entity = entityHit.getEntity();

			if(entity instanceof PlayerEntity playerTarget && caster instanceof PlayerEntity playerCaster && !playerCaster.shouldDamagePlayer(playerTarget))
				return;

			if(entity instanceof Targetable targetable && targetable.arcanuscontinuum$canBeTargeted() && entity instanceof LivingEntity livingEntity)
				livingEntity.addStatusEffect(new StatusEffectInstance(ArcanusStatusEffects.DISCOMBOBULATE.get(), ArcanusConfig.AttackEffects.DiscombobulateEffectProperties.baseEffectDuration + (int) (ArcanusConfig.AttackEffects.DiscombobulateEffectProperties.effectDurationModifier * effects.stream().filter(ArcanusSpellComponents.DISCOMBOBULATE::is).count() * potency), 0, true, false));
		}
	}
}
