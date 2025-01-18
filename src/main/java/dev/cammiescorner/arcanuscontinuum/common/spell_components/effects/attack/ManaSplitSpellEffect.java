package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.attack;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManaSplitSpellEffect extends SpellEffect {
	public ManaSplitSpellEffect() {
		super(
			ArcanusConfig.AttackEffects.ManaSplitEffectProperties.enabled,
			SpellType.ATTACK,
			ArcanusConfig.AttackEffects.ManaSplitEffectProperties.weight,
			ArcanusConfig.AttackEffects.ManaSplitEffectProperties.manaCost,
			ArcanusConfig.AttackEffects.ManaSplitEffectProperties.coolDown,
			ArcanusConfig.AttackEffects.ManaSplitEffectProperties.minimumLevel
		);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;
			Entity entity = entityHit.getEntity();

			if(entity instanceof Player playerTarget && caster instanceof Player playerCaster && !playerCaster.canHarmPlayer(playerTarget))
				return;

			if(entity instanceof LivingEntity livingEntity && caster != null) {
				double splitMana = ArcanusComponents.getMana(caster) + ArcanusComponents.getMana(livingEntity);
				double percent = 0.5 + (effects.stream().filter(ArcanusSpellComponents.MANA_SPLIT::is).count() / 11F) * 0.3 * potency;
				double casterMana = splitMana * percent;

				ArcanusComponents.setMana(caster, casterMana);
				ArcanusComponents.setMana(livingEntity, splitMana - casterMana);
			}
		}
	}
}
