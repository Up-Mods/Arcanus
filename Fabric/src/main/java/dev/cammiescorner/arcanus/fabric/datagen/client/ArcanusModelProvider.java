package dev.cammiescorner.arcanus.fabric.datagen.client;

import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusBlocks;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusItems;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

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
		var compendium = FabricMain.id("compendium_arcanus").withPrefix("item/");
		ModelTemplates.FLAT_ITEM.create(compendium, TextureMapping.layer0(compendium), gen.output);

		gen.generateFlatItem(ArcanusItems.SPELL_BOOK.get(), ModelTemplates.FLAT_ITEM);
		gen.generateFlatItem(ArcanusItems.SCROLL_OF_KNOWLEDGE.get(), ModelTemplates.FLAT_ITEM);

		gen.generateFlatItem(ArcanusItems.WIZARD_SPAWN_EGG.get(), SPAWN_EGG_TEMPLATE);
		gen.generateFlatItem(ArcanusItems.OPOSSUM_SPAWN_EGG.get(), SPAWN_EGG_TEMPLATE);

		var wizardHatModel = ModelLocationUtils.getModelLocation(ArcanusItems.WIZARD_HAT.get());
		var wizardHatTexture = TextureMapping.getItemTexture(ArcanusItems.WIZARD_HAT.get());
		gen.generateLayeredItem(wizardHatModel, wizardHatTexture, wizardHatTexture.withSuffix("_trim"));
		var wizardRobesModel = ModelLocationUtils.getModelLocation(ArcanusItems.WIZARD_ROBES.get());
		var wizardRobesTexture = TextureMapping.getItemTexture(ArcanusItems.WIZARD_ROBES.get());
		gen.generateLayeredItem(wizardRobesModel, wizardRobesTexture, wizardRobesTexture.withSuffix("_trim"));
		gen.generateFlatItem(ArcanusItems.WIZARD_PANTS.get(), ModelTemplates.FLAT_ITEM);
		gen.generateFlatItem(ArcanusItems.WIZARD_BOOTS.get(), ModelTemplates.FLAT_ITEM);
	}

	public final void copyModelNoItem(BlockModelGenerators gen, Block sourceBlock, Block targetBlock) {
		ResourceLocation resourceLocation = ModelLocationUtils.getModelLocation(sourceBlock);
		gen.blockStateOutput.accept(MultiVariantGenerator.multiVariant(targetBlock, Variant.variant().with(VariantProperties.MODEL, resourceLocation)));
	}
}
