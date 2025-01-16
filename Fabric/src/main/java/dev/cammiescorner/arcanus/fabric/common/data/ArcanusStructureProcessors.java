package dev.cammiescorner.arcanus.fabric.common.data;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class ArcanusStructureProcessors {

	public static final ResourceKey<StructureProcessorList> WIZARD_TOWER_PROCESSORS = ResourceKey.create(Registries.PROCESSOR_LIST, FabricMain.id("wizard_tower_processors"));
}
