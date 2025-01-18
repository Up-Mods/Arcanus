package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GuidedShotSpellShape extends SpellShape {
	public GuidedShotSpellShape() {
		super(
			ArcanusConfig.SpellShapes.GuidedShotShapeProperties.enabled,
			ArcanusConfig.SpellShapes.GuidedShotShapeProperties.weight,
			ArcanusConfig.SpellShapes.GuidedShotShapeProperties.manaCost,
			ArcanusConfig.SpellShapes.GuidedShotShapeProperties.manaMultiplier,
			ArcanusConfig.SpellShapes.GuidedShotShapeProperties.coolDown,
			ArcanusConfig.SpellShapes.GuidedShotShapeProperties.minimumLevel,
			ArcanusConfig.SpellShapes.GuidedShotShapeProperties.potencyModifier
		);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {

	}
}
