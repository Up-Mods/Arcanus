package dev.cammiescorner.arcanuscontinuum.common.components.entity;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

public class MagicColorComponent implements AutoSyncedComponent {
	private final Entity entity;
	private Color color = Arcanus.DEFAULT_MAGIC_COLOUR;

	public MagicColorComponent(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		color = Color.fromARGB(tag.getInt("MagicColor"));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("MagicColor", color.asIntARGB());
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		ArcanusComponents.MAGIC_COLOR.sync(entity);
	}
}
