package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.Aggressorb;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AggressorbSpellShape extends SpellShape {
	public AggressorbSpellShape() {
		super(
			ArcanusConfig.SpellShapes.AggressorbShapeProperties.enabled,
			ArcanusConfig.SpellShapes.AggressorbShapeProperties.weight,
			ArcanusConfig.SpellShapes.AggressorbShapeProperties.manaCost,
			ArcanusConfig.SpellShapes.AggressorbShapeProperties.manaMultiplier,
			ArcanusConfig.SpellShapes.AggressorbShapeProperties.coolDown,
			ArcanusConfig.SpellShapes.AggressorbShapeProperties.minimumLevel,
			ArcanusConfig.SpellShapes.AggressorbShapeProperties.potencyModifier
		);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		potency += getPotencyModifier();
		Entity sourceEntity = castSource != null ? castSource : caster;

		if(sourceEntity instanceof LivingEntity target) {
			if(ArcanusComponents.aggressorbCount(target) < ArcanusConfig.SpellShapes.AggressorbShapeProperties.maximumAggressorbs) {
				Aggressorb aggressorb = ArcanusEntities.AGGRESSORB.get().create(world);

				if(aggressorb != null) {
					aggressorb.setProperties(caster, target, stack, effects, spellGroups, groupIndex, potency);
					aggressorb.setPos(castFrom);
					world.addFreshEntity(aggressorb);
				}
			}
			else if(caster instanceof Player player)
				player.sendSystemMessage(Component.translatable("text.arcanuscontinuum.too_many_orbs").withStyle(ChatFormatting.RED));
		}
	}
}
