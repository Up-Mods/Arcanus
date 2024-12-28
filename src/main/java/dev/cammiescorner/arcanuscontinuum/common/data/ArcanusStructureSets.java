package dev.cammiescorner.arcanuscontinuum.common.data;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public class ArcanusStructureSets {

	public static final ResourceKey<StructureSet> WIZARD_TOWER = ResourceKey.create(Registries.STRUCTURE_SET, Arcanus.id("wizard_tower"));
}
