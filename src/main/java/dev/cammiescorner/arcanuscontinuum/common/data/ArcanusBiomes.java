package dev.cammiescorner.arcanuscontinuum.common.data;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public class ArcanusBiomes {

	public static final ResourceKey<Biome> POCKET_DIMENSION = ResourceKey.create(Registries.BIOME, Arcanus.id("pocket_dimension"));
}
