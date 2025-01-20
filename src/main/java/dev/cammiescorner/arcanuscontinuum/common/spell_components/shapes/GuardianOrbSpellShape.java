package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.EntangledOrb;
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
			ArcanusConfig.SpellShapes.EntangledOrbShapeProperties.enabled,
			ArcanusConfig.SpellShapes.EntangledOrbShapeProperties.weight,
			ArcanusConfig.SpellShapes.EntangledOrbShapeProperties.manaCost,
			ArcanusConfig.SpellShapes.EntangledOrbShapeProperties.manaMultiplier,
			ArcanusConfig.SpellShapes.EntangledOrbShapeProperties.coolDown,
			ArcanusConfig.SpellShapes.EntangledOrbShapeProperties.minimumLevel,
			ArcanusConfig.SpellShapes.EntangledOrbShapeProperties.potencyModifier
		);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		LivingEntity targetEntity = castSource instanceof LivingEntity livingEntity ? livingEntity : caster;

		if(targetEntity != null) {
			EntangledOrb orb = ArcanusEntities.ENTANGLED_ORB.get().create(world);
			orb.setProperties(caster, targetEntity, stack, effects, spellGroups, groupIndex, potency + getPotencyModifier());
			orb.setPos(castFrom);
			world.addFreshEntity(orb);

			if(caster != null) {
				List<? extends EntangledOrb> oldOrbs = world.getEntities(ArcanusEntities.ENTANGLED_ORB.get(), existingOrb -> existingOrb != orb && existingOrb.getCaster().getUUID().equals(caster.getUUID()));
				oldOrbs.forEach(Entity::discard);
			}
		}
	}
}
