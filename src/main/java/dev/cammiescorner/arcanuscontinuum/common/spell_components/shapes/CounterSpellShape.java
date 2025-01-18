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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CounterSpellShape extends SpellShape {
	public CounterSpellShape() {
		super(
			ArcanusConfig.SpellShapes.CounterShapeProperties.enabled,
			ArcanusConfig.SpellShapes.CounterShapeProperties.weight,
			ArcanusConfig.SpellShapes.CounterShapeProperties.manaCost,
			ArcanusConfig.SpellShapes.CounterShapeProperties.manaMultiplier,
			ArcanusConfig.SpellShapes.CounterShapeProperties.coolDown,
			ArcanusConfig.SpellShapes.CounterShapeProperties.minimumLevel,
			ArcanusConfig.SpellShapes.CounterShapeProperties.potencyModifier
		);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		potency += getPotencyModifier();
		LivingEntity targetEntity = castSource instanceof LivingEntity livingEntity ? livingEntity : caster;

		if(targetEntity != null) {
			ArcanusComponents.setCounterProperties(targetEntity, caster, stack, effects, spellGroups, groupIndex, ArcanusHelper.getMagicColor(caster), potency, world.getGameTime());
		}
	}
}
