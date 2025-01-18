package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.GuardianOrbEntity;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GuardianOrbSpellShape extends SpellShape {
	public GuardianOrbSpellShape() {
		super(
			ArcanusConfig.SpellShapes.GuardianOrbShapeProperties.enabled,
			ArcanusConfig.SpellShapes.GuardianOrbShapeProperties.weight,
			ArcanusConfig.SpellShapes.GuardianOrbShapeProperties.manaCost,
			ArcanusConfig.SpellShapes.GuardianOrbShapeProperties.manaMultiplier,
			ArcanusConfig.SpellShapes.GuardianOrbShapeProperties.coolDown,
			ArcanusConfig.SpellShapes.GuardianOrbShapeProperties.minimumLevel,
			ArcanusConfig.SpellShapes.GuardianOrbShapeProperties.potencyModifier
		);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		LivingEntity targetEntity = castSource instanceof LivingEntity livingEntity ? livingEntity : caster;

		if(targetEntity != null) {
			GuardianOrbEntity orb = ArcanusEntities.GUARDIAN_ORB.get().create(world);
			orb.setProperties(caster, targetEntity, stack, effects, spellGroups, groupIndex, potency + getPotencyModifier());
			orb.setPos(castFrom);
			world.addFreshEntity(orb);

			if(caster != null) {
				List<? extends GuardianOrbEntity> oldOrbs = world.getEntities(ArcanusEntities.GUARDIAN_ORB.get(), existingOrb -> existingOrb != orb && existingOrb.getCaster().getUUID().equals(caster.getUUID()));
				oldOrbs.forEach(Entity::discard);
			}
		}
	}
}
