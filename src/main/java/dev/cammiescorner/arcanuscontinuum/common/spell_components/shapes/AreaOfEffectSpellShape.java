package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.AreaOfEffectEntity;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusEntities;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AreaOfEffectSpellShape extends SpellShape {
	public AreaOfEffectSpellShape() {
		super(
			ArcanusConfig.SpellShapes.AOEShapeProperties.enabled,
			ArcanusConfig.SpellShapes.AOEShapeProperties.weight,
			ArcanusConfig.SpellShapes.AOEShapeProperties.manaCost,
			ArcanusConfig.SpellShapes.AOEShapeProperties.manaMultiplier,
			ArcanusConfig.SpellShapes.AOEShapeProperties.coolDown,
			ArcanusConfig.SpellShapes.AOEShapeProperties.minimumLevel,
			ArcanusConfig.SpellShapes.AOEShapeProperties.potencyModifier
		);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		potency += getPotencyModifier();

		if(caster != null) {
			List<? extends AreaOfEffectEntity> list = world.getEntities(EntityTypeTest.forClass(AreaOfEffectEntity.class), entity -> caster.getUUID().equals(entity.getCasterId()));

			for(int i = 0; i < list.size() - 20; i++)
				list.get(i).kill();

			AreaOfEffectEntity areaOfEffect = ArcanusEntities.AOE.get().create(world);
			Entity sourceEntity = castSource != null ? castSource : caster;

			if(areaOfEffect != null) {
				areaOfEffect.setProperties(caster.getUUID(), sourceEntity, castFrom, stack, effects, potency, spellGroups, groupIndex);
				ArcanusHelper.copyMagicColor(areaOfEffect, caster);
				world.addFreshEntity(areaOfEffect);
			}
		}
	}
}
