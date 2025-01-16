package dev.cammiescorner.arcanus.fabric.datagen.common;

import dev.cammiescorner.arcanus.fabric.common.criterion.WizardLevelCriterion;
import dev.cammiescorner.arcanus.fabric.common.data.ArcanusAdvancements;
import dev.cammiescorner.arcanus.fabric.common.data.ArcanusLootTables;
import dev.cammiescorner.arcanus.fabric.common.data.ArcanusStructures;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusBlocks;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ArcanusAdvancementProvider extends FabricAdvancementProvider {

	public ArcanusAdvancementProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateAdvancement(Consumer<Advancement> writer) {
		Advancement.Builder.recipeAdvancement().addCriterion("tick", PlayerTrigger.TriggerInstance.tick()).rewards(AdvancementRewards.Builder.loot(ArcanusLootTables.COMPENDIUM_ARCANUS)).save(writer, ArcanusAdvancements.GRANT_COMPENDIUM_ARCANUS);

		var arcaneRoot = Advancement.Builder.advancement().display(ArcanusItems.CRYSTAL_STAFF.get(), Component.translatable("advancements.arcanus.arcane.root.title"), Component.translatable("advancements.arcanus.arcane.root.description"), new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"), FrameType.TASK, false, false, false).addCriterion("tick", PlayerTrigger.TriggerInstance.tick()).save(writer, ArcanusAdvancements.ARCANE_ROOT);
		var aMagicalCrystal = Advancement.Builder.advancement().parent(arcaneRoot).display(Items.AMETHYST_SHARD, Component.translatable("advancements.arcanus.arcane.a_magical_crystal.title"), Component.translatable("advancements.arcanus.arcane.a_magical_crystal.description"), null, FrameType.GOAL, true, true, false).addCriterion("has_amethyst", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AMETHYST_SHARD)).save(writer, ArcanusAdvancements.A_MAGICAL_CRYSTAL);
		var arcaneArtifice = Advancement.Builder.advancement().parent(aMagicalCrystal).display(ArcanusBlocks.ARCANE_WORKBENCH.get(), Component.translatable("advancements.arcanus.arcane.arcane_artifice.title"), Component.translatable("advancements.arcanus.arcane.arcane_artifice.description"), null, FrameType.GOAL, true, true, false).addCriterion("has_arcane_workbench", InventoryChangeTrigger.TriggerInstance.hasItems(ArcanusBlocks.ARCANE_WORKBENCH.get())).save(writer, ArcanusAdvancements.ARCANE_ARTIFICE);
		var magicalMeetAndGreet = Advancement.Builder.advancement().parent(aMagicalCrystal).display(ArcanusItems.WIZARD_HAT.get(), Component.translatable("advancements.arcanus.arcane.magical_meet_and_greet.title"), Component.translatable("advancements.arcanus.arcane.magical_meet_and_greet.description"), null, FrameType.GOAL, true, true, false).addCriterion("discovered_wizard_tower", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(ArcanusStructures.WIZARD_TOWER))).save(writer, ArcanusAdvancements.MAGICAL_MEET_AND_GREET);

		var wizardLevel1 = Advancement.Builder.advancement().parent(magicalMeetAndGreet).display(ArcanusItems.SCROLL_OF_KNOWLEDGE.get(), Component.translatable("advancements.arcanus.arcane.wizard_level_1.title"), Component.translatable("advancements.arcanus.arcane.wizard_level_1.description"), null, FrameType.TASK, true, true, false).addCriterion("has_level_1", WizardLevelCriterion.TriggerInstance.hasWizardLevel(1)).save(writer, ArcanusAdvancements.WIZARD_LEVEL_1);
		var wizardLevel2 = Advancement.Builder.advancement().parent(wizardLevel1).display(ArcanusItems.SCROLL_OF_KNOWLEDGE.get(), Component.translatable("advancements.arcanus.arcane.wizard_level_2.title"), Component.translatable("advancements.arcanus.arcane.wizard_level_2.description"), null, FrameType.TASK, false, false, false).addCriterion("has_level_2", WizardLevelCriterion.TriggerInstance.hasWizardLevel(2)).save(writer, ArcanusAdvancements.WIZARD_LEVEL_2);
		var wizardLevel3 = Advancement.Builder.advancement().parent(wizardLevel2).display(ArcanusItems.SCROLL_OF_KNOWLEDGE.get(), Component.translatable("advancements.arcanus.arcane.wizard_level_3.title"), Component.translatable("advancements.arcanus.arcane.wizard_level_3.description"), null, FrameType.TASK, false, false, false).addCriterion("has_level_3", WizardLevelCriterion.TriggerInstance.hasWizardLevel(3)).save(writer, ArcanusAdvancements.WIZARD_LEVEL_3);
		var wizardLevel4 = Advancement.Builder.advancement().parent(wizardLevel3).display(ArcanusItems.SCROLL_OF_KNOWLEDGE.get(), Component.translatable("advancements.arcanus.arcane.wizard_level_4.title"), Component.translatable("advancements.arcanus.arcane.wizard_level_4.description"), null, FrameType.TASK, false, false, false).addCriterion("has_level_4", WizardLevelCriterion.TriggerInstance.hasWizardLevel(4)).save(writer, ArcanusAdvancements.WIZARD_LEVEL_4);
		var wizardLevel5 = Advancement.Builder.advancement().parent(wizardLevel4).display(ArcanusItems.SCROLL_OF_KNOWLEDGE.get(), Component.translatable("advancements.arcanus.arcane.wizard_level_5.title"), Component.translatable("advancements.arcanus.arcane.wizard_level_5.description"), null, FrameType.TASK, false, false, false).addCriterion("has_level_5", WizardLevelCriterion.TriggerInstance.hasWizardLevel(5)).save(writer, ArcanusAdvancements.WIZARD_LEVEL_5);
		var wizardLevel6 = Advancement.Builder.advancement().parent(wizardLevel5).display(ArcanusItems.SCROLL_OF_KNOWLEDGE.get(), Component.translatable("advancements.arcanus.arcane.wizard_level_6.title"), Component.translatable("advancements.arcanus.arcane.wizard_level_6.description"), null, FrameType.TASK, false, false, false).addCriterion("has_level_6", WizardLevelCriterion.TriggerInstance.hasWizardLevel(6)).save(writer, ArcanusAdvancements.WIZARD_LEVEL_6);
		var wizardLevel7 = Advancement.Builder.advancement().parent(wizardLevel6).display(ArcanusItems.SCROLL_OF_KNOWLEDGE.get(), Component.translatable("advancements.arcanus.arcane.wizard_level_7.title"), Component.translatable("advancements.arcanus.arcane.wizard_level_7.description"), null, FrameType.TASK, false, false, false).addCriterion("has_level_7", WizardLevelCriterion.TriggerInstance.hasWizardLevel(7)).save(writer, ArcanusAdvancements.WIZARD_LEVEL_7);
		var wizardLevel8 = Advancement.Builder.advancement().parent(wizardLevel7).display(ArcanusItems.SCROLL_OF_KNOWLEDGE.get(), Component.translatable("advancements.arcanus.arcane.wizard_level_8.title"), Component.translatable("advancements.arcanus.arcane.wizard_level_8.description"), null, FrameType.TASK, false, false, false).addCriterion("has_level_8", WizardLevelCriterion.TriggerInstance.hasWizardLevel(8)).save(writer, ArcanusAdvancements.WIZARD_LEVEL_8);
		var wizardLevel9 = Advancement.Builder.advancement().parent(wizardLevel8).display(ArcanusItems.SCROLL_OF_KNOWLEDGE.get(), Component.translatable("advancements.arcanus.arcane.wizard_level_9.title"), Component.translatable("advancements.arcanus.arcane.wizard_level_9.description"), null, FrameType.TASK, false, false, false).addCriterion("has_level_9", WizardLevelCriterion.TriggerInstance.hasWizardLevel(9)).save(writer, ArcanusAdvancements.WIZARD_LEVEL_9);
		var wizardLevel10 = Advancement.Builder.advancement().parent(wizardLevel9).display(ArcanusItems.SCROLL_OF_KNOWLEDGE.get(), Component.translatable("advancements.arcanus.arcane.wizard_level_10.title"), Component.translatable("advancements.arcanus.arcane.wizard_level_10.description"), null, FrameType.TASK, true, true, false).addCriterion("has_level_10", WizardLevelCriterion.TriggerInstance.hasWizardLevel(10)).save(writer, ArcanusAdvancements.WIZARD_LEVEL_10);
	}
}
