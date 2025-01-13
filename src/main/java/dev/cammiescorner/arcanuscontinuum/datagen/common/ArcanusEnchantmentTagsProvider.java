package dev.cammiescorner.arcanuscontinuum.datagen.common;

import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusEnchantmentTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.concurrent.CompletableFuture;

public class ArcanusEnchantmentTagsProvider extends FabricTagProvider.EnchantmentTagProvider {
	public ArcanusEnchantmentTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider arg) {
		getOrCreateTagBuilder(ArcanusEnchantmentTags.MANA_POOL_COMPATIBLE_WITH)
			.add(Enchantments.UNBREAKING)
			.add(Enchantments.MENDING);
	}
}
