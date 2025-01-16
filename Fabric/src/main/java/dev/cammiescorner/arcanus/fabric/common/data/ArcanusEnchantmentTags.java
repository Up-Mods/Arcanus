package dev.cammiescorner.arcanus.fabric.common.data;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class ArcanusEnchantmentTags {
	public static final TagKey<Enchantment> MANA_POOL_COMPATIBLE_WITH = TagKey.create(Registries.ENCHANTMENT, Arcanus.id("mana_pool_compatible_with"));
}
