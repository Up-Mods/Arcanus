package dev.cammiescorner.arcanuscontinuum.common.data;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ArcanusEntityTags {

	public static final TagKey<EntityType<?>> DISPELLABLE = TagKey.create(BuiltInRegistries.ENTITY_TYPE.key(), Arcanus.id("dispellable"));
}
