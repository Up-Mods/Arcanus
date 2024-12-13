package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.attack;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusDamageTypes;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DamageSpellEffect extends SpellEffect {
	public DamageSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, World world, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;
			Entity entity = entityHit.getEntity();
			float damage = ArcanusConfig.AttackEffects.DamageEffectProperties.baseDamage;

			if(entity instanceof PlayerEntity playerTarget && caster instanceof PlayerEntity playerCaster && !playerCaster.shouldDamagePlayer(playerTarget))
				return;

			if(caster != null && entity instanceof Targetable targetable && targetable.arcanuscontinuum$canBeTargeted()) {
				if(entity.isWet() && effects.contains(ArcanusSpellComponents.ELECTRIC.get()))
					damage *= ArcanusConfig.AttackEffects.ElectricEffectProperties.wetEntityDamageMultiplier;

				entity.timeUntilRegen = 0;
				entity.damage(sourceEntity instanceof ProjectileEntity projectile ? ArcanusDamageTypes.getMagicDamage(projectile, caster) : ArcanusDamageTypes.getMagicDamage(caster), (float) (damage * effects.stream().filter(ArcanusSpellComponents.DAMAGE::is).count() * potency));
			}
		}
	}
}
