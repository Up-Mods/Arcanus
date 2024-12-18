package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.utility;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpatialRiftSpellEffect extends SpellEffect {
	private static final ResourceKey<Level> POCKET_DIMENSION_WORLD_KEY = ResourceKey.create(Registries.DIMENSION, Arcanus.id("pocket_dimension"));

	public SpatialRiftSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level world, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(!world.isClientSide() && caster instanceof Player player && world.dimension() != POCKET_DIMENSION_WORLD_KEY) {
			Vec3 pos = target.getLocation();
			ArcanusComponents.createPortal(player, (ServerLevel) world, pos, effects.stream().filter(ArcanusSpellComponents.SPATIAL_RIFT::is).count() * potency);
		}
	}

	@Override
	public boolean shouldTriggerOnceOnExplosion() {
		return true;
	}
}
