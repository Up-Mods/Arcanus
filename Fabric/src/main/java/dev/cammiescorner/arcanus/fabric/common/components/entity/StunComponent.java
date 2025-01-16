package dev.cammiescorner.arcanus.fabric.common.components.entity;

import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class StunComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity entity;
	private int stunTimer;

	public StunComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		if(stunTimer > 0) {
			setStunTimer(getStunTimer() - 1);

			if(entity instanceof Player player)
				player.resetAttackStrengthTicker();
		}
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		stunTimer = tag.getInt("StunTimer");
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putInt("StunTimer", stunTimer);
	}

	public int getStunTimer() {
		return stunTimer;
	}

	public void setStunTimer(int stunTimer) {
		this.stunTimer = stunTimer;
		ArcanusComponents.STUN_COMPONENT.sync(entity);
	}
}
