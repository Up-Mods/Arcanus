package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.utility;

import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MineSpellEffect extends SpellEffect {
	public MineSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, World world, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.BLOCK) {
			BlockHitResult blockHit = (BlockHitResult) target;
			BlockState state = world.getBlockState(blockHit.getBlockPos());

			if(state.getHardness(world, blockHit.getBlockPos()) > 0) {
				if(!(caster instanceof PlayerEntity player) || world.canPlayerModifyAt(player, blockHit.getBlockPos())) {
					int count = (int) effects.stream().filter(ArcanusSpellComponents.MINE::is).count();

					if(count < 2 && state.isIn(BlockTags.NEEDS_STONE_TOOL))
						return;
					if(count < 3 && state.isIn(BlockTags.NEEDS_IRON_TOOL))
						return;
					if(count < 4 && state.isIn(BlockTags.NEEDS_DIAMOND_TOOL))
						return;

					world.breakBlock(blockHit.getBlockPos(), true, caster);
				}
			}
		}
	}
}
