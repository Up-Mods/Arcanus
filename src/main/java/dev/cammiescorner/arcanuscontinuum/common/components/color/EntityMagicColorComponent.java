package dev.cammiescorner.arcanuscontinuum.common.components.color;

import com.google.common.base.MoreObjects;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.components.MagicColorComponent;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EntityMagicColorComponent implements MagicColorComponent, AutoSyncedComponent {
	private final Entity entity;
	private UUID sourceId = Util.NIL_UUID;

	public EntityMagicColorComponent(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		sourceId = tag.getUuid(SOURCE_ID_KEY);
		updateStoredColor();
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putUuid(SOURCE_ID_KEY, sourceId);
	}

	public Color getColor() {
		return ArcanusHelper.getMagicColor(sourceId);
	}

	@Override
	public UUID getSourceId() {
		return sourceId;
	}

	@Override
	public void setSourceId(@Nullable UUID ownerId) {
		this.sourceId = MoreObjects.firstNonNull(ownerId, Util.NIL_UUID);
		ArcanusComponents.MAGIC_COLOR.sync(entity);
		updateStoredColor();
	}

	private void updateStoredColor() {
		if (!Util.NIL_UUID.equals(sourceId)) {
			Arcanus.WIZARD_DATA.get(sourceId);
		}
	}
}
