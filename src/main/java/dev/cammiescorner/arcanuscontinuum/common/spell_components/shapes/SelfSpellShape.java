package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class SelfSpellShape extends SpellShape {
	public SelfSpellShape() {
		super(
			ArcanusConfig.SpellShapes.SelfShapeProperties.enabled,
			ArcanusConfig.SpellShapes.SelfShapeProperties.weight,
			ArcanusConfig.SpellShapes.SelfShapeProperties.manaCost,
			ArcanusConfig.SpellShapes.SelfShapeProperties.manaMultiplier,
			ArcanusConfig.SpellShapes.SelfShapeProperties.coolDown,
			ArcanusConfig.SpellShapes.SelfShapeProperties.minimumLevel,
			ArcanusConfig.SpellShapes.SelfShapeProperties.potencyModifier
		);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		potency += getPotencyModifier();

		HitResult hit = new EntityHitResult(caster);

		for(SpellEffect effect : new HashSet<>(effects))
			effect.effect(caster, caster, world, hit, effects, stack, potency);

		castNext(caster, hit.getLocation(), caster, world, stack, spellGroups, groupIndex, potency);
	}
}
