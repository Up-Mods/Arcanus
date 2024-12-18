package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.support;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HealSpellEffect extends SpellEffect {
	public HealSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level world, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY && target instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity livingEntity)
			livingEntity.heal((float) (ArcanusConfig.SupportEffects.HealEffectProperties.baseHealAmount * effects.stream().filter(ArcanusSpellComponents.HEAL::is).count() * potency));
	}

}
