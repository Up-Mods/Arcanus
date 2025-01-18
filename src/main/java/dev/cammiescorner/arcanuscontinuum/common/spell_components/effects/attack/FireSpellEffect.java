package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.attack;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FireSpellEffect extends SpellEffect {
	public FireSpellEffect() {
		super(
			ArcanusConfig.AttackEffects.FireEffectProperties.enabled,
			SpellType.ATTACK,
			ArcanusConfig.AttackEffects.FireEffectProperties.weight,
			ArcanusConfig.AttackEffects.FireEffectProperties.manaCost,
			ArcanusConfig.AttackEffects.FireEffectProperties.coolDown,
			ArcanusConfig.AttackEffects.FireEffectProperties.minimumLevel
		);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;
			Entity entity = entityHit.getEntity();

			if(entity instanceof Player playerTarget && caster instanceof Player playerCaster && !playerCaster.canHarmPlayer(playerTarget))
				return;

			if(entity instanceof LivingEntity livingEntity)
				livingEntity.setSecondsOnFire((int) (ArcanusConfig.AttackEffects.FireEffectProperties.baseTimeOnFire * effects.stream().filter(ArcanusSpellComponents.FIRE::is).count() * potency));
		}
		else if(target.getType() == HitResult.Type.BLOCK) {
			BlockHitResult blockHit = (BlockHitResult) target;
			BlockPos pos = blockHit.getBlockPos().relative(blockHit.getDirection());
			BlockState state = Blocks.FIRE.defaultBlockState().setValue(switch(blockHit.getDirection()) {
				case UP, DOWN -> FireBlock.UP;
				case NORTH -> FireBlock.SOUTH;
				case SOUTH -> FireBlock.NORTH;
				case WEST -> FireBlock.EAST;
				case EAST -> FireBlock.WEST;
			}, blockHit.getDirection() != Direction.UP);

			if(level.isUnobstructed(state, pos, CollisionContext.empty()) && level.getBlockState(pos).canBeReplaced())
				level.setBlockAndUpdate(pos, state);
		}
	}
}
