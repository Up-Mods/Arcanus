package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.utility;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpatialRiftSpellEffect extends SpellEffect {
	public SpatialRiftSpellEffect() {
		super(
			ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.enabled,
			SpellType.UTILITY,
			ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.weight,
			ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.manaCost,
			ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.coolDown,
			ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.minimumLevel
		);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(!level.isClientSide() && caster instanceof Player player)
			ArcanusComponents.createPortal(player, (ServerLevel) level, target.getLocation(), effects.stream().filter(ArcanusSpellComponents.SPATIAL_RIFT::is).count() * potency);
	}

	@Override
	public boolean shouldTriggerOnceOnExplosion() {
		return true;
	}
}
