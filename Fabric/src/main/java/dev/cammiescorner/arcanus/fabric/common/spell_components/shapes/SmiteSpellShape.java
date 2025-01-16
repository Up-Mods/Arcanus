package dev.cammiescorner.arcanus.fabric.common.spell_components.shapes;

import dev.cammiescorner.arcanus.api.spells.SpellEffect;
import dev.cammiescorner.arcanus.api.spells.SpellGroup;
import dev.cammiescorner.arcanus.api.spells.SpellShape;
import dev.cammiescorner.arcanus.api.spells.Weight;
import dev.cammiescorner.arcanus.fabric.common.entities.magic.SmiteEntity;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusEntities;
import dev.cammiescorner.arcanus.fabric.common.util.ArcanusHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SmiteSpellShape extends SpellShape {
	public SmiteSpellShape(boolean isEnabled, Weight weight, double manaCost, double manaMultiplier, int coolDown, int minLevel, double potencyModifier) {
		super(isEnabled, weight, manaCost, manaMultiplier, coolDown, minLevel, potencyModifier);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		Entity sourceEntity = castSource != null ? castSource : caster;
		potency += getPotencyModifier();

		if(caster != null) {
			List<? extends SmiteEntity> list = world.getEntities(EntityTypeTest.forClass(SmiteEntity.class), entity -> caster.getUUID().equals(entity.getCasterId()));

			for(int i = 0; i < list.size() - 50; i++)
				list.get(i).kill();

			SmiteEntity smite = ArcanusEntities.SMITE.get().create(world);

			if(smite != null) {
				smite.setProperties(caster.getUUID(), sourceEntity, castFrom, stack, effects, potency);
				ArcanusHelper.copyMagicColor(smite, caster);
				world.addFreshEntity(smite);
				castNext(caster, smite.position(), smite, world, stack, spellGroups, groupIndex, potency);
			}
		}
	}
}
