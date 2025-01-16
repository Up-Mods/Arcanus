package dev.cammiescorner.arcanus.fabric.common.registry;

import dev.cammiescorner.arcanus.fabric.common.structures.WizardTowerProcessor;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ArcanusStructureProcessorTypes {

	public static final RegistryHandler<StructureProcessorType<?>> STRUCTURE_PROCESSORS = RegistryHandler.create(Registries.STRUCTURE_PROCESSOR, FabricMain.MOD_ID);

	public static final RegistrySupplier<StructureProcessorType<WizardTowerProcessor>> WIZARD_TOWER = STRUCTURE_PROCESSORS.register("wizard_tower_processor", () -> () -> WizardTowerProcessor.CODEC);
}
