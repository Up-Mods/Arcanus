package dev.cammiescorner.arcanus.fabric.common.data;

import dev.cammiescorner.arcanus.fabric.common.util.ConventionsHelper;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ArcanusBiomeTags {

	public static final TagKey<Biome> C_HAS_VILLAGE = ConventionsHelper.tag(Registries.BIOME, "has_structure/village");
	public static final TagKey<Biome> HAS_WIZARD_TOWER = TagKey.create(Registries.BIOME, Arcanus.id("has_structure/wizard_tower"));
	public static final TagKey<Biome> IS_POCKET_DIMENSION = TagKey.create(Registries.BIOME, Arcanus.id("is_pocket_dimension"));
}
