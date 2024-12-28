package dev.cammiescorner.arcanuscontinuum.common.data;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class ArcanusStructureProcessors {

	public static final ResourceKey<StructureProcessorList> WIZARD_TOWER_PROCESSORS = ResourceKey.create(Registries.PROCESSOR_LIST, Arcanus.id("wizard_tower_processors"));
}
