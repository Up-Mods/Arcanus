package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.attack;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IceSpellEffect extends SpellEffect {
	public IceSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, World world, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;
			Entity entity = entityHit.getEntity();

			if(entity instanceof PlayerEntity playerTarget && caster instanceof PlayerEntity playerCaster && !playerCaster.shouldDamagePlayer(playerTarget))
				return;

			if(entity instanceof LivingEntity livingEntity)
				livingEntity.setFrozenTicks(livingEntity.getFrozenTicks() + (int) (ArcanusConfig.AttackEffects.IceEffectProperties.baseFreezingTime * effects.stream().filter(ArcanusSpellComponents.ICE::is).count() * potency));
		}
		else if(target.getType() == HitResult.Type.BLOCK) {
			BlockHitResult blockHit = (BlockHitResult) target;
			BlockPos pos = blockHit.getBlockPos().offset(blockHit.getSide());

			if(world.getBlockState(blockHit.getBlockPos()).getFluidState().isOf(Fluids.WATER))
				world.setBlockState(blockHit.getBlockPos(), Blocks.ICE.getDefaultState());
			else if(world.getBlockState(blockHit.getBlockPos()).getFluidState().isOf(Fluids.LAVA))
				world.setBlockState(blockHit.getBlockPos(), Blocks.OBSIDIAN.getDefaultState());
			else if(world.isTopSolid(pos.down(), caster) && world.canPlace(Blocks.SNOW.getDefaultState(), pos, ShapeContext.absent()) && world.getBlockState(pos).materialReplaceable())
				world.setBlockState(pos, Blocks.SNOW.getDefaultState());
		}
	}
}
