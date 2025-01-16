package dev.cammiescorner.arcanus.fabric.common.structures;

import com.mojang.serialization.Codec;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusStructureProcessorTypes;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WizardTowerProcessor extends StructureProcessor {
	public static final WizardTowerProcessor INSTANCE = new WizardTowerProcessor();
	public static final Codec<WizardTowerProcessor> CODEC = Codec.unit(() -> WizardTowerProcessor.INSTANCE);

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo localBlockInfo, StructureTemplate.StructureBlockInfo absoluteBlockInfo, StructurePlaceSettings placementData) {
		if(!absoluteBlockInfo.state().is(Blocks.CHISELED_BOOKSHELF))
			return absoluteBlockInfo;

		SimpleContainer inventory = new SimpleContainer(6);
		ResourceLocation lootTableId = Arcanus.id("bookshelves/wizard_tower");
		long lootTableSeed = placementData.getRandom(absoluteBlockInfo.pos()).nextLong();
		BlockState blockState = absoluteBlockInfo.state();

		if(world instanceof ServerLevelAccessor serverWorld) {
			LootTable lootTable = serverWorld.getServer().getLootData().getLootTable(lootTableId);
			LootParams.Builder builder = new LootParams.Builder(serverWorld.getLevel()).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(absoluteBlockInfo.pos()));
			lootTable.fill(inventory, builder.create(LootContextParamSets.CHEST), lootTableSeed);
			ContainerHelper.saveAllItems(absoluteBlockInfo.nbt(), inventory.items, true);

			for(int j = 0; j < ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.size(); ++j) {
				boolean bl = !inventory.getItem(j).isEmpty();
				BooleanProperty booleanProperty = ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(j);
				blockState = blockState.setValue(booleanProperty, bl);
			}
		}

		return new StructureTemplate.StructureBlockInfo(absoluteBlockInfo.pos(), blockState, absoluteBlockInfo.nbt());
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return ArcanusStructureProcessorTypes.WIZARD_TOWER.get();
	}
}
