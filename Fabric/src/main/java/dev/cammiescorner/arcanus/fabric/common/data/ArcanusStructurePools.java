package dev.cammiescorner.arcanus.fabric.common.data;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class ArcanusStructurePools {

	public static final ResourceKey<StructureTemplatePool> WIZARD_TOWER = ResourceKey.create(Registries.TEMPLATE_POOL, FabricMain.id("wizard_tower"));
}
