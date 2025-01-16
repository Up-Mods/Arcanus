package dev.cammiescorner.arcanus.fabric.common.data;

import dev.cammiescorner.arcanus.fabric.common.util.ConventionsHelper;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ArcanusItemTags {

	public static final TagKey<Item> COPPER_CURSE_IMMUNE = TagKey.create(Registries.ITEM, FabricMain.id("copper_curse_immune"));
	public static final TagKey<Item> CRAFTING_SPELLBINDING_SPELLBOOKS = TagKey.create(Registries.ITEM, FabricMain.id("crafting/spellbinding_acceptable_spellbooks"));
	public static final TagKey<Item> STAVES = TagKey.create(Registries.ITEM, FabricMain.id("staves"));
	public static final TagKey<Item> WIZARD_ARMOR = TagKey.create(Registries.ITEM, FabricMain.id("wizard_armor"));

	public static final TagKey<Item> C_FEATHERS = ConventionsHelper.tag(Registries.ITEM, "feathers");
}
