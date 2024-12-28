package dev.cammiescorner.arcanuscontinuum.common.data;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class ArcanusStructurePools {

	public static final ResourceKey<StructureTemplatePool> WIZARD_TOWER = ResourceKey.create(Registries.TEMPLATE_POOL, Arcanus.id("wizard_tower"));
}
