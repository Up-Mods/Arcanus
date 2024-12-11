package dev.cammiescorner.arcanuscontinuum.common.util.supporters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.UUID;

public record HaloData(Optional<Color> color) {

	public static final Identifier ID = Arcanus.id("halo");

	private static final HaloData EMPTY = new HaloData(Optional.empty());

	public static HaloData empty() {
		return EMPTY;
	}

	public static final Codec<HaloData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Color.CODEC.optionalFieldOf("color").forGetter(HaloData::color)
	).apply(instance, HaloData::new));

	public static HaloData getOrEmpty(UUID uuid) {
		return Arcanus.HALO_DATA.getOrDefault(uuid, HaloData.empty());
	}

}
