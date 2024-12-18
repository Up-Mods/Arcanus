package dev.cammiescorner.arcanuscontinuum.common.components.level;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record PocketDimensionPlot(UUID ownerId, BlockPos min, BlockPos max) {

	public static final Codec<PocketDimensionPlot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		UUIDUtil.CODEC.fieldOf("owner_id").forGetter(PocketDimensionPlot::ownerId),
		BlockPos.CODEC.fieldOf("min").forGetter(PocketDimensionPlot::min),
		BlockPos.CODEC.fieldOf("max").forGetter(PocketDimensionPlot::max)
	).apply(instance, PocketDimensionPlot::new));

	public CompoundTag toNbt() {
		return (CompoundTag) CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElseThrow();
	}

	@Nullable
	public static PocketDimensionPlot fromNbt(CompoundTag tag) {
		return CODEC.decode(NbtOps.INSTANCE, tag).resultOrPartial(err -> Arcanus.LOGGER.error("Unable to decode PocketDimensionPlot from NBT: {}", err)).map(Pair::getFirst).orElse(null);
	}

	BoundingBox getBounds() {
		return BoundingBox.fromCorners(min(), max());
	}

	public static PocketDimensionPlot of(UUID ownerId, BoundingBox bounds) {
		var min = new BlockPos(bounds.minX(), bounds.minY(), bounds.minZ());
		var max = new BlockPos(bounds.maxX(), bounds.maxY(), bounds.maxZ());
		return new PocketDimensionPlot(ownerId, min, max);
	}
}
