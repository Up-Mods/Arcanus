package dev.cammiescorner.arcanus.fabric.common.spell_components.effects.attack;

import dev.cammiescorner.arcanus.ArcanusConfig;
import dev.cammiescorner.arcanus.api.spells.SpellEffect;
import dev.cammiescorner.arcanus.api.spells.SpellType;
import dev.cammiescorner.arcanus.api.spells.Weight;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusSpellComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IceSpellEffect extends SpellEffect {
	public IceSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;
			Entity entity = entityHit.getEntity();

			if(entity instanceof Player playerTarget && caster instanceof Player playerCaster && !playerCaster.canHarmPlayer(playerTarget))
				return;

			if(entity instanceof LivingEntity livingEntity)
				livingEntity.setTicksFrozen(livingEntity.getTicksFrozen() + (int) (ArcanusConfig.AttackEffects.IceEffectProperties.baseFreezingTime * effects.stream().filter(ArcanusSpellComponents.ICE::is).count() * potency));
		}
		else if(target.getType() == HitResult.Type.BLOCK) {
			BlockHitResult blockHit = (BlockHitResult) target;
			BlockPos pos = blockHit.getBlockPos().relative(blockHit.getDirection());

			if(level.getBlockState(pos).getFluidState().is(Fluids.WATER))
				level.setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
			else if(level.getBlockState(pos).getFluidState().is(Fluids.LAVA))
				level.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
			else if(level.loadedAndEntityCanStandOn(pos.below(), caster) && level.isUnobstructed(Blocks.SNOW.defaultBlockState(), pos, CollisionContext.empty()) && level.getBlockState(pos).canBeReplaced())
				level.setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState());
		}
	}
}
