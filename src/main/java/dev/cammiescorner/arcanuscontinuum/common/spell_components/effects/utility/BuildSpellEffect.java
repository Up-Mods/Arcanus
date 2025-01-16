package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.utility;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlockEntities;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlocks;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BuildSpellEffect extends SpellEffect {
	public BuildSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.BLOCK) {
			BlockHitResult blockHit = (BlockHitResult) target;
			BlockPos pos = blockHit.getBlockPos().relative(blockHit.getDirection());

			if(level.getBlockState(pos).canBeReplaced() && (!(caster instanceof Player player) || level.mayInteract(player, pos))) {
				level.setBlock(pos, ArcanusBlocks.MAGIC_BLOCK.get().defaultBlockState(), Block.UPDATE_CLIENTS);
				level.scheduleTick(pos, level.getBlockState(pos).getBlock(), (int) (ArcanusConfig.UtilityEffects.BuildEffectProperties.baseLifeSpan * effects.stream().filter(ArcanusSpellComponents.BUILD::is).count() * potency));

				if(caster != null) {
					level.getBlockEntity(pos, ArcanusBlockEntities.MAGIC_BLOCK.get()).ifPresent(blockEntity -> ArcanusHelper.copyMagicColor(blockEntity, caster));
				}
			}
		}
	}
}
