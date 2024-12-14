package dev.cammiescorner.arcanuscontinuum.common.util.supporters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.util.Identifier;

import java.util.UUID;

public record HaloData(Color color, boolean shouldShow) {
	public static final Identifier ID = Arcanus.id("halo");
	private static final HaloData EMPTY = new HaloData(Color.fromInt(0xf2dd50, Color.Ordering.RGB), false);
	public static final Codec<HaloData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Color.CODEC.fieldOf("color").forGetter(HaloData::color),
		Codec.BOOL.fieldOf("shouldShow").forGetter(HaloData::shouldShow)
	).apply(instance, HaloData::new));

	public static HaloData empty() {
		return EMPTY;
	}

	public static HaloData getOrEmpty(UUID uuid) {
		return Arcanus.HALO_DATA.getOrDefault(uuid, HaloData.empty());
	}
}
