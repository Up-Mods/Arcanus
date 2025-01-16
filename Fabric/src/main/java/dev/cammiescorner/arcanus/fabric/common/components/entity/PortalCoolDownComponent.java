package dev.cammiescorner.arcanus.fabric.common.components.entity;

import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class PortalCoolDownComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final Player player;
	private int coolDown = 0;

	public PortalCoolDownComponent(Player player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		coolDown = tag.getInt("CoolDown");
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putInt("CoolDown", coolDown);
	}

	@Override
	public void serverTick() {
		if(coolDown > 0 && --coolDown == 0)
			ArcanusComponents.PORTAL_COOL_DOWN_COMPONENT.sync(player);
	}

	public void setCoolDown(int coolDown) {
		this.coolDown = coolDown;
		ArcanusComponents.PORTAL_COOL_DOWN_COMPONENT.sync(player);
	}

	public boolean hasCoolDown() {
		return coolDown > 0;
	}
}
