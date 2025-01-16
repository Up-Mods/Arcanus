package dev.cammiescorner.arcanus.fabric.common.spell_components.shapes;

import dev.cammiescorner.arcanus.ArcanusConfig;
import dev.cammiescorner.arcanus.api.spells.SpellEffect;
import dev.cammiescorner.arcanus.api.spells.SpellGroup;
import dev.cammiescorner.arcanus.api.spells.SpellShape;
import dev.cammiescorner.arcanus.api.spells.Weight;
import dev.cammiescorner.arcanus.fabric.common.entities.magic.AggressorbEntity;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusEntities;
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
	public AggressorbSpellShape(boolean isEnabled, Weight weight, double manaCost, double manaMultiplier, int coolDown, int minLevel, double potencyModifier) {
		super(isEnabled, weight, manaCost, manaMultiplier, coolDown, minLevel, potencyModifier);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		potency += getPotencyModifier();
		Entity sourceEntity = castSource != null ? castSource : caster;

		if(sourceEntity instanceof LivingEntity target) {
			if(ArcanusComponents.aggressorbCount(target) < ArcanusConfig.SpellShapes.AggressorbShapeProperties.maximumAggressorbs) {
				AggressorbEntity aggressorb = ArcanusEntities.AGGRESSORB.get().create(world);

				if(aggressorb != null) {
					aggressorb.setProperties(caster, target, stack, effects, spellGroups, groupIndex, potency);
					aggressorb.setPos(castFrom);
					world.addFreshEntity(aggressorb);
				}
			}
			else if(caster instanceof Player player)
				player.sendSystemMessage(Component.translatable("text.arcanus.too_many_orbs").withStyle(ChatFormatting.RED));
		}
	}
}
