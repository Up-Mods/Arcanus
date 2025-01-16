package dev.cammiescorner.arcanuscontinuum.common.data;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;

public class ArcanusDimensionTypes {

	public static final ResourceKey<DimensionType> POCKET_DIMENSION = ResourceKey.create(Registries.DIMENSION_TYPE, Arcanus.id("pocket_dimension"));
}
