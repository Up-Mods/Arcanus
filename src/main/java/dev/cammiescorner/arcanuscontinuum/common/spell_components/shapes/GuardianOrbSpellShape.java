package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.GuardianOrbEntity;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusEntities;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GuardianOrbSpellShape extends SpellShape {
	public GuardianOrbSpellShape(boolean isEnabled, Weight weight, double manaCost, double manaMultiplier, int coolDown, int minLevel, double potencyModifier) {
		super(isEnabled, weight, manaCost, manaMultiplier, coolDown, minLevel, potencyModifier);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3d castFrom, @Nullable Entity castSource, ServerWorld world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		LivingEntity targetEntity = castSource instanceof LivingEntity livingEntity ? livingEntity : caster;

		if(targetEntity != null) {
			GuardianOrbEntity orb = ArcanusEntities.GUARDIAN_ORB.get().create(world);
			orb.setProperties(caster, targetEntity, stack, effects, spellGroups, groupIndex, ArcanusHelper.getMagicColor(caster), potency + getPotencyModifier());
			orb.setPosition(castFrom);
			world.spawnEntity(orb);

			if(caster != null) {
				List<? extends GuardianOrbEntity> oldOrbs = world.getEntitiesByType(ArcanusEntities.GUARDIAN_ORB.get(), existingOrb -> existingOrb != orb && existingOrb.getCaster().getUuid().equals(caster.getUuid()));
				oldOrbs.forEach(Entity::discard);
			}
		}
	}
}
