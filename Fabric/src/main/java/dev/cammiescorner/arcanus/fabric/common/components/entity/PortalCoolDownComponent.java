package dev.cammiescorner.arcanus.fabric.common.components.entity;

import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class PortalCoolDownComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final Player player;
	private int coolDown = 0;

	public PortalCoolDownComponent(Player player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
		coolDown = tag.getInt("CoolDown");
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
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
