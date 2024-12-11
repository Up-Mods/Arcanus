package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.BeamEntity;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusEntities;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BeamSpellShape extends SpellShape {
	public BeamSpellShape(boolean isEnabled, Weight weight, double manaCost, double manaMultiplier, int coolDown, int minLevel, double potencyModifier) {
		super(isEnabled, weight, manaCost, manaMultiplier, coolDown, minLevel, potencyModifier);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3d castFrom, @Nullable Entity castSource, ServerWorld world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		double range = ArcanusConfig.SpellShapes.BeamShapeProperties.range;
		potency += getPotencyModifier();
		Entity sourceEntity = castSource != null ? castSource : caster;
		HitResult target = ArcanusHelper.raycast(sourceEntity, range, true, true);

		if(target.getType() != HitResult.Type.MISS) {
			BeamEntity beam = ArcanusEntities.BEAM.get().create(world);

			if(beam != null) {
				beam.setProperties(caster, stack, effects, spellGroups, groupIndex, ArcanusConfig.SpellShapes.BeamShapeProperties.delay, ArcanusHelper.getMagicColor(caster), potency, target.getType() == HitResult.Type.ENTITY);

				beam.setPosition(target.getPos());

				if(target.getType() == HitResult.Type.ENTITY)
					beam.startRiding(((EntityHitResult) target).getEntity(), true);
				else
					beam.setPosition(Vec3d.ofCenter(((BlockHitResult) target).getBlockPos()));

				world.spawnEntity(beam);
			}
		}
	}
}
