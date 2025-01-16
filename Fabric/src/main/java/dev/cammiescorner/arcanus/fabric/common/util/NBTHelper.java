package dev.cammiescorner.arcanus.fabric.common.util;

import net.minecraft.nbt.CompoundTag;

public class NBTHelper {

	public static Color readColor(CompoundTag nbt, String key) {
		return Color.fromInt(nbt.getInt(key), Color.Ordering.ARGB);
	}

	public static void writeColor(CompoundTag nbt, Color color, String key) {
		nbt.putInt(key, color.asInt(Color.Ordering.ARGB));
	}
}
