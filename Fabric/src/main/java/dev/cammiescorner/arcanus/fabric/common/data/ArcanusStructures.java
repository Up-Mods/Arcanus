package dev.cammiescorner.arcanus.fabric.common.data;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ArcanusStructures {

	public static final ResourceKey<Structure> WIZARD_TOWER = ResourceKey.create(Registries.STRUCTURE, FabricMain.id("wizard_tower"));
}
