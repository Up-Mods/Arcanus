package dev.cammiescorner.arcanus.fabric.common.components.entity;

import dev.cammiescorner.arcanus.api.entities.ArcanusEntityAttributes;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusCriteriaTriggers;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class WizardLevelComponent implements AutoSyncedComponent {

	public static final int MAX_LEVEL = 10;
	public static final UUID MANA_MODIFIER = UUID.fromString("a64a245e-0f14-494f-8875-7aa8146b3fc1");
	private final LivingEntity entity;
	private int level = 0;

	public WizardLevelComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		level = Mth.clamp(tag.getInt("WizardLevel"), 0, getMaxLevel());
		AttributeInstance manaAttr = entity.getAttribute(ArcanusEntityAttributes.MAX_MANA.get());

		if(manaAttr != null) {
			if(manaAttr.getModifier(MANA_MODIFIER) != null)
				manaAttr.removeModifier(MANA_MODIFIER);

			manaAttr.addPermanentModifier(new AttributeModifier(MANA_MODIFIER, "Wizard Level Modifier", Math.max(level - 1, 0) * 10, AttributeModifier.Operation.ADDITION));
		}

		if(entity instanceof ServerPlayer serverPlayer) {
			ArcanusCriteriaTriggers.WIZARD_LEVEL_CRITERION.trigger(serverPlayer);
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putInt("WizardLevel", level);
	}

	public int getLevel() {
		return level;
	}

	public int getMaxLevel() {
		return MAX_LEVEL;
	}

	public void setLevel(int level) {
		this.level = Mth.clamp(level, 0, getMaxLevel());
		AttributeInstance manaAttr = entity.getAttribute(ArcanusEntityAttributes.MAX_MANA.get());

		if(manaAttr != null) {
			if(manaAttr.getModifier(MANA_MODIFIER) != null)
				manaAttr.removeModifier(MANA_MODIFIER);

			manaAttr.addPermanentModifier(new AttributeModifier(MANA_MODIFIER, "Wizard Level Modifier", Math.max(level - 1, 0) * 10, AttributeModifier.Operation.ADDITION));
		}

		ArcanusComponents.WIZARD_LEVEL_COMPONENT.sync(entity);
		if(entity instanceof ServerPlayer serverPlayer) {
			ArcanusCriteriaTriggers.WIZARD_LEVEL_CRITERION.trigger(serverPlayer);
		}
	}
}
