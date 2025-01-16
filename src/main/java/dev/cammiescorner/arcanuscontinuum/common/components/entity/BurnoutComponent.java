package dev.cammiescorner.arcanuscontinuum.common.components.entity;

import dev.cammiescorner.arcanuscontinuum.api.entities.ArcanusEntityAttributes;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class BurnoutComponent implements AutoSyncedComponent, ServerTickingComponent {
	public static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("c2223d02-f2f0-4fa9-b9d8-5b2c265a8195");
	public static final UUID MOVE_SPEED_MODIFIER = UUID.fromString("38e12f7a-64d8-4054-b609-039e240eb2a9");
	private final LivingEntity entity;
	private double burnout;

	public BurnoutComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		AttributeInstance burnoutRegenAttr = entity.getAttribute(ArcanusEntityAttributes.BURNOUT_REGEN.get());
		AttributeInstance attackSpeedAttr = entity.getAttribute(Attributes.ATTACK_SPEED);
		AttributeInstance moveSpeedAttr = entity.getAttribute(Attributes.MOVEMENT_SPEED);

		if(burnoutRegenAttr != null && drainBurnout(burnoutRegenAttr.getValue(), true)) {
			drainBurnout(burnoutRegenAttr.getValue() / (entity instanceof Player player && player.isCreative() ? 1 : 30), false);

			if(entity instanceof Player player)
				player.causeFoodExhaustion(0.01F);
		}

		if(attackSpeedAttr != null) {
			if(burnout > 0 && attackSpeedAttr.getModifier(ATTACK_SPEED_MODIFIER) == null)
				attackSpeedAttr.addPermanentModifier(new AttributeModifier(ATTACK_SPEED_MODIFIER, "Burnout modifier", -0.5, AttributeModifier.Operation.MULTIPLY_BASE));
			if(burnout <= 0 && attackSpeedAttr.getModifier(ATTACK_SPEED_MODIFIER) != null)
				attackSpeedAttr.removeModifier(ATTACK_SPEED_MODIFIER);
		}

		if(moveSpeedAttr != null) {
			if(burnout > 0 && moveSpeedAttr.getModifier(MOVE_SPEED_MODIFIER) == null)
				moveSpeedAttr.addPermanentModifier(new AttributeModifier(MOVE_SPEED_MODIFIER, "Burnout modifier", -0.1, AttributeModifier.Operation.MULTIPLY_BASE));
			if(burnout <= 0 && moveSpeedAttr.getModifier(MOVE_SPEED_MODIFIER) != null)
				moveSpeedAttr.removeModifier(MOVE_SPEED_MODIFIER);
		}
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		burnout = tag.getDouble("Burnout");
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putDouble("Burnout", burnout);
	}

	public double getBurnout() {
		return burnout;
	}

	public void setBurnout(double burnout) {
		this.burnout = burnout;

		if(entity instanceof Player)
			ArcanusComponents.BURNOUT_COMPONENT.sync(entity);
	}

	public boolean addBurnout(double amount, boolean simulate) {
		double maxMana = ArcanusComponents.getMaxMana(entity) - ArcanusComponents.getManaLock(entity);

		if(getBurnout() < maxMana) {
			if(!simulate)
				setBurnout(Math.min(maxMana, getBurnout() + amount));

			return true;
		}

		return false;
	}

	public boolean drainBurnout(double amount, boolean simulate) {
		double maxMana = ArcanusComponents.getMaxMana(entity) - ArcanusComponents.getManaLock(entity);

		if(getBurnout() > 0) {
			if(!simulate) {
				if(burnout > maxMana)
					setBurnout(Math.max(0, maxMana - amount));
				else
					setBurnout(Math.max(0, getBurnout() - amount));
			}

			return true;
		}

		return false;
	}
}
