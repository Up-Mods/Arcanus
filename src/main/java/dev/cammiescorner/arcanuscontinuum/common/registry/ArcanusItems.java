package dev.cammiescorner.arcanuscontinuum.common.registry;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.items.*;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.cammiescorner.arcanuscontinuum.common.util.StaffType;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.WizardData;
import dev.upcraft.datasync.api.util.GameProfileHelper;
import dev.upcraft.sparkweave.api.SparkweaveApi;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Formatting;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.List;
import java.util.UUID;

public class ArcanusItems {

	public static final RegistryHandler<ItemGroup> ITEM_GROUPS = RegistryHandler.create(RegistryKeys.ITEM_GROUP, Arcanus.MOD_ID);
	public static final RegistryHandler<Item> ITEMS = RegistryHandler.create(RegistryKeys.ITEM, Arcanus.MOD_ID);

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

	public static final RegistrySupplier<Item> WIZARD_HAT = ITEMS.register("wizard_hat", () -> new WizardArmorItem(ArcanusArmourMaterials.WIZARD, ArmorItem.ArmorSlot.HELMET, 1, 0.1, 0, 0, -0.06));
	public static final RegistrySupplier<Item> WIZARD_ROBES = ITEMS.register("wizard_robes", () -> new WizardArmorItem(ArcanusArmourMaterials.WIZARD, ArmorItem.ArmorSlot.CHESTPLATE, 2, 0.2, 0, 0, -0.12));
	public static final RegistrySupplier<Item> WIZARD_PANTS = ITEMS.register("wizard_pants", () -> new WizardArmorItem(ArcanusArmourMaterials.WIZARD, ArmorItem.ArmorSlot.LEGGINGS, 2, 0.2, 0, 0, -0.1));
	public static final RegistrySupplier<Item> WIZARD_BOOTS = ITEMS.register("wizard_boots", () -> new WizardArmorItem(ArcanusArmourMaterials.WIZARD, ArmorItem.ArmorSlot.BOOTS, 1, 0.1, 0, 0, -0.05));
	public static final RegistrySupplier<Item> BATTLE_MAGE_HELMET = ITEMS.register("battle_mage_helmet", () -> new BattleMageArmorItem(ArcanusArmourMaterials.BATTLE_MAGE, ArmorItem.ArmorSlot.HELMET, 0.25, 0, 0.2, 0.06, 0));
	public static final RegistrySupplier<Item> BATTLE_MAGE_CHESTPLATE = ITEMS.register("battle_mage_chestplate", () -> new BattleMageArmorItem(ArcanusArmourMaterials.BATTLE_MAGE, ArmorItem.ArmorSlot.CHESTPLATE, 0.75, 0, 0.3, 0.12, 0));
	public static final RegistrySupplier<Item> BATTLE_MAGE_LEGGINGS = ITEMS.register("battle_mage_leggings", () -> new BattleMageArmorItem(ArcanusArmourMaterials.BATTLE_MAGE, ArmorItem.ArmorSlot.LEGGINGS, 0.75, 0, 0.3, 0.1, 0));
	public static final RegistrySupplier<Item> BATTLE_MAGE_BOOTS = ITEMS.register("battle_mage_boots", () -> new BattleMageArmorItem(ArcanusArmourMaterials.BATTLE_MAGE, ArmorItem.ArmorSlot.BOOTS, 0.25, 0, 0.2, 0.05, 0));

	public static final RegistrySupplier<Item> BATTLE_MAGE_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("battle_mage_upgrade_smithing_template", ArcanusItems::getBattleMageUpgrade);
	public static final RegistrySupplier<Item> SPELL_BOOK = ITEMS.register("spell_book", SpellBookItem::new);
	public static final RegistrySupplier<Item> SCROLL_OF_KNOWLEDGE = ITEMS.register("scroll_of_knowledge", ScrollOfKnowledgeItem::new);
	public static final RegistrySupplier<Item> WIZARD_SPAWN_EGG = ITEMS.register("wizard_spawn_egg", () -> new SpawnEggItem(ArcanusEntities.WIZARD.get(), 0x52392a, 0xffd87c, new QuiltItemSettings()));
	public static final RegistrySupplier<Item> OPOSSUM_SPAWN_EGG = ITEMS.register("opossum_spawn_egg", () -> new SpawnEggItem(ArcanusEntities.OPOSSUM.get(), 0x131317, 0xbdbdbd, new QuiltItemSettings()));

	public static final RegistrySupplier<ItemGroup> ITEM_GROUP = ITEM_GROUPS.register("general", () -> FabricItemGroup.builder().name(Arcanus.translate("itemGroup", "general")).icon(() -> new ItemStack(ArcanusItems.CRYSTAL_STAFF.get())).entries((params, entries) -> {
		entries.addItem(SPELL_BOOK.get());
		entries.addItem(SCROLL_OF_KNOWLEDGE.get());

		if (SparkweaveApi.CLIENTSIDE_ENVIRONMENT) {
			var currentPlayerId = GameProfileHelper.getClientProfile().getId();
			entries.addStack(StaffItem.setCraftedBy(new ItemStack(WOODEN_STAFF.get()), currentPlayerId));
			entries.addStack(StaffItem.setCraftedBy(new ItemStack(CRYSTAL_STAFF.get()), currentPlayerId));
			entries.addStack(StaffItem.setCraftedBy(new ItemStack(DIVINATION_STAFF.get()), currentPlayerId));
			entries.addStack(StaffItem.setCraftedBy(new ItemStack(CRESCENT_STAFF.get()), currentPlayerId));
			entries.addStack(StaffItem.setCraftedBy(new ItemStack(ANCIENT_STAFF.get()), currentPlayerId));

			if (WizardData.isSupporter(currentPlayerId)) {
				entries.addStack(StaffItem.setCraftedBy(new ItemStack(WAND.get()), currentPlayerId));
				entries.addStack(StaffItem.setCraftedBy(new ItemStack(THAUMATURGES_GAUNTLET.get()), currentPlayerId));
				entries.addStack(StaffItem.setCraftedBy(new ItemStack(MIND_STAFF.get()), currentPlayerId));
				entries.addStack(StaffItem.setCraftedBy(new ItemStack(MAGIC_TOME.get()), currentPlayerId));
				entries.addStack(StaffItem.setCraftedBy(new ItemStack(MAGE_PISTOL.get()), currentPlayerId));
			}
		} else {
			var dummyId = UUID.fromString("6147825f-5493-4154-87c5-5c03c6b0a7c2");
			entries.addStack(StaffItem.setCraftedBy(new ItemStack(WOODEN_STAFF.get()), dummyId));
			entries.addStack(StaffItem.setCraftedBy(new ItemStack(CRYSTAL_STAFF.get()), dummyId));
			entries.addStack(StaffItem.setCraftedBy(new ItemStack(DIVINATION_STAFF.get()), dummyId));
			entries.addStack(StaffItem.setCraftedBy(new ItemStack(CRESCENT_STAFF.get()), dummyId));
			entries.addStack(StaffItem.setCraftedBy(new ItemStack(ANCIENT_STAFF.get()), dummyId));
		}

		entries.addItem(WIZARD_HAT.get());
		entries.addItem(WIZARD_ROBES.get());
		entries.addItem(WIZARD_PANTS.get());
		entries.addItem(WIZARD_BOOTS.get());
		entries.addItem(BATTLE_MAGE_HELMET.get());
		entries.addItem(BATTLE_MAGE_CHESTPLATE.get());
		entries.addItem(BATTLE_MAGE_LEGGINGS.get());
		entries.addItem(BATTLE_MAGE_BOOTS.get());
		entries.addItem(BATTLE_MAGE_UPGRADE_SMITHING_TEMPLATE.get());
		entries.addItem(ArcanusBlocks.MAGIC_DOOR.get());
		entries.addItem(ArcanusBlocks.ARCANE_WORKBENCH.get());

		entries.addItem(WIZARD_SPAWN_EGG.get());
		entries.addItem(OPOSSUM_SPAWN_EGG.get());
	}).build());

	private static SmithingTemplateItem getBattleMageUpgrade() {
		var appliesToText = Arcanus.translate("item", "smithing_template", "battle_mage_upgrade", "applies_to").formatted(Formatting.BLUE);
		var ingredientsText = Arcanus.translate("item", "smithing_template", "battle_mage_upgrade", "ingredients").formatted(Formatting.BLUE);
		var upgradeText = Arcanus.translate("upgrade", "battle_mage_upgrade").formatted(Formatting.GRAY);
		var baseSlotText = Arcanus.translate("item", "smithing_template", "battle_mage_upgrade", "base_slot_description");
		var additionsSlotText = Arcanus.translate("item", "smithing_template", "battle_mage_upgrade", "additions_slot_description");
		var baseIcons = SmithingTemplateItem.getArmorIcons();
		var additionsIcons = List.of(SmithingTemplateItem.AMETHYST_ICON);
		return new SmithingTemplateItem(appliesToText, ingredientsText, upgradeText, baseSlotText, additionsSlotText, baseIcons, additionsIcons);
	}
}
