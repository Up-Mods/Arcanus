package dev.cammiescorner.arcanuscontinuum.common.components.color;

import dev.cammiescorner.arcanuscontinuum.common.components.MagicColorComponent;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class PlayerMagicColorComponent implements MagicColorComponent {

	private final PlayerEntity player;

	public PlayerMagicColorComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public Color getColor() {
		return ArcanusHelper.getMagicColor(player.getGameProfile().getId());
	}

	@Override
	public UUID getSourceId() {
		return player.getGameProfile().getId();
	}

	@Override
	public void setSourceId(UUID ownerId) {
		// NO-OP
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		// NO-OP
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		// NO-OP
	}
}
