package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.attack;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElectricSpellEffect extends SpellEffect {
	public ElectricSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, World world, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;
			Entity entity = entityHit.getEntity();

			if(entity instanceof PlayerEntity playerTarget && caster instanceof PlayerEntity playerCaster && !playerCaster.shouldDamagePlayer(playerTarget))
				return;

			if(entity instanceof LivingEntity livingEntity) {
				ArcanusComponents.setStunTimer(livingEntity, ArcanusConfig.AttackEffects.ElectricEffectProperties.baseStunTime * (int) (effects.stream().filter(ArcanusSpellComponents.ELECTRIC::is).count() * potency));

				if(livingEntity instanceof CreeperEntity creeper && !creeper.getDataTracker().get(CreeperEntity.CHARGED))
					creeper.getDataTracker().set(CreeperEntity.CHARGED, true);
			}
		}
	}
}
