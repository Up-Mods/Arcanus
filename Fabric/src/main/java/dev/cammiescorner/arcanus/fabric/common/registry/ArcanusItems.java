package dev.cammiescorner.arcanus.fabric.common.registry;

import dev.cammiescorner.arcanus.fabric.common.items.*;
import dev.cammiescorner.fabric.common.items.*;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.cammiescorner.arcanus.common.items.*;
import dev.cammiescorner.arcanus.fabric.common.util.Color;
import dev.cammiescorner.arcanus.fabric.common.util.StaffType;
import dev.cammiescorner.arcanus.fabric.common.util.supporters.WizardData;
import dev.upcraft.datasync.api.util.GameProfileHelper;
import dev.upcraft.sparkweave.api.SparkweaveApi;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import java.util.List;
import java.util.UUID;

public class ArcanusItems {

	public static final RegistryHandler<CreativeModeTab> ITEM_GROUPS = RegistryHandler.create(Registries.CREATIVE_MODE_TAB, FabricMain.MOD_ID);
	public static final RegistryHandler<Item> ITEMS = RegistryHandler.create(Registries.ITEM, FabricMain.MOD_ID);

	public static final RegistrySupplier<Item> WOODEN_STAFF = ITEMS.register("wooden_staff", () -> new StaffItem(StaffType.STAFF, Color.fromRGB(255, 255, 255), Color.fromRGB(81, 48, 26)));
	public static final RegistrySupplier<Item> CRYSTAL_STAFF = ITEMS.register("crystal_staff", () -> new StaffItem(StaffType.STAFF, Color.fromRGB(255, 255, 255), Color.fromRGB(81, 48, 26)));
	public static final RegistrySupplier<Item> DIVINATION_STAFF = ITEMS.register("divination_staff", () -> new StaffItem(StaffType.STAFF, Color.fromRGB(255, 255, 255), Color.fromRGB(81, 48, 26)));
	public static final RegistrySupplier<Item> CRESCENT_STAFF = ITEMS.register("crescent_staff", () -> new StaffItem(StaffType.STAFF, Color.fromRGB(255, 255, 255), Color.fromRGB(81, 48, 26)));
	public static final RegistrySupplier<Item> ANCIENT_STAFF = ITEMS.register("ancient_staff", () -> new StaffItem(StaffType.STAFF, Color.fromRGB(255, 255, 255), Color.fromRGB(81, 48, 26)));
	public static final RegistrySupplier<Item> WAND = ITEMS.register("wand", () -> new StaffItem(StaffType.WAND, Color.fromRGB(255, 255, 255), Color.fromRGB(81, 48, 26), true));
	public static final RegistrySupplier<Item> THAUMATURGES_GAUNTLET = ITEMS.register("thaumaturges_gauntlet", () -> new StaffItem(StaffType.GAUNTLET, Color.fromRGB(255, 255, 255), Color.fromRGB(128, 128, 128), true));
	public static final RegistrySupplier<Item> MIND_STAFF = ITEMS.register("mind_staff", () -> new StaffItem(StaffType.STAFF, Color.fromRGB(255, 255, 255), Color.fromRGB(255, 255, 255), true));
	public static final RegistrySupplier<Item> MAGIC_TOME = ITEMS.register("magic_tome", () -> new StaffItem(StaffType.BOOK, Color.fromRGB(139, 69, 19), Color.fromRGB(30, 27, 27), true));
	public static final RegistrySupplier<Item> MAGE_PISTOL = ITEMS.register("mage_pistol", () -> new StaffItem(StaffType.GUN, Color.fromRGB(255, 255, 255), Color.fromRGB(255, 255, 255), true));

	public static final RegistrySupplier<Item> WIZARD_HAT = ITEMS.register("wizard_hat", () -> new WizardRobesArmorItem(ArcanusArmourMaterials.WIZARD, ArmorItem.Type.HELMET, 1, 0.1, 0, 0, -0.06));
	public static final RegistrySupplier<Item> WIZARD_ROBES = ITEMS.register("wizard_robes", () -> new WizardRobesArmorItem(ArcanusArmourMaterials.WIZARD, ArmorItem.Type.CHESTPLATE, 2, 0.2, 0, 0, -0.12));
	public static final RegistrySupplier<Item> WIZARD_PANTS = ITEMS.register("wizard_pants", () -> new WizardRobesArmorItem(ArcanusArmourMaterials.WIZARD, ArmorItem.Type.LEGGINGS, 2, 0.2, 0, 0, -0.1));
	public static final RegistrySupplier<Item> WIZARD_BOOTS = ITEMS.register("wizard_boots", () -> new WizardRobesArmorItem(ArcanusArmourMaterials.WIZARD, ArmorItem.Type.BOOTS, 1, 0.1, 0, 0, -0.05));
	public static final RegistrySupplier<Item> BATTLE_MAGE_HELMET = ITEMS.register("battle_mage_helmet", () -> new BattleMageArmorItem(ArcanusArmourMaterials.BATTLE_MAGE, ArmorItem.Type.HELMET, 0.25, 0, 0.2, 0.06, 0));
	public static final RegistrySupplier<Item> BATTLE_MAGE_CHESTPLATE = ITEMS.register("battle_mage_chestplate", () -> new BattleMageArmorItem(ArcanusArmourMaterials.BATTLE_MAGE, ArmorItem.Type.CHESTPLATE, 0.75, 0, 0.3, 0.12, 0));
	public static final RegistrySupplier<Item> BATTLE_MAGE_LEGGINGS = ITEMS.register("battle_mage_leggings", () -> new BattleMageArmorItem(ArcanusArmourMaterials.BATTLE_MAGE, ArmorItem.Type.LEGGINGS, 0.75, 0, 0.3, 0.1, 0));
	public static final RegistrySupplier<Item> BATTLE_MAGE_BOOTS = ITEMS.register("battle_mage_boots", () -> new BattleMageArmorItem(ArcanusArmourMaterials.BATTLE_MAGE, ArmorItem.Type.BOOTS, 0.25, 0, 0.2, 0.05, 0));

	public static final RegistrySupplier<Item> BATTLE_MAGE_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("battle_mage_upgrade_smithing_template", ArcanusItems::getBattleMageUpgrade);
	public static final RegistrySupplier<Item> SPELL_BOOK = ITEMS.register("spell_book", SpellBookItem::new);
	public static final RegistrySupplier<Item> SCROLL_OF_KNOWLEDGE = ITEMS.register("scroll_of_knowledge", ScrollOfKnowledgeItem::new);
	public static final RegistrySupplier<Item> WIZARD_SPAWN_EGG = ITEMS.register("wizard_spawn_egg", () -> new SpawnEggItem(ArcanusEntities.WIZARD.get(), 0x52392a, 0xffd87c, new Item.Properties()));
	public static final RegistrySupplier<Item> OPOSSUM_SPAWN_EGG = ITEMS.register("opossum_spawn_egg", () -> new SpawnEggItem(ArcanusEntities.OPOSSUM.get(), 0x131317, 0xbdbdbd, new Item.Properties()));

	public static final RegistrySupplier<CreativeModeTab> ITEM_GROUP = ITEM_GROUPS.register("general", () -> FabricItemGroup.builder().title(Component.translatable("itemGroup.arcanus.general")).icon(() -> new ItemStack(ArcanusItems.CRYSTAL_STAFF.get())).displayItems((params, entries) -> {
		entries.accept(SPELL_BOOK.get());
		entries.accept(SCROLL_OF_KNOWLEDGE.get());

		if(SparkweaveApi.CLIENTSIDE_ENVIRONMENT) {
			var currentPlayerId = GameProfileHelper.getClientProfile().getId();
			entries.accept(StaffItem.setCraftedBy(new ItemStack(WOODEN_STAFF.get()), currentPlayerId));
			entries.accept(StaffItem.setCraftedBy(new ItemStack(CRYSTAL_STAFF.get()), currentPlayerId));
			entries.accept(StaffItem.setCraftedBy(new ItemStack(DIVINATION_STAFF.get()), currentPlayerId));
			entries.accept(StaffItem.setCraftedBy(new ItemStack(CRESCENT_STAFF.get()), currentPlayerId));
			entries.accept(StaffItem.setCraftedBy(new ItemStack(ANCIENT_STAFF.get()), currentPlayerId));

			if(WizardData.isSupporter(currentPlayerId)) {
				entries.accept(StaffItem.setCraftedBy(new ItemStack(WAND.get()), currentPlayerId));
				entries.accept(StaffItem.setCraftedBy(new ItemStack(THAUMATURGES_GAUNTLET.get()), currentPlayerId));
				entries.accept(StaffItem.setCraftedBy(new ItemStack(MIND_STAFF.get()), currentPlayerId));
				entries.accept(StaffItem.setCraftedBy(new ItemStack(MAGIC_TOME.get()), currentPlayerId));
				entries.accept(StaffItem.setCraftedBy(new ItemStack(MAGE_PISTOL.get()), currentPlayerId));
			}
		}
		else {
			var dummyId = UUID.fromString("6147825f-5493-4154-87c5-5c03c6b0a7c2");
			entries.accept(StaffItem.setCraftedBy(new ItemStack(WOODEN_STAFF.get()), dummyId));
			entries.accept(StaffItem.setCraftedBy(new ItemStack(CRYSTAL_STAFF.get()), dummyId));
			entries.accept(StaffItem.setCraftedBy(new ItemStack(DIVINATION_STAFF.get()), dummyId));
			entries.accept(StaffItem.setCraftedBy(new ItemStack(CRESCENT_STAFF.get()), dummyId));
			entries.accept(StaffItem.setCraftedBy(new ItemStack(ANCIENT_STAFF.get()), dummyId));
		}

		entries.accept(WIZARD_HAT.get());
		entries.accept(WIZARD_ROBES.get());
		entries.accept(WIZARD_PANTS.get());
		entries.accept(WIZARD_BOOTS.get());
		entries.accept(BATTLE_MAGE_HELMET.get());
		entries.accept(BATTLE_MAGE_CHESTPLATE.get());
		entries.accept(BATTLE_MAGE_LEGGINGS.get());
		entries.accept(BATTLE_MAGE_BOOTS.get());
		entries.accept(BATTLE_MAGE_UPGRADE_SMITHING_TEMPLATE.get());
		entries.accept(ArcanusBlocks.MAGIC_DOOR.get());
		entries.accept(ArcanusBlocks.ARCANE_WORKBENCH.get());

		entries.accept(WIZARD_SPAWN_EGG.get());
		entries.accept(OPOSSUM_SPAWN_EGG.get());
	}).build());

	private static SmithingTemplateItem getBattleMageUpgrade() {
		var appliesToText = Component.translatable("item.arcanus.smithing_template.battle_mage_upgrade.applies_to").withStyle(ChatFormatting.BLUE);
		var ingredientsText = Component.translatable("item.arcanus.smithing_template.battle_mage_upgrade.ingredients").withStyle(ChatFormatting.BLUE);
		var upgradeText = Component.translatable("upgrade.arcanus.battle_mage_upgrade").withStyle(ChatFormatting.GRAY);
		var baseSlotText = Component.translatable("item.arcanus.smithing_template.battle_mage_upgrade.base_slot_description");
		var additionsSlotText = Component.translatable("item.arcanus.smithing_template.battle_mage_upgrade.additions_slot_description");
		var baseIcons = SmithingTemplateItem.createTrimmableArmorIconList();
		var additionsIcons = List.of(SmithingTemplateItem.EMPTY_SLOT_AMETHYST_SHARD);
		return new SmithingTemplateItem(appliesToText, ingredientsText, upgradeText, baseSlotText, additionsSlotText, baseIcons, additionsIcons);
	}
}
