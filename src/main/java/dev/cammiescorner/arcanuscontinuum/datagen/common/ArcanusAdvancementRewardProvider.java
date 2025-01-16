package dev.cammiescorner.arcanuscontinuum.datagen.common;

import dev.cammiescorner.arcanuscontinuum.common.compat.PatchouliCompat;
import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class ArcanusAdvancementRewardProvider extends SimpleFabricLootTableProvider {

	public ArcanusAdvancementRewardProvider(FabricDataOutput output) {
		super(output, LootContextParamSets.ADVANCEMENT_REWARD);
	}

	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
		ItemStack guideBook = PatchouliCompat.getCompendiumArcanus();
		output.accept(ArcanusLootTables.COMPENDIUM_ARCANUS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(guideBook.getItem()).apply(SetNbtFunction.setTag(guideBook.getOrCreateTag())))));
	}
}
