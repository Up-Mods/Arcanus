package dev.cammiescorner.arcanuscontinuum.common.components.level;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.UuidUtil;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record PocketDimensionPlot(UUID ownerId, BlockPos min, BlockPos max) {

	public static final Codec<PocketDimensionPlot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		UuidUtil.INT_STREAM_CODEC.fieldOf("owner_id").forGetter(PocketDimensionPlot::ownerId),
		BlockPos.CODEC.fieldOf("min").forGetter(PocketDimensionPlot::min),
		BlockPos.CODEC.fieldOf("max").forGetter(PocketDimensionPlot::max)
	).apply(instance, PocketDimensionPlot::new));

	public NbtCompound toNbt() {
		return (NbtCompound) CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElseThrow();
	}

	@Nullable
	public static PocketDimensionPlot fromNbt(NbtCompound tag) {
		return CODEC.decode(NbtOps.INSTANCE, tag).resultOrPartial(err -> Arcanus.LOGGER.error("Unable to decode PocketDimensionPlot from NBT: {}", err)).map(Pair::getFirst).orElse(null);
	}

	BlockBox getBounds() {
		return BlockBox.create(min(), max());
	}

	public static PocketDimensionPlot of(UUID ownerId, BlockBox bounds) {
		var min = new BlockPos(bounds.getMinX(), bounds.getMinY(), bounds.getMinZ());
		var max = new BlockPos(bounds.getMaxX(), bounds.getMaxY(), bounds.getMaxZ());
		return new PocketDimensionPlot(ownerId, min, max);
	}
}
