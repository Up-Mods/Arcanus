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

public class CopperCurseSpellEffect extends SpellEffect {
	public CopperCurseSpellEffect() {
		super(
			ArcanusConfig.AttackEffects.CopperCurseEffectProperties.enabled,
			SpellType.ATTACK,
			ArcanusConfig.AttackEffects.CopperCurseEffectProperties.weight,
			ArcanusConfig.AttackEffects.CopperCurseEffectProperties.manaCost,
			ArcanusConfig.AttackEffects.CopperCurseEffectProperties.coolDown,
			ArcanusConfig.AttackEffects.CopperCurseEffectProperties.minimumLevel
		);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult hitResult = (EntityHitResult) target;
			Entity entity = hitResult.getEntity();

			if(entity instanceof Player playerTarget && caster instanceof Player playerCaster && !playerCaster.canHarmPlayer(playerTarget))
				return;

			if(entity instanceof LivingEntity livingEntity)
				livingEntity.addEffect(new MobEffectInstance(ArcanusMobEffects.COPPER_CURSE.get(), ArcanusConfig.AttackEffects.CopperCurseEffectProperties.baseEffectDuration + (ArcanusConfig.AttackEffects.CopperCurseEffectProperties.effectDurationModifier * ((int) ((effects.stream().filter(ArcanusSpellComponents.COPPER_CURSE::is).count() - 1) * potency))), 0, true, false));
		}
	}
}
