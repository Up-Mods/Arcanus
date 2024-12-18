package dev.cammiescorner.arcanuscontinuum.common.components.entity;

import dev.cammiescorner.arcanuscontinuum.api.entities.ArcanusEntityAttributes;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

public class ManaComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity entity;
	private double mana;

	public ManaComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		AttributeInstance manaRegenAttr = entity.getAttribute(ArcanusEntityAttributes.MANA_REGEN.get());

		if(manaRegenAttr != null) {
			addMana(manaRegenAttr.getValue() / (entity instanceof Player player && player.isCreative() ? 1 : 20), false);
		}
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		mana = tag.getDouble("Mana");
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putDouble("Mana", mana);
	}

	public double getMana() {
		return mana;
	}

	public void setMana(double mana) {
		this.mana = Mth.clamp(mana, 0, getTrueMaxMana());
		ArcanusComponents.MANA_COMPONENT.sync(entity);
	}

	public double getTrueMaxMana() {
		return (getMaxMana() - getManaLock()) - ArcanusComponents.getBurnout(entity);
	}

	public double getMaxMana() {
		AttributeInstance maxManaAttr = entity.getAttribute(ArcanusEntityAttributes.MAX_MANA.get());

		if (maxManaAttr != null)
			return maxManaAttr.getValue();

		return 0;
	}

	public double getManaLock() {
		AttributeInstance manaLockAttr = entity.getAttribute(ArcanusEntityAttributes.MANA_LOCK.get());

		if (manaLockAttr != null)
			return manaLockAttr.getValue();

		return 0;
	}

	public boolean addMana(double amount, boolean simulate) {
		if (getMana() < getTrueMaxMana()) {
			if (!simulate)
				setMana(getMana() + amount);

			return true;
		}

		return false;
	}

	public boolean drainMana(double amount, boolean simulate) {
		AttributeInstance instance = entity.getAttribute(ArcanusEntityAttributes.MANA_COST.get());
		if (instance != null) {
			amount *= instance.getValue();
		}

		if (getMana() >= 0 && getMana() + getTrueMaxMana() >= amount) {
			if (!simulate) {
				if (amount > getMana()) {
					ArcanusComponents.addBurnout(entity, amount - getMana(), false);

					if (ArcanusComponents.getBurnout(entity) >= ArcanusComponents.getMaxMana(entity) * 0.5F)
						entity.hurt(entity.damageSources().fellOutOfWorld(), (float) Math.min(entity.getHealth() - 1, amount - getMana()));
				}

				setMana(Math.max(0, getMana() - amount));
			}

			return true;
		}

		return false;
	}
}
