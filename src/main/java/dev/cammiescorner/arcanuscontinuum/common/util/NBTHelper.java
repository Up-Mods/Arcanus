package dev.cammiescorner.arcanuscontinuum.common.util;

import net.minecraft.nbt.NbtCompound;

public class NBTHelper {

	public static Color readColor(NbtCompound nbt, String key) {
		return Color.fromInt(nbt.getInt(key), Color.Ordering.ARGB);
	}

	public static void writeColor(NbtCompound nbt, Color color, String key) {
		nbt.putInt(key, color.asInt(Color.Ordering.ARGB));
	}
}
