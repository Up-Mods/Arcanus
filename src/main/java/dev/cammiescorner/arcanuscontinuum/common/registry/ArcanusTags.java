package dev.cammiescorner.arcanuscontinuum.common.registry;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class ArcanusTags {
	public static final TagKey<Item> STAVES = TagKey.create(BuiltInRegistries.ITEM.key(), Arcanus.id("staves"));
	public static final TagKey<Item> WIZARD_ARMOUR = TagKey.create(BuiltInRegistries.ITEM.key(), Arcanus.id("wizard_armor"));
	public static final TagKey<Item> COPPER_CURSE_IMMUNE = TagKey.create(BuiltInRegistries.ITEM.key(), Arcanus.id("copper_curse_immune"));

	public static final TagKey<EntityType<?>> DISPELLABLE = TagKey.create(BuiltInRegistries.ENTITY_TYPE.key(), Arcanus.id("dispellable"));
}
