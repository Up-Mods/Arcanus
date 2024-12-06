package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.GuardianOrbEntity;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class GuardianOrbSpellShape extends SpellShape {
	public GuardianOrbSpellShape(boolean isEnabled, Weight weight, double manaCost, double manaMultiplier, int coolDown, int minLevel, double potencyModifier) {
		super(isEnabled, weight, manaCost, manaMultiplier, coolDown, minLevel, potencyModifier);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3d castFrom, @Nullable Entity castSource, ServerWorld world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		potency += getPotencyModifier();
		GuardianOrbEntity guardianOrb = ArcanusEntities.GUARDIAN_ORB.get().create(world);
		LivingEntity targetEntity = castSource instanceof LivingEntity livingEntity ? livingEntity : caster;

		if(targetEntity != null && guardianOrb != null) {
			if(caster != null) {
				List<? extends GuardianOrbEntity> oldOrbs = world.getEntitiesByType(ArcanusEntities.GUARDIAN_ORB.get(), t -> Optional.ofNullable(t.getCaster()).map(Entity::getUuid).filter(caster.getUuid()::equals).isPresent());
				oldOrbs.forEach(Entity::discard);
			}

			guardianOrb.setProperties(caster, targetEntity, stack, effects, spellGroups, groupIndex, Arcanus.DEFAULT_MAGIC_COLOUR, potency);
			guardianOrb.setPosition(castFrom);

			if(caster instanceof PlayerEntity player)
				guardianOrb.setColour(Arcanus.getMagicColour(player.getGameProfile().getId()));

			world.spawnEntity(guardianOrb);
		}
	}
}
