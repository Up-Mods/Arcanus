package dev.cammiescorner.arcanuscontinuum.common.data;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class ArcanusDimensions {

	public static final ResourceKey<Level> POCKET_DIMENSION = ResourceKey.create(Registries.DIMENSION, Arcanus.id("pocket_dimension"));
}
