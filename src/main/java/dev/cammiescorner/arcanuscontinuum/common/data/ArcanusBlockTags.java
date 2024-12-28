package dev.cammiescorner.arcanuscontinuum.common.data;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ArcanusBlockTags {

	public static final TagKey<Block> WARDING_NOT_ALLOWED = TagKey.create(Registries.BLOCK, Arcanus.id("warding_not_allowed"));
}
