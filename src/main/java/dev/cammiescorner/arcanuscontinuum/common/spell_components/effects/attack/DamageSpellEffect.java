package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.attack;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusDamageTypes;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DamageSpellEffect extends SpellEffect {
	public DamageSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level world, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;
			Entity entity = entityHit.getEntity();
			float damage = ArcanusConfig.AttackEffects.DamageEffectProperties.baseDamage;

			if(entity instanceof Player playerTarget && caster instanceof Player playerCaster && !playerCaster.canHarmPlayer(playerTarget))
				return;

			if(caster != null && entity instanceof Targetable targetable && targetable.arcanuscontinuum$canBeTargeted()) {
				if(entity.isInWaterRainOrBubble() && effects.contains(ArcanusSpellComponents.ELECTRIC.get()))
					damage *= ArcanusConfig.AttackEffects.ElectricEffectProperties.wetEntityDamageMultiplier;

				entity.invulnerableTime = 0;
				entity.hurt(sourceEntity instanceof Projectile projectile ? ArcanusDamageTypes.getMagicProjectileDamage(projectile, caster) : ArcanusDamageTypes.getMagicDamage(caster), (float) (damage * effects.stream().filter(ArcanusSpellComponents.DAMAGE::is).count() * potency));
			}
		}
	}
}
