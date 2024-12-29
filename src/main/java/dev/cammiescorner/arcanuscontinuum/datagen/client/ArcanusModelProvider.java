package dev.cammiescorner.arcanuscontinuum.datagen.client;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlocks;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

public class ArcanusModelProvider extends FabricModelProvider {

	private static final ModelTemplate SPAWN_EGG_TEMPLATE = ModelTemplates.createItem("template_spawn_egg");

	public ArcanusModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators gen) {
		gen.createDoor(ArcanusBlocks.MAGIC_DOOR.get());

		gen.createTrivialCube(ArcanusBlocks.MAGIC_BLOCK.get());
		copyModelNoItem(gen, ArcanusBlocks.MAGIC_BLOCK.get(), ArcanusBlocks.SPATIAL_RIFT_WALL.get());
		copyModelNoItem(gen, Blocks.DEEPSLATE_TILES, ArcanusBlocks.SPATIAL_RIFT_EXIT.get());
	}

	@Override
	public void generateItemModels(ItemModelGenerators gen) {
		var compendium = Arcanus.id("compendium_arcanus").withPrefix("item/");
		ModelTemplates.FLAT_ITEM.create(compendium, TextureMapping.layer0(compendium), gen.output);

		gen.generateFlatItem(ArcanusItems.SPELL_BOOK.get(), ModelTemplates.FLAT_ITEM);
		gen.generateFlatItem(ArcanusItems.SCROLL_OF_KNOWLEDGE.get(), ModelTemplates.FLAT_ITEM);

		gen.generateFlatItem(ArcanusItems.WIZARD_SPAWN_EGG.get(), SPAWN_EGG_TEMPLATE);
		gen.generateFlatItem(ArcanusItems.OPOSSUM_SPAWN_EGG.get(), SPAWN_EGG_TEMPLATE);

		gen.generateFlatItem(ArcanusItems.WIZARD_HAT.get(), ModelTemplates.FLAT_ITEM);
		gen.generateFlatItem(ArcanusItems.WIZARD_ROBES.get(), ModelTemplates.FLAT_ITEM);
		gen.generateFlatItem(ArcanusItems.WIZARD_PANTS.get(), ModelTemplates.FLAT_ITEM);
		gen.generateFlatItem(ArcanusItems.WIZARD_BOOTS.get(), ModelTemplates.FLAT_ITEM);

	}

	private void staff(ItemModelGenerators gen, Supplier<? extends Item> staff) {
		var item = staff.get();
		var guiModelLoc = ModelLocationUtils.getModelLocation(item, "_gui");
		var handheldModelLoc = ModelLocationUtils.getModelLocation(item, "_handheld");

		var focusTexture = TextureMapping.getItemTexture(item, "_focus");
		var handleTexture = TextureMapping.getItemTexture(item, "_handle");
		gen.generateLayeredItem(guiModelLoc, focusTexture, handleTexture);
	}

	public final void copyModelNoItem(BlockModelGenerators gen, Block sourceBlock, Block targetBlock) {
		ResourceLocation resourceLocation = ModelLocationUtils.getModelLocation(sourceBlock);
		gen.blockStateOutput.accept(MultiVariantGenerator.multiVariant(targetBlock, Variant.variant().with(VariantProperties.MODEL, resourceLocation)));
	}
}
