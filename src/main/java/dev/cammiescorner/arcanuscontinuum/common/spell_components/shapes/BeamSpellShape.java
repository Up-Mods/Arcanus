package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.BeamEntity;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusEntities;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BeamSpellShape extends SpellShape {
	public BeamSpellShape() {
		super(
			ArcanusConfig.SpellShapes.BeamShapeProperties.enabled,
			ArcanusConfig.SpellShapes.BeamShapeProperties.weight,
			ArcanusConfig.SpellShapes.BeamShapeProperties.manaCost,
			ArcanusConfig.SpellShapes.BeamShapeProperties.manaMultiplier,
			ArcanusConfig.SpellShapes.BeamShapeProperties.coolDown,
			ArcanusConfig.SpellShapes.BeamShapeProperties.minimumLevel,
			ArcanusConfig.SpellShapes.BeamShapeProperties.potencyModifier
		);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		double range = ArcanusConfig.SpellShapes.BeamShapeProperties.range;
		potency += getPotencyModifier();
		Entity sourceEntity = castSource != null ? castSource : caster;
		HitResult target = ArcanusHelper.raycast(sourceEntity, range, true, true);

		if(target.getType() != HitResult.Type.MISS) {
			BeamEntity beam = ArcanusEntities.BEAM.get().create(world);

			if(beam != null) {
				beam.setProperties(caster, stack, effects, spellGroups, groupIndex, ArcanusConfig.SpellShapes.BeamShapeProperties.delay, potency, target.getType() == HitResult.Type.ENTITY);

				beam.setPos(target.getLocation());

				if(target.getType() == HitResult.Type.ENTITY)
					beam.startRiding(((EntityHitResult) target).getEntity(), true);
				else
					beam.setPos(Vec3.atCenterOf(((BlockHitResult) target).getBlockPos()));

				world.addFreshEntity(beam);
			}
		}
	}
}
