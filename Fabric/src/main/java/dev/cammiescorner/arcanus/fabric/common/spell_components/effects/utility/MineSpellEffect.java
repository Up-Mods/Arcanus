package dev.cammiescorner.arcanus.fabric.common.spell_components.effects.utility;

import dev.cammiescorner.arcanus.api.spells.SpellEffect;
import dev.cammiescorner.arcanus.api.spells.SpellType;
import dev.cammiescorner.arcanus.api.spells.Weight;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusSpellComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MineSpellEffect extends SpellEffect {
	public MineSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.BLOCK) {
			BlockHitResult blockHit = (BlockHitResult) target;
			BlockState state = level.getBlockState(blockHit.getBlockPos());

			if(state.getDestroySpeed(level, blockHit.getBlockPos()) > 0) {
				if(!(caster instanceof Player player) || level.mayInteract(player, blockHit.getBlockPos())) {
					int count = (int) effects.stream().filter(ArcanusSpellComponents.MINE::is).count();

					if(count < 2 && state.is(BlockTags.NEEDS_STONE_TOOL))
						return;
					if(count < 3 && state.is(BlockTags.NEEDS_IRON_TOOL))
						return;
					if(count < 4 && state.is(BlockTags.NEEDS_DIAMOND_TOOL))
						return;

					level.destroyBlock(blockHit.getBlockPos(), true, caster);
				}
			}
		}
	}
}
