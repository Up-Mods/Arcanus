package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.utility;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PowerSpellEffect extends SpellEffect {
	public PowerSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level world, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.BLOCK) {
			BlockHitResult blockHit = (BlockHitResult) target;
			BlockPos pos = blockHit.getBlockPos();
			BlockPos pos1 = pos.relative(blockHit.getDirection());
			BlockState state = world.getBlockState(pos);
			BlockState state1 = world.getBlockState(pos1);
			int maxPower = (int) (ArcanusConfig.UtilityEffects.PowerEffectProperties.basePower + effects.stream().filter(ArcanusSpellComponents.POWER::is).count() * potency);

			if(state.getProperties().contains(BlockStateProperties.POWER)) {
				world.gameEvent(caster, GameEvent.BLOCK_ACTIVATE, pos);
				world.setBlock(pos, state.setValue(BlockStateProperties.POWER, maxPower), Block.UPDATE_ALL);
				world.updateNeighborsAt(pos, state.getBlock());
				world.scheduleTick(pos, state.getBlock(), 2);
			}
			if(state.getProperties().contains(BlockStateProperties.POWERED)) {
				world.gameEvent(caster, GameEvent.BLOCK_ACTIVATE, pos);
				world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, true), Block.UPDATE_ALL);
				world.updateNeighborsAt(pos, state.getBlock());
				world.scheduleTick(pos, state.getBlock(), 2);
			}
			if(state1.getProperties().contains(BlockStateProperties.POWER)) {
				world.gameEvent(caster, GameEvent.BLOCK_ACTIVATE, pos1);
				world.setBlock(pos1, state1.setValue(BlockStateProperties.POWER, maxPower), Block.UPDATE_ALL);
				world.updateNeighborsAt(pos1, state1.getBlock());
				world.scheduleTick(pos1, state1.getBlock(), 2);
			}
			if(state1.getProperties().contains(BlockStateProperties.POWERED)) {
				world.gameEvent(caster, GameEvent.BLOCK_ACTIVATE, pos1);
				world.setBlock(pos1, state1.setValue(BlockStateProperties.POWERED, true), Block.UPDATE_ALL);
				world.updateNeighborsAt(pos1, state1.getBlock());
				world.scheduleTick(pos1, state1.getBlock(), 2);
			}
		}
	}
}
