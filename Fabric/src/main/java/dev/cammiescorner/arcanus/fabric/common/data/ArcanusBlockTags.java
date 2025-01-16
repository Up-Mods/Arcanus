package dev.cammiescorner.arcanus.fabric.common.data;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ArcanusBlockTags {

	public static final TagKey<Block> WARDING_NOT_ALLOWED = TagKey.create(Registries.BLOCK, FabricMain.id("warding_not_allowed"));
}
