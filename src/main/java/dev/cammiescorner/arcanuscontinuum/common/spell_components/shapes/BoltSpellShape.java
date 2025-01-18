package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class BoltSpellShape extends SpellShape {
	public BoltSpellShape() {
		super(
			ArcanusConfig.SpellShapes.BoltShapeProperties.enabled,
			ArcanusConfig.SpellShapes.BoltShapeProperties.weight,
			ArcanusConfig.SpellShapes.BoltShapeProperties.manaCost,
			ArcanusConfig.SpellShapes.BoltShapeProperties.manaMultiplier,
			ArcanusConfig.SpellShapes.BoltShapeProperties.coolDown,
			ArcanusConfig.SpellShapes.BoltShapeProperties.minimumLevel,
			ArcanusConfig.SpellShapes.BoltShapeProperties.potencyModifier
		);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		potency += getPotencyModifier();
		double range = ArcanusConfig.SpellShapes.BoltShapeProperties.range;
		Entity sourceEntity = castSource != null ? castSource : caster;
		AABB box = new AABB(castFrom.add(-range, -range, -range), castFrom.add(range, range, range));
		List<Entity> affectedEntities = world.getEntities(sourceEntity, box);

		Predicate<Entity> predicate = entity -> {
			if(entity.getBoundingBox().intersects(sourceEntity.getBoundingBox()))
				return true;
			if(sourceEntity instanceof LivingEntity livingEntity && !livingEntity.hasLineOfSight(entity))
				return false;

			Vec3 look = sourceEntity.getLookAngle();
			Optional<Vec3> vecOptional = entity.getBoundingBox().inflate(0.75).clip(castFrom, castFrom.add(look.scale(range)));
			return vecOptional.isPresent();
		};

		Entity entityTarget = getClosestEntity(affectedEntities, range, castFrom, sourceEntity == caster ? predicate : entity -> true);
		Vec3 castAt = castFrom;

		if(entityTarget != null) {
			castAt = entityTarget.position();

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

				castAt = target.getLocation();
			}

			if(target.getType() != HitResult.Type.ENTITY && sourceEntity instanceof LivingEntity livingEntity)
				ArcanusComponents.setBoltPos(livingEntity, target.getLocation());
		}

		if(sourceEntity instanceof LivingEntity livingEntity) {
			ArcanusComponents.setShouldRenderBolt(livingEntity, true);
			ArcanusComponents.setBoltAge(livingEntity, 0);
		}

		castNext(caster, castAt, entityTarget, world, stack, spellGroups, groupIndex, potency);
	}

	@Nullable
	private static Entity getClosestEntity(List<Entity> entityList, double range, Vec3 pos, Predicate<Entity> predicate) {
		double d = -1.0;
		Entity value = null;

		for(Entity entity : entityList) {
			if(predicate.test(entity)) {
				double e = entity.position().distanceTo(pos);

				if(e <= range && (d == -1.0 || e < d)) {
					d = e;
					value = entity;
				}
			}
		}

		return value;
	}
}
