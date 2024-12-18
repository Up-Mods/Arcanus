package dev.cammiescorner.arcanuscontinuum.common.components.entity;

import dev.cammiescorner.arcanuscontinuum.api.entities.ArcanusEntityAttributes;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class WizardLevelComponent implements AutoSyncedComponent {
	public static final UUID MANA_MODIFIER = UUID.fromString("a64a245e-0f14-494f-8875-7aa8146b3fc1");
	private final LivingEntity entity;
	private int level = 0;

	public WizardLevelComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		level = tag.getInt("WizardLevel");
		AttributeInstance manaAttr = entity.getAttribute(ArcanusEntityAttributes.MAX_MANA.get());

		if(manaAttr != null) {
			if(manaAttr.getModifier(MANA_MODIFIER) != null)
				manaAttr.removeModifier(MANA_MODIFIER);

			manaAttr.addPermanentModifier(new AttributeModifier(MANA_MODIFIER, "Wizard Level Modifier", Math.max(level - 1, 0) * 10, AttributeModifier.Operation.ADDITION));
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putInt("WizardLevel", level);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		AttributeInstance manaAttr = entity.getAttribute(ArcanusEntityAttributes.MAX_MANA.get());

		if(manaAttr != null) {
			if(manaAttr.getModifier(MANA_MODIFIER) != null)
				manaAttr.removeModifier(MANA_MODIFIER);

			manaAttr.addPermanentModifier(new AttributeModifier(MANA_MODIFIER, "Wizard Level Modifier", Math.max(level - 1, 0) * 10, AttributeModifier.Operation.ADDITION));
		}

		ArcanusComponents.WIZARD_LEVEL_COMPONENT.sync(entity);
	}
}
