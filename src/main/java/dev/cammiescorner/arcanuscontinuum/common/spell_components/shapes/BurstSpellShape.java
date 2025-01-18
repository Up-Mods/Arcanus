package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import com.google.common.collect.Sets;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.packets.s2c.SyncExplosionParticlesPacket;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BurstSpellShape extends SpellShape {
	public BurstSpellShape() {
		super(
			ArcanusConfig.SpellShapes.BurstShapeProperties.enabled,
			ArcanusConfig.SpellShapes.BurstShapeProperties.weight,
			ArcanusConfig.SpellShapes.BurstShapeProperties.manaCost,
			ArcanusConfig.SpellShapes.BurstShapeProperties.manaMultiplier,
			ArcanusConfig.SpellShapes.BurstShapeProperties.coolDown,
			ArcanusConfig.SpellShapes.BurstShapeProperties.minimumLevel,
			ArcanusConfig.SpellShapes.BurstShapeProperties.potencyModifier
		);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		Entity sourceEntity = castSource != null ? castSource : caster;
		potency += getPotencyModifier();
		float strength = ArcanusConfig.SpellShapes.BurstShapeProperties.strength;

		world.gameEvent(caster, GameEvent.EXPLODE, new Vec3(castFrom.x(), castFrom.y(), castFrom.z()));
		Set<BlockPos> affectedBlocks = Sets.newHashSet();

		for(int j = 0; j < 16; ++j) {
			for(int k = 0; k < 16; ++k) {
				for(int l = 0; l < 16; ++l) {
					if(j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
						double d = j / 15F * 2F - 1F;
						double e = k / 15F * 2F - 1F;
						double f = l / 15F * 2F - 1F;
						double g = Math.sqrt(d * d + e * e + f * f);
						d /= g;
						e /= g;
						f /= g;
						float h = strength * (0.7F + world.random.nextFloat() * 0.6F);
						double m = castFrom.x();
						double n = castFrom.y();
						double o = castFrom.z();

						for(; h > 0F; h -= 0.225F) {
							BlockPos blockPos = new BlockPos((int) m, (int) n, (int) o);
							BlockState blockState = world.getBlockState(blockPos);

							if(!world.isInWorldBounds(blockPos))
								break;

							if(!blockState.isAir() && blockState.getFluidState().isEmpty())
								h -= (blockState.getBlock().getExplosionResistance() + 0.3F) * 0.3F;

							if(!world.isEmptyBlock(blockPos))
								affectedBlocks.add(blockPos);

							m += d * 0.3F;
							n += e * 0.3F;
							o += f * 0.3F;
						}
					}
				}
			}
		}

		float f = strength * 2;
		int k = Mth.floor(castFrom.x() - f - 1);
		int l = Mth.floor(castFrom.x() + f + 1);
		int r = Mth.floor(castFrom.y() - f - 1);
		int s = Mth.floor(castFrom.y() + f + 1);
		int t = Mth.floor(castFrom.z() - f - 1);
		int u = Mth.floor(castFrom.z() + f + 1);
		List<Entity> affectedEntities = world.getEntities(sourceEntity == caster ? caster : null, new AABB(k, r, t, l, s, u), entity -> entity.isAlive() && !entity.isSpectator()).stream().toList();

		for(SpellEffect effect : new HashSet<>(effects)) {
			if(effect.shouldTriggerOnceOnExplosion()) {
				effect.effect(caster, sourceEntity, world, new EntityHitResult(sourceEntity), effects, stack, potency);
				continue;
			}

			for(Entity entity : affectedEntities) {
				effect.effect(caster, sourceEntity, world, new EntityHitResult(entity), effects, stack, potency);
			}
			for(BlockPos blockPos : affectedBlocks) {
				effect.effect(caster, sourceEntity, world, new BlockHitResult(Vec3.atCenterOf(blockPos), Direction.UP, blockPos, true), effects, stack, potency);
			}
		}

		world.playSeededSound(null, castFrom.x(), castFrom.y(), castFrom.z(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4F, (1F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F, 1L);

		for(ServerPlayer player : PlayerLookup.tracking(world, BlockPos.containing(castFrom.x(), castFrom.y(), castFrom.z()))) {
			SyncExplosionParticlesPacket.send(player, castFrom.x(), castFrom.y(), castFrom.z(), strength, effects.contains(ArcanusSpellComponents.MINE.get()));
		}
		castNext(caster, castFrom, castSource, world, stack, spellGroups, groupIndex, potency);
	}
}
