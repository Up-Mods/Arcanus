package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.utility;

import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusBlockTags;
import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusDimensionTags;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WardingSpellEffect extends SpellEffect {
	public WardingSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if (target.getType() == HitResult.Type.BLOCK && caster instanceof Player player) {
			var blockHit = (BlockHitResult) target;
			var pos = blockHit.getBlockPos();
			var state = level.getBlockState(pos);

			if (level.mayInteract(player, pos)) {
				var isWarded = ArcanusComponents.isBlockWarded(level, pos);
				if (isWarded || !state.getCollisionShape(level, pos).isEmpty()) {
					if (isWarded) {
						ArcanusComponents.removeWardedBlock(player, pos);
					} else {
						var dimensionTypes = level.registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE);
						var dimensionHolder = dimensionTypes.getOrThrow(level.dimensionTypeId());
						if (dimensionHolder.is(ArcanusDimensionTags.WARDING_NOT_ALLOWED)) {
							// TODO make translatable
							player.sendSystemMessage(Component.literal("Cannot ward blocks in this dimension!"));
						} else if (state.is(ArcanusBlockTags.WARDING_NOT_ALLOWED)) {
							// TODO make translatable
							player.sendSystemMessage(Component.literal("Cannot ward this block!"));
						} else {
							ArcanusComponents.addWardedBlock(player, pos);
						}
					}
				}
			}
		}
	}
}
