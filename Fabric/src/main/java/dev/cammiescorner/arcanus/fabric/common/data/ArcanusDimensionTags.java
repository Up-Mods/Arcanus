package dev.cammiescorner.arcanus.fabric.common.data;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.dimension.DimensionType;

public class ArcanusDimensionTags {

	public static final TagKey<DimensionType> WARDING_NOT_ALLOWED = TagKey.create(Registries.DIMENSION_TYPE, FabricMain.id("warding_not_allowed"));
}
