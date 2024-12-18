package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.utility;

import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import net.minecraft.core.BlockPos;
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
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level world, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.BLOCK && caster instanceof Player player) {
			BlockHitResult blockHit = (BlockHitResult) target;
			BlockPos pos = blockHit.getBlockPos();

			if(world.mayInteract(player, pos) && !world.getBlockState(pos).getCollisionShape(world, pos).isEmpty()) {
				if(ArcanusComponents.isBlockWarded(world, pos))
					ArcanusComponents.removeWardedBlock(player, pos);
				else
					ArcanusComponents.addWardedBlock(player, pos);
			}
		}
	}
}
