package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class BoltSpellShape extends SpellShape {
	public BoltSpellShape(boolean isEnabled, Weight weight, double manaCost, double manaMultiplier, int coolDown, int minLevel, double potencyModifier) {
		super(isEnabled, weight, manaCost, manaMultiplier, coolDown, minLevel, potencyModifier);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3d castFrom, @Nullable Entity castSource, ServerWorld world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		potency += getPotencyModifier();
		double range = ArcanusConfig.SpellShapes.BoltShapeProperties.range;
		Entity sourceEntity = castSource != null ? castSource : caster;
		Box box = new Box(castFrom.add(-range, -range, -range), castFrom.add(range, range, range));
		List<Entity> affectedEntities = world.getOtherEntities(sourceEntity, box);

		Predicate<Entity> predicate = entity -> {
			if(entity.getBoundingBox().intersects(sourceEntity.getBoundingBox()))
				return true;
			if(sourceEntity instanceof LivingEntity livingEntity && !livingEntity.canSee(entity))
				return false;

			Vec3d look = sourceEntity.getRotationVector();
			Optional<Vec3d> vecOptional = entity.getBoundingBox().expand(0.75).raycast(castFrom, castFrom.add(look.multiply(range)));
			return vecOptional.isPresent();
		};

		Entity entityTarget = getClosestEntity(affectedEntities, range, castFrom, sourceEntity == caster ? predicate : entity -> true);
		Vec3d castAt = castFrom;

		if(entityTarget != null) {
			castAt = entityTarget.getPos();

			if(sourceEntity instanceof LivingEntity livingEntity)
				ArcanusComponents.setBoltPos(livingEntity, entityTarget.getBoundingBox().getCenter());

			for(SpellEffect effect : new HashSet<>(effects))
				effect.effect(caster, sourceEntity, world, new EntityHitResult(entityTarget), effects, stack, potency);
		}
		else if(sourceEntity != null) {
			HitResult target = ArcanusHelper.raycast(sourceEntity, range, false, true);

			if(target.getType() == HitResult.Type.BLOCK) {
				for(SpellEffect effect : new HashSet<>(effects))
					effect.effect(caster, sourceEntity, world, target, effects, stack, potency);

				castAt = target.getPos();
			}

			if(target.getType() != HitResult.Type.ENTITY && sourceEntity instanceof LivingEntity livingEntity)
				ArcanusComponents.setBoltPos(livingEntity, target.getPos());
		}

		if(sourceEntity instanceof LivingEntity livingEntity) {
			ArcanusComponents.setShouldRenderBolt(livingEntity, true);
			ArcanusComponents.setBoltAge(livingEntity, 0);
		}

		castNext(caster, castAt, entityTarget, world, stack, spellGroups, groupIndex, potency);
	}

	@Nullable
	private static Entity getClosestEntity(List<Entity> entityList, double range, Vec3d pos, Predicate<Entity> predicate) {
		double d = -1.0;
		Entity value = null;

		for(Entity entity : entityList) {
			if(predicate.test(entity)) {
				double e = entity.getPos().distanceTo(pos);

				if(e <= range && (d == -1.0 || e < d)) {
					d = e;
					value = entity;
				}
			}
		}

		return value;
	}
}
