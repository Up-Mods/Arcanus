package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.MagicRuneEntity;
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

public class RuneSpellShape extends SpellShape {
	public RuneSpellShape() {
		super(
			ArcanusConfig.SpellShapes.RuneShapeProperties.enabled,
			ArcanusConfig.SpellShapes.RuneShapeProperties.weight,
			ArcanusConfig.SpellShapes.RuneShapeProperties.manaCost,
			ArcanusConfig.SpellShapes.RuneShapeProperties.manaMultiplier,
			ArcanusConfig.SpellShapes.RuneShapeProperties.coolDown,
			ArcanusConfig.SpellShapes.RuneShapeProperties.minimumLevel,
			ArcanusConfig.SpellShapes.RuneShapeProperties.potencyModifier
		);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		potency += getPotencyModifier();

		if(caster != null) {
			List<? extends MagicRuneEntity> list = world.getEntities(EntityTypeTest.forClass(MagicRuneEntity.class), entity -> caster.getUUID().equals(entity.getCasterId()));

			for(int i = 0; i < list.size() - 100; i++)
				list.get(i).kill();

			MagicRuneEntity magicRune = ArcanusEntities.MAGIC_RUNE.get().create(world);
			Entity sourceEntity = castSource != null ? castSource : caster;

			if(magicRune != null) {
				magicRune.setProperties(caster.getUUID(), sourceEntity, castFrom, stack, effects, potency, spellGroups, groupIndex);
				ArcanusHelper.copyMagicColor(magicRune, caster);
				world.addFreshEntity(magicRune);
			}
		}
	}
}
