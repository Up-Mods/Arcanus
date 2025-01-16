package dev.cammiescorner.arcanuscontinuum.datagen.common;

import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusItemTags;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ArcanusItemTagsProvider extends FabricTagProvider.ItemTagProvider {

	public ArcanusItemTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable BlockTagProvider blockTagProvider) {
		super(output, completableFuture, blockTagProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider arg) {
		getOrCreateTagBuilder(ArcanusItemTags.C_FEATHERS)
			.add(Items.FEATHER);

		getOrCreateTagBuilder(ItemTags.BOOKSHELF_BOOKS)
			.add(ArcanusItems.SPELL_BOOK.get());

		getOrCreateTagBuilder(ItemTags.LECTERN_BOOKS)
			.add(ArcanusItems.SPELL_BOOK.get());

		getOrCreateTagBuilder(ItemTags.TOOLS)
			.addTag(ArcanusItemTags.STAVES);

		getOrCreateTagBuilder(ArcanusItemTags.COPPER_CURSE_IMMUNE)
			.forceAddTag(ItemTags.TOOLS)
			.add(Items.FISHING_ROD)
			.forceAddTag(ConventionalItemTags.BOWS)
			.forceAddTag(ConventionalItemTags.SHIELDS)
			.forceAddTag(ConventionalItemTags.SPEARS)
			.forceAddTag(ConventionalItemTags.SHEARS)
			.forceAddTag(ItemTags.TRIMMABLE_ARMOR)
			.add(Items.ELYTRA)
			.forceAddTag(ItemTags.TRIM_TEMPLATES)
			.add(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
			.add(ArcanusItems.BATTLE_MAGE_UPGRADE_SMITHING_TEMPLATE.get())
			.forceAddTag(ItemTags.DECORATED_POT_SHERDS)
			.addTag(ArcanusItemTags.WIZARD_ARMOR);

		getOrCreateTagBuilder(ArcanusItemTags.CRAFTING_SPELLBINDING_SPELLBOOKS)
			.add(ArcanusItems.SPELL_BOOK.get());

		getOrCreateTagBuilder(ArcanusItemTags.STAVES)
			.add(ArcanusItems.WOODEN_STAFF.get())
			.add(ArcanusItems.CRYSTAL_STAFF.get())
			.add(ArcanusItems.DIVINATION_STAFF.get())
			.add(ArcanusItems.CRESCENT_STAFF.get())
			.add(ArcanusItems.ANCIENT_STAFF.get())
			.add(ArcanusItems.WAND.get())
			.add(ArcanusItems.THAUMATURGES_GAUNTLET.get())
			.add(ArcanusItems.MIND_STAFF.get())
			.add(ArcanusItems.MAGIC_TOME.get())
			.add(ArcanusItems.MAGE_PISTOL.get());

		getOrCreateTagBuilder(ArcanusItemTags.WIZARD_ARMOR)
			.add(ArcanusItems.WIZARD_HAT.get())
			.add(ArcanusItems.WIZARD_ROBES.get())
			.add(ArcanusItems.WIZARD_PANTS.get())
			.add(ArcanusItems.WIZARD_BOOTS.get())
			.add(ArcanusItems.BATTLE_MAGE_HELMET.get())
			.add(ArcanusItems.BATTLE_MAGE_CHESTPLATE.get())
			.add(ArcanusItems.BATTLE_MAGE_LEGGINGS.get())
			.add(ArcanusItems.BATTLE_MAGE_BOOTS.get());
	}
}
