package dev.cammiescorner.arcanus.fabric.common.util.supporters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.arcanus.fabric.common.util.Color;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record HaloData(Color color, boolean shouldShow) {
	public static final ResourceLocation ID = FabricMain.id("halo");
	private static final HaloData EMPTY = new HaloData(Color.fromInt(0xf2dd50, Color.Ordering.RGB), false);
	public static final Codec<HaloData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Color.CODEC.fieldOf("color").forGetter(HaloData::color),
			Codec.BOOL.fieldOf("shouldShow").forGetter(HaloData::shouldShow)
	).apply(instance, HaloData::new));

	public static HaloData empty() {
		return EMPTY;
	}

	public static HaloData getOrEmpty(UUID uuid) {
		return FabricMain.HALO_DATA.getOrDefault(uuid, HaloData.empty());
	}

	public HaloData withEnabled(boolean enabled) {
		return new HaloData(this.color(), enabled);
	}

	public HaloData withColor(Color color) {
		return new HaloData(color, this.shouldShow());
	}
}
