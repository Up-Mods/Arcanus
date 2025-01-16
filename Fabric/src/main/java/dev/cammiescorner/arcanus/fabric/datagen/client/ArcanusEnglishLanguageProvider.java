package dev.cammiescorner.arcanus.fabric.datagen.client;

import dev.cammiescorner.arcanus.api.entities.ArcanusEntityAttributes;
import dev.cammiescorner.arcanus.api.spells.SpellComponent;
import dev.cammiescorner.arcanus.common.data.*;
import dev.cammiescorner.arcanus.fabric.common.data.*;
import dev.cammiescorner.arcanus.fabric.common.registry.*;
import dev.cammiescorner.fabric.common.data.*;
import dev.cammiescorner.arcanus.fabric.common.items.BattleMageArmorItem;
import dev.cammiescorner.arcanus.common.registry.*;
import dev.cammiescorner.fabric.common.registry.*;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.WeatheringCopper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ArcanusEnglishLanguageProvider extends FabricLanguageProvider {

	private final CompletableFuture<HolderLookup.Provider> registriesFuture;

	public ArcanusEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(dataOutput);
		this.registriesFuture = registriesFuture;
	}

	@Override
	public void generateTranslations(TranslationBuilder builder) {
		builder.add(ArcanusItems.ITEM_GROUP.getRegistryKey(), "Arcanus: Continuum");
		builder.add(ArcanusItems.WOODEN_STAFF.get(), "Wooden Staff");
		builder.add(ArcanusItems.CRYSTAL_STAFF.get(), "Crystal Staff");
		builder.add(ArcanusItems.DIVINATION_STAFF.get(), "Divination Staff");
		builder.add(ArcanusItems.CRESCENT_STAFF.get(), "Crescent Staff");
		builder.add(ArcanusItems.ANCIENT_STAFF.get(), "Ancient Staff");
		builder.add(ArcanusItems.WAND.get(), "Wand");
		builder.add(ArcanusItems.THAUMATURGES_GAUNTLET.get(), "Thaumaturge's Gauntlet");
		builder.add(ArcanusItems.MIND_STAFF.get(), "Mind Staff");
		builder.add(ArcanusItems.MAGIC_TOME.get(), "Magic Tome");
		builder.add(ArcanusItems.MAGE_PISTOL.get(), "Mage Pistol");
		builder.add(ArcanusItems.WIZARD_HAT.get(), "Wizard Hat");
		builder.add(ArcanusItems.WIZARD_ROBES.get(), "Wizard Robes");
		builder.add(ArcanusItems.WIZARD_PANTS.get(), "Wizard Pants");
		builder.add(ArcanusItems.WIZARD_BOOTS.get(), "Wizard Boots");
		builder.add(ArcanusItems.SPELL_BOOK.get(), "Spell Book");
		builder.add(ArcanusItems.SCROLL_OF_KNOWLEDGE.get(), "Scroll of Knowledge");
		builder.add(ArcanusItems.WIZARD_SPAWN_EGG.get(), "Wizard Spawn Egg");
		builder.add(ArcanusItems.OPOSSUM_SPAWN_EGG.get(), "Opossum Spawn Egg");

		builder.add(ArcanusItems.BATTLE_MAGE_UPGRADE_SMITHING_TEMPLATE.get(), "Smithing Template");
		builder.add("upgrade.arcanus.battle_mage_upgrade", "Battle Mage Upgrade");
		builder.add("item.arcanus.smithing_template.battle_mage_upgrade.applies_to", "Diamond Armor");
		builder.add("item.arcanus.smithing_template.battle_mage_upgrade.ingredients", "Amethyst Shard");
		builder.add("item.arcanus.smithing_template.battle_mage_upgrade.base_slot_description", "Add diamond armor");
		builder.add("item.arcanus.smithing_template.battle_mage_upgrade.additions_slot_description", "Add Amethyst Shard");

		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_HELMET, WeatheringCopper.WeatherState.UNAFFECTED, false), "Battle Mage Helmet");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_HELMET, WeatheringCopper.WeatherState.UNAFFECTED, true), "Waxed Battle Mage Helmet");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_HELMET, WeatheringCopper.WeatherState.EXPOSED, false), "Exposed Battle Mage Helmet");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_HELMET, WeatheringCopper.WeatherState.EXPOSED, true), "Waxed Exposed Battle Mage Helmet");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_HELMET, WeatheringCopper.WeatherState.WEATHERED, false), "Weathered Battle Mage Helmet");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_HELMET, WeatheringCopper.WeatherState.WEATHERED, true), "Waxed Weathered Battle Mage Helmet");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_HELMET, WeatheringCopper.WeatherState.OXIDIZED, false), "Oxidized Battle Mage Helmet");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_HELMET, WeatheringCopper.WeatherState.OXIDIZED, true), "Waxed Oxidized Battle Mage Helmet");

		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_CHESTPLATE, WeatheringCopper.WeatherState.UNAFFECTED, false), "Battle Mage Chestplate");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_CHESTPLATE, WeatheringCopper.WeatherState.UNAFFECTED, true), "Waxed Battle Mage Chestplate");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_CHESTPLATE, WeatheringCopper.WeatherState.EXPOSED, false), "Exposed Battle Mage Chestplate");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_CHESTPLATE, WeatheringCopper.WeatherState.EXPOSED, true), "Waxed Exposed Battle Mage Chestplate");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_CHESTPLATE, WeatheringCopper.WeatherState.WEATHERED, false), "Weathered Battle Mage Chestplate");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_CHESTPLATE, WeatheringCopper.WeatherState.WEATHERED, true), "Waxed Weathered Battle Mage Chestplate");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_CHESTPLATE, WeatheringCopper.WeatherState.OXIDIZED, false), "Oxidized Battle Mage Chestplate");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_CHESTPLATE, WeatheringCopper.WeatherState.OXIDIZED, true), "Waxed Oxidized Battle Mage Chestplate");

		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_LEGGINGS, WeatheringCopper.WeatherState.UNAFFECTED, false), "Battle Mage Leggings");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_LEGGINGS, WeatheringCopper.WeatherState.UNAFFECTED, true), "Waxed Battle Mage Leggings");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_LEGGINGS, WeatheringCopper.WeatherState.EXPOSED, false), "Exposed Battle Mage Leggings");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_LEGGINGS, WeatheringCopper.WeatherState.EXPOSED, true), "Waxed Exposed Battle Mage Leggings");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_LEGGINGS, WeatheringCopper.WeatherState.WEATHERED, false), "Weathered Battle Mage Leggings");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_LEGGINGS, WeatheringCopper.WeatherState.WEATHERED, true), "Waxed Weathered Battle Mage Leggings");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_LEGGINGS, WeatheringCopper.WeatherState.OXIDIZED, false), "Oxidized Battle Mage Leggings");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_LEGGINGS, WeatheringCopper.WeatherState.OXIDIZED, true), "Waxed Oxidized Battle Mage Leggings");

		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_BOOTS, WeatheringCopper.WeatherState.UNAFFECTED, false), "Battle Mage Boots");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_BOOTS, WeatheringCopper.WeatherState.UNAFFECTED, true), "Waxed Battle Mage Boots");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_BOOTS, WeatheringCopper.WeatherState.EXPOSED, false), "Exposed Battle Mage Boots");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_BOOTS, WeatheringCopper.WeatherState.EXPOSED, true), "Waxed Exposed Battle Mage Boots");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_BOOTS, WeatheringCopper.WeatherState.WEATHERED, false), "Weathered Battle Mage Boots");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_BOOTS, WeatheringCopper.WeatherState.WEATHERED, true), "Waxed Weathered Battle Mage Boots");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_BOOTS, WeatheringCopper.WeatherState.OXIDIZED, false), "Oxidized Battle Mage Boots");
		itemStack(builder, BattleMageArmorItem.getStack(ArcanusItems.BATTLE_MAGE_BOOTS, WeatheringCopper.WeatherState.OXIDIZED, true), "Waxed Oxidized Battle Mage Boots");

		biome(builder, ArcanusBiomes.POCKET_DIMENSION, "Pocket Dimension");

		builder.add(ArcanusBlocks.ARCANE_WORKBENCH.get(), "Arcane Workbench");
		builder.add(ArcanusBlocks.MAGIC_DOOR.get(), "Magic Door");
		builder.add(ArcanusBlocks.MAGIC_BLOCK.get(), "Magic Block");
		builder.add(ArcanusBlocks.SPATIAL_RIFT_EXIT.get(), "Spatial Rift Exit");
		builder.add(ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get(), "Spatial Rift Wall");
		builder.add(ArcanusBlocks.SPATIAL_RIFT_WALL.get(), "Spatial Rift Wall");

		builder.add(ArcanusEntities.AGGRESSORB.get(), "Aggressorb");
		builder.add(ArcanusEntities.AOE.get(), "Area Of Effect");
		builder.add(ArcanusEntities.BEAM.get(), "Beam");
		builder.add(ArcanusEntities.GUARDIAN_ORB.get(), "Guardian Orb");
		builder.add(ArcanusEntities.MAGIC_PROJECTILE.get(), "Magic Projectile");
		builder.add(ArcanusEntities.MAGIC_RUNE.get(), "Magic Rune");
		builder.add(ArcanusEntities.MANA_SHIELD.get(), "Mana Shield");
		builder.add(ArcanusEntities.NECRO_SKELETON.get(), "Necro Skeleton");
		builder.add(ArcanusEntities.OPOSSUM.get(), "Opossum");
		builder.add(ArcanusEntities.PORTAL.get(), "Pocket Dimension Portal");
		builder.add(ArcanusEntities.SMITE.get(), "Smite");
		builder.add(ArcanusEntities.WIZARD.get(), "Wizard");

		builder.add(ArcanusEnchantments.MANA_POOL.get(), "Mana Pool");

		builder.add(ArcanusMobEffects.ANONYMITY.get(), "Anonymity");
		builder.add(ArcanusMobEffects.BOUNCY.get(), "Bouncy");
		builder.add(ArcanusMobEffects.COPPER_CURSE.get(), "Copper Curse");
		builder.add(ArcanusMobEffects.DANGER_SENSE.get(), "Danger Sense");
		builder.add(ArcanusMobEffects.DISCOMBOBULATE.get(), "Discombobulate");
		builder.add(ArcanusMobEffects.FLOAT.get(), "Float");
		builder.add(ArcanusMobEffects.FORTIFY.get(), "Fortify");
		builder.add(ArcanusMobEffects.MANA_LOCK.get(), "Mana Lock");
		builder.add(ArcanusMobEffects.MANA_WINGS.get(), "Mana Wings");
		builder.add(ArcanusMobEffects.STOCKPILE.get(), "Stockpile");
		builder.add(ArcanusMobEffects.VULNERABILITY.get(), "Vulnerability");
		builder.add(ArcanusMobEffects.SHRINK.get(), "Shrink");
		builder.add(ArcanusMobEffects.ENLARGE.get(), "Enlarge");

		tag(builder, ArcanusBiomeTags.C_HAS_VILLAGE, "Has Village");
		tag(builder, ArcanusBiomeTags.HAS_WIZARD_TOWER, "Has Wizard Tower");
		tag(builder, ArcanusBiomeTags.IS_POCKET_DIMENSION, "Is Pocket Dimension");

		tag(builder, ArcanusBlockTags.WARDING_NOT_ALLOWED, "Warding Not Allowed");

		tag(builder, ArcanusDimensionTags.WARDING_NOT_ALLOWED, "Warding Not Allowed");

		tag(builder, ArcanusEntityTags.C_IMMOVABLE, "Immovable");
		tag(builder, ArcanusEntityTags.DISPELLABLE, "Dispellable");
		tag(builder, ArcanusEntityTags.RUNE_TRIGGER_IGNORED, "Does not trigger Magic Runes");
		tag(builder, ArcanusEntityTags.SPATIAL_RIFT_IMMUNE, "Immune to Spatial Rifts");

		tag(builder, ArcanusEnchantmentTags.MANA_POOL_COMPATIBLE_WITH, "Compatible enchantments for Mana Pool");

		tag(builder, ArcanusItemTags.C_FEATHERS, "Feathers");
		tag(builder, ArcanusItemTags.CRAFTING_SPELLBINDING_SPELLBOOKS, "Spellbooks");
		tag(builder, ArcanusItemTags.COPPER_CURSE_IMMUNE, "Ignored by Copper Curse");
		tag(builder, ArcanusItemTags.STAVES, "Staves");
		tag(builder, ArcanusItemTags.WIZARD_ARMOR, "Wizard Armor");

		builder.add("text.arcanus.block_is_warded", "This block is warded.");
		builder.add("text.arcanus.disabled_component", "This spell contains one or more disabled spell components. Aborting spell.");
		builder.add("text.arcanus.too_many_orbs", "That target already has the maximum amount of Aggressorbs!");

		builder.add("text.arcanus.use_item.scroll_of_knowledge", "You feel a little smarter...");

		builder.add("text.arcanus.wizard_dialogue.no_wizard_armor", "Come back to me when you look like a wizard.");
		builder.add("tooltip.arcanus.wizard_dialogue.no_wizard_armor", "Maybe wearing Wizard Robes will help...");

		builder.add("door.arcanus.access_granted", "Access Granted...");
		builder.add("door.arcanus.not_owner", "You do not own this door");
		builder.add("door.arcanus.say_magic_word", "This door needs a magic word to open");
		builder.add("door.arcanus.password_set", "Password set to %s");

		builder.add("staff.arcanus.invalid_data", "!!! INVALID DATA !!!");
		builder.add("staff.arcanus.primary_color", "Focus Color");
		builder.add("staff.arcanus.secondary_color", "Body Color");

		builder.add("command.arcanus.pocket_dimension.regenerate.success.walls_only", "Repaired the walls of %s's pocket dimension");
		builder.add("command.arcanus.pocket_dimension.regenerate.success.full", "Regenerated %s's pocket dimension");
		builder.add("command.arcanus.pocket_dimension.regenerate.warn.teleport", "The pocket dimension plot you were in has been cleared by an admin.");

		builder.add("command.arcanus.pocket_dimension.export.success", "Successfully exported %s's pocket dimension");
		builder.add("command.arcanus.pocket_dimension.export.error.dimension_not_found", "unable to find %s!");
		builder.add("command.arcanus.pocket_dimension.export.error.pocket_not_found", "Pocket dimension for player %s has not been created yet!");

		builder.add("command.arcanus.wizard_level.get.success", "%s's wizard level is %s");
		builder.add("command.arcanus.wizard_level.set.success", "Set %s's wizard level to %s");

		advancement(builder, ArcanusAdvancements.ARCANE_ROOT, "Arcanus: Continuum", "[insert witty sales pitch for something you're already playing]");
		advancement(builder, ArcanusAdvancements.A_MAGICAL_CRYSTAL, "A Magical Crystal", "Find an Amethyst Shard");
		advancement(builder, ArcanusAdvancements.ARCANE_ARTIFICE, "Arcane Artifice", "Craft an Arcane Workbench");
		advancement(builder, ArcanusAdvancements.MAGICAL_MEET_AND_GREET, "Magical Meet & Greet", "Find a Wizard Tower and meet the residents");
		advancement(builder, ArcanusAdvancements.WIZARD_LEVEL_1, "First Steps", "Obtain your first Scroll of Knowledge from a Wizard");
		advancement(builder, ArcanusAdvancements.WIZARD_LEVEL_2, "Wizard Level 2", "Use a Scroll of Knowledge to reach level 2");
		advancement(builder, ArcanusAdvancements.WIZARD_LEVEL_3, "Wizard Level 3", "Use a Scroll of Knowledge to reach level 3");
		advancement(builder, ArcanusAdvancements.WIZARD_LEVEL_4, "Wizard Level 4", "Use a Scroll of Knowledge to reach level 4");
		advancement(builder, ArcanusAdvancements.WIZARD_LEVEL_5, "Wizard Level 5", "Use a Scroll of Knowledge to reach level 5");
		advancement(builder, ArcanusAdvancements.WIZARD_LEVEL_6, "Wizard Level 6", "Use a Scroll of Knowledge to reach level 6");
		advancement(builder, ArcanusAdvancements.WIZARD_LEVEL_7, "Wizard Level 7", "Use a Scroll of Knowledge to reach level 7");
		advancement(builder, ArcanusAdvancements.WIZARD_LEVEL_8, "Wizard Level 8", "Use a Scroll of Knowledge to reach level 8");
		advancement(builder, ArcanusAdvancements.WIZARD_LEVEL_9, "Wizard Level 9", "Use a Scroll of Knowledge to reach level 9");
		advancement(builder, ArcanusAdvancements.WIZARD_LEVEL_10, "Master of The Arcane", "Achieve maximum magical power!");

		damageType(builder, ArcanusDamageTypes.MAGIC, "%s was killed by %s using magic", null, "%s was killed by %s using magic with %s");
		damageType(builder, ArcanusDamageTypes.MAGIC_PROJECTILE, "%s was shot by %s using magic", null, "%s was shot by %s using magic with %s");

		builder.add(ArcanusEntityAttributes.MAX_MANA.get(), "Max Mana");
		builder.add(ArcanusEntityAttributes.MANA_REGEN.get(), "Mana Regeneration");
		builder.add(ArcanusEntityAttributes.BURNOUT_REGEN.get(), "Burnout Regeneration");
		builder.add(ArcanusEntityAttributes.MANA_LOCK.get(), "Mana Lock");
		builder.add(ArcanusEntityAttributes.SPELL_POTENCY.get(), "Spell Potency");
		builder.add(ArcanusEntityAttributes.MANA_COST.get(), "Mana Cost");
		builder.add(ArcanusEntityAttributes.MAGIC_RESISTANCE.get(), "Magic Resistance");
		builder.add(ArcanusEntityAttributes.SPELL_COOL_DOWN.get(), "Spell Cooldown");

		builder.add(SpellComponent.DISABLED_TRANSLATION_KEY, "Unreadable Text");
		spell(builder, ArcanusSpellComponents.EMPTY, "EMPTY");
		spell(builder, ArcanusSpellComponents.SELF, "Self Shape");
		spell(builder, ArcanusSpellComponents.TOUCH, "Touch Shape");
		spell(builder, ArcanusSpellComponents.PROJECTILE, "Projectile Shape");
		spell(builder, ArcanusSpellComponents.LOB, "Lob Shape");
		spell(builder, ArcanusSpellComponents.BOLT, "Bolt Shape");
		spell(builder, ArcanusSpellComponents.BEAM, "Beam Shape");
		spell(builder, ArcanusSpellComponents.RUNE, "Rune Shape");
		spell(builder, ArcanusSpellComponents.SMITE, "Smite Shape");
		spell(builder, ArcanusSpellComponents.AOE, "Area Of Effect Shape");
		spell(builder, ArcanusSpellComponents.EXPLOSION, "Explosion Shape");
		spell(builder, ArcanusSpellComponents.COUNTER, "Counter Shape");
		spell(builder, ArcanusSpellComponents.GUARDIAN_ORB, "Guardian Orb Shape");
		spell(builder, ArcanusSpellComponents.AGGRESSORB, "Aggressorb Shape");

		spell(builder, ArcanusSpellComponents.DAMAGE, "Damage Effect");
		spell(builder, ArcanusSpellComponents.HEAL, "Heal Effect");
		spell(builder, ArcanusSpellComponents.FIRE, "Fire Effect");
		spell(builder, ArcanusSpellComponents.ELECTRIC, "Electric Effect");
		spell(builder, ArcanusSpellComponents.ICE, "Ice Effect");
		spell(builder, ArcanusSpellComponents.PUSH, "Push Effect");
		spell(builder, ArcanusSpellComponents.PULL, "Pull Effect");
		spell(builder, ArcanusSpellComponents.TELEPORT, "Teleport Effect");
		spell(builder, ArcanusSpellComponents.DISPEL, "Dispel Effect");
		spell(builder, ArcanusSpellComponents.MANA_LOCK, "Mana Lock Effect");
		spell(builder, ArcanusSpellComponents.WITHERING, "Withering Effect");
		spell(builder, ArcanusSpellComponents.REGENERATE, "Regenerate Effect");
		spell(builder, ArcanusSpellComponents.VULNERABILITY, "Vulnerability Effect");
		spell(builder, ArcanusSpellComponents.FORTIFY, "Fortify Effect");
		spell(builder, ArcanusSpellComponents.BOUNCY, "Bouncy Effect");
		spell(builder, ArcanusSpellComponents.FEATHER, "Feather Effect");
		spell(builder, ArcanusSpellComponents.POWER, "Power Effect");
		spell(builder, ArcanusSpellComponents.NECROMANCY, "Necromancy Effect");
		spell(builder, ArcanusSpellComponents.MANA_SPLIT, "Mana Split Effect");
		spell(builder, ArcanusSpellComponents.ANONYMITY, "Anonymity Effect");
		spell(builder, ArcanusSpellComponents.MINE, "Mine Effect");
		spell(builder, ArcanusSpellComponents.BUILD, "Build Effect");
		spell(builder, ArcanusSpellComponents.LEVITATE, "Levitate Effect");
		spell(builder, ArcanusSpellComponents.GROWTH, "Growth Effect");
		spell(builder, ArcanusSpellComponents.MANA_SHIELD, "Mana Shield Effect");
		spell(builder, ArcanusSpellComponents.SPEED, "Speed Effect");
		spell(builder, ArcanusSpellComponents.SHRINK, "Shrink Effect");
		spell(builder, ArcanusSpellComponents.ENLARGE, "Enlarge Effect");
		spell(builder, ArcanusSpellComponents.COPPER_CURSE, "Copper Curse Effect");
		spell(builder, ArcanusSpellComponents.SPATIAL_RIFT, "Spatial Rift Effect");
		spell(builder, ArcanusSpellComponents.WARDING, "Warding Effect");
		spell(builder, ArcanusSpellComponents.DISCOMBOBULATE, "Discombobulate Effect");
		spell(builder, ArcanusSpellComponents.HASTE, "Haste Effect");
		spell(builder, ArcanusSpellComponents.FLOAT, "Float Effect");
		spell(builder, ArcanusSpellComponents.MANA_WINGS, "Mana Wings Effect");
		spell(builder, ArcanusSpellComponents.STOCKPILE, "Stockpile Effect");
		spell(builder, ArcanusSpellComponents.DANGER_SENSE, "Danger Sense Effect");

		builder.add("screen.arcanus.tooltip.component_count", "Spell Components");
		builder.add("screen.arcanus.tooltip.change_screens", "Change Mode");
		builder.add("screen.arcanus.tooltip.cycle_up", "Previous Skin");
		builder.add("screen.arcanus.tooltip.cycle_down", "Next Skin");

		builder.add("screen.arcanus.tooltip.undo", "Undo");
		builder.add("screen.arcanus.tooltip.redo", "Redo");
		builder.add("screen.arcanus.check.enabled", "✓");
		builder.add("screen.arcanus.check.disabled", "✘");

		builder.add("spell_book.arcanus.weight", "Weight");
		builder.add("spell_book.arcanus.weight.none", "None");
		builder.add("spell_book.arcanus.weight.very_light", "Very Light");
		builder.add("spell_book.arcanus.weight.light", "Light");
		builder.add("spell_book.arcanus.weight.medium", "Medium");
		builder.add("spell_book.arcanus.weight.heavy", "Heavy");
		builder.add("spell_book.arcanus.weight.very_heavy", "Very Heavy");
		builder.add("spell_book.arcanus.mana_cost", "Mana Cost");
		builder.add("spell_book.arcanus.mana_multiplier", "Mana Multiplier");
		builder.add("spell_book.arcanus.potency_modifier", "Potency Modifier");
		builder.add("spell_book.arcanus.cool_down", "Cool Down");
		builder.add("spell_book.arcanus.seconds", "s");

		builder.add("spell.arcanus.too_many_components", "Too many components!");
		builder.add("spell.arcanus.too_low_level", "Wizard level too low!");
		builder.add("spell.arcanus.not_enough_mana", "Not enough mana!");

		builder.add("config.arcanus.supporter_settings", "Supporter Perks");
		builder.add("config.arcanus.supporter_settings.magic_color", "Magic Color");
		builder.add("config.arcanus.supporter_settings.pocket_dimension_color", "Pocket Dimension Color");
		builder.add("config.arcanus.supporter_settings.halo_color", "Halo Color");
		builder.add("config.arcanus.supporter_settings.halo_enabled", "Halo Enabled");
		builder.add("config.arcanus.supporter_settings.halo_disabled", "Halo Disabled");
		builder.add("config.arcanus.supporter_settings.save_and_exit", "Save and Exit");
		builder.add("screen.arcanus.supporter_settings.saving", "Saving...");

		builder.add("screen.arcanus.support_us.title", "Support %s");
		builder.add("screen.arcanus.support_us.title2", "Arcanus: Continuum");
		builder.add("screen.arcanus.support_us.welcome_message", "Hey there, %s! If you wish to support the development of our many mods, please consider donating.");
		builder.add("screen.arcanus.support_us.perks_message", "Monthly subscribers get various perks across our mods, like being able to customize your magic color as well as access to more staves/wands!");
		builder.add("screen.arcanus.support_us.button_support_cammie", "Support Cammie!");
		builder.add("screen.arcanus.support_us.button_support_up", "Support Up!");

		builder.add("item.arcanus.compendium_arcanus", "Compendium Arcanus");
		builder.add("item.arcanus.compendium_arcanus.landing_text", "Arcanus: Continuum introduces a new magic system to the world of Minecraft. Players can create their own custom spells to suit however they like to play the game!");

		builder.add("config.arcanus.enchantments_category", "Enchantments");
		builder.add("config.arcanus.mana_pool", "Mana Pool");
		builder.add("config.arcanus.spellShapesCategory", "Spell Shapes");
		builder.add("config.arcanus.attackEffectsCategory", "Attack Effects");
		builder.add("config.arcanus.supportEffectsCategory", "Support Effects");
		builder.add("config.arcanus.utilityEffectsCategory", "Utility Effects");
		builder.add("config.arcanus.movementEffectsCategory", "Movement Effects");
		builder.add("config.arcanus.castingSpeedHasCoolDown", "Use Attack Cool Down for Casting?");
		builder.add("config.arcanus.sizeChangingIsPermanent", "Size Changing Effects are Permanent for Players and Tamed Animals (Requires Pehkui)");
		builder.add("config.arcanus.max_enchantment_level", "Max Enchantment Level");
		builder.add("config.arcanus.mana_per_level", "Extra Mana Per Level");
		builder.add("config.arcanus.mana_modifier_operation", "Mana Modifier Operation");
		builder.add("config.arcanus.enabled", "Enabled (Requires Restart)");
		builder.add("config.arcanus.weight", "Weight (Requires Restart)");
		builder.add("config.arcanus.manaCost", "Mana Cost (Requires Restart)");
		builder.add("config.arcanus.manaMultiplier", "Mana Multiplier (Requires Restart)");
		builder.add("config.arcanus.coolDown", "Cool Down (Requires Restart)");
		builder.add("config.arcanus.minimumLevel", "Minimum Level (Requires Restart)");
		builder.add("config.arcanus.potencyModifier", "Potency Modifier (Requires Restart)");
		builder.add("config.arcanus.projectileSpeed", "Projectile Speed");
		builder.add("config.arcanus.baseLifeSpan", "Base Life Span");
		builder.add("config.arcanus.lifeSpanModifier", "Life Span Modifier");
		builder.add("config.arcanus.range", "Range");
		builder.add("config.arcanus.delay", "Delay");
		builder.add("config.arcanus.strength", "Strength");
		builder.add("config.arcanus.maximumManaLock", "Maximum Mana Lock");
		builder.add("config.arcanus.maximumAggressorbs", "Maximum Aggressorbs");
		builder.add("config.arcanus.baseDamage", "Base Damage");
		builder.add("config.arcanus.baseTimeOnFire", "Base Time On Fire");
		builder.add("config.arcanus.baseStunTime", "Base Stun Time");
		builder.add("config.arcanus.wetEntityDamageMultiplier", "Damage Multiplier Against Wet Entities");
		builder.add("config.arcanus.baseFreezingTime", "Base Freezing Time");
		builder.add("config.arcanus.baseEffectDuration", "Base Effect Duration");
		builder.add("config.arcanus.baseHealth", "Base Health");
		builder.add("config.arcanus.effectDurationModifier", "Effect Duration Modifier");
		builder.add("config.arcanus.damageNeededToIncrease", "Damage Needed To Increase");
		builder.add("config.arcanus.baseHealAmount", "Base Heal Amount");
		builder.add("config.arcanus.baseChangeToActivate", "Base Chance To Activate");
		builder.add("config.arcanus.basePower", "Base Power");
		builder.add("config.arcanus.baseShrinkAmount", "Base Shrink Amount");
		builder.add("config.arcanus.baseEnlargeAmount", "Base Enlarge Amount");
		builder.add("config.arcanus.canSuckEntitiesIn", "Can Suck Entities In");
		builder.add("config.arcanus.portalGrowTime", "Spatial Rift Grow Time");
		builder.add("config.arcanus.pocketWidth", "Pocket Dimension Width");
		builder.add("config.arcanus.pocketHeight", "Pocket Dimension Height");
		builder.add("config.arcanus.canBeRemovedByOthers", "Can Be Removed By Other Players");
		builder.add("config.arcanus.basePushAmount", "Base Push Amount");
		builder.add("config.arcanus.basePullAmount", "Base Pull Amount");
		builder.add("config.arcanus.baseTeleportDistance", "Base Teleport Distance");
		builder.add("config.arcanus.removedUponTakingDamage", "Removed Upon Taking Damage");
		builder.add("config.arcanus.baseMovementSpeed", "Base Movement Speed");
		builder.add("config.arcanus.baseManaDrain", "Base Mana Drain");
		builder.add("config.arcanus.selfShapeProperties", "Self Shape");
		builder.add("config.arcanus.touchShapeProperties", "Touch Shape");
		builder.add("config.arcanus.projectileShapeProperties", "Projectile Shape");
		builder.add("config.arcanus.lobShapeProperties", "Lob Shape");
		builder.add("config.arcanus.boltShapeProperties", "Bolt Shape");
		builder.add("config.arcanus.beamShapeProperties", "Beam Shape");
		builder.add("config.arcanus.runeShapeProperties", "Rune Shape");
		builder.add("config.arcanus.explosionShapeProperties", "Explosion Shape");
		builder.add("config.arcanus.counterShapeProperties", "Counter Shape");
		builder.add("config.arcanus.aoeShapeProperties", "Area Of Effect Shape");
		builder.add("config.arcanus.smiteShapeProperties", "Smite Shape");
		builder.add("config.arcanus.guardianOrbShapeProperties", "Guardian Orb Shape");
		builder.add("config.arcanus.aggressorbShapeProperties", "Aggressorb Shape");
		builder.add("config.arcanus.damageEffectProperties", "Damage Effect");
		builder.add("config.arcanus.fireEffectProperties", "Fire Effect");
		builder.add("config.arcanus.electricEffectProperties", "Electric Effect");
		builder.add("config.arcanus.iceEffectProperties", "Ice Effect");
		builder.add("config.arcanus.vulnerabilityEffectProperties", "Vulnerability Effect");
		builder.add("config.arcanus.manaLockEffectProperties", "Mana Lock Effect");
		builder.add("config.arcanus.witheringEffectProperties", "Withering Effect");
		builder.add("config.arcanus.necromancyEffectProperties", "Necromancy Effect");
		builder.add("config.arcanus.manaSplitEffectProperties", "Mana Split Effect");
		builder.add("config.arcanus.copperCurseEffectProperties", "Copper Curse Effect");
		builder.add("config.arcanus.discombobulateEffectProperties", "Discombobulate Effect");
		builder.add("config.arcanus.stockpileEffectProperties", "Stockpile Effect");
		builder.add("config.arcanus.healEffectProperties", "Heal Effect");
		builder.add("config.arcanus.dispelEffectProperties", "Dispel Effect");
		builder.add("config.arcanus.regenerateEffectProperties", "Regenerate Effect");
		builder.add("config.arcanus.fortifyEffectProperties", "Fortify Effect");
		builder.add("config.arcanus.hasteEffectProperties", "Haste Effect");
		builder.add("config.arcanus.manaShieldEffectProperties", "Mana Shield Effect");
		builder.add("config.arcanus.dangerSenseEffectProperties", "Danger Sense Effect");
		builder.add("config.arcanus.pushEffectProperties", "Push Effect");
		builder.add("config.arcanus.pullEffectProperties", "Pull Effect");
		builder.add("config.arcanus.powerEffectProperties", "Power Effect");
		builder.add("config.arcanus.anonymityEffectProperties", "Anonymity Effect");
		builder.add("config.arcanus.mineEffectProperties", "Mine Effect");
		builder.add("config.arcanus.growthEffectProperties", "Growth Effect");
		builder.add("config.arcanus.shrinkEffectProperties", "Shrink Effect");
		builder.add("config.arcanus.enlargeEffectProperties", "Enlarge Effect");
		builder.add("config.arcanus.spatialRiftEffectProperties", "Spatial Rift Effect");
		builder.add("config.arcanus.wardingEffectProperties", "Warding Effect");
		builder.add("config.arcanus.buildEffectProperties", "Build Effect");
		builder.add("config.arcanus.levitateEffectProperties", "Levitate Effect");
		builder.add("config.arcanus.speedEffectProperties", "Speed Effect");
		builder.add("config.arcanus.teleportEffectProperties", "Teleport Effect");
		builder.add("config.arcanus.bouncyEffectProperties", "Bouncy Effect");
		builder.add("config.arcanus.featherEffectProperties", "Feather Effect");
		builder.add("config.arcanus.floatEffectProperties", "Float Effect");
		builder.add("config.arcanus.manaWingsEffectProperties", "Mana Wings Effect");
	}

	private void damageType(TranslationBuilder builder, ResourceKey<DamageType> typeKey, String defaultTranslation, @Nullable String killedByTranslation, @Nullable String killedWithItemTranslation) {
		registriesFuture.thenAccept(registries -> {
			var damageTypes = registries.lookupOrThrow(Registries.DAMAGE_TYPE);
			var type = damageTypes.getOrThrow(typeKey).value();

			if(type.deathMessageType() != DeathMessageType.DEFAULT) {
				throw new IllegalArgumentException("Death message type not currently supported: " + type.deathMessageType());
			}

			var translationKey = "death.attack." + type.msgId();
			builder.add(translationKey, defaultTranslation);
			builder.add(translationKey + ".player", killedByTranslation != null ? killedByTranslation : defaultTranslation);
			builder.add(translationKey + ".item", killedWithItemTranslation != null ? killedWithItemTranslation : defaultTranslation);
		});
	}

	private void itemStack(TranslationBuilder builder, ItemStack stack, String translationValue) {
		builder.add(stack.getDescriptionId(), translationValue);
	}

	private void advancement(TranslationBuilder builder, ResourceLocation advancementId, String title, String description) {
		builder.add(Util.makeDescriptionId("advancements", advancementId.withSuffix(".title")), title);
		builder.add(Util.makeDescriptionId("advancements", advancementId.withSuffix(".description")), description);
	}

	private void spell(TranslationBuilder builder, RegistrySupplier<? extends SpellComponent> component, String translation) {
		builder.add(component.get().getTranslationKey(), translation);
	}

	private void tag(TranslationBuilder builder, TagKey<?> tag, String translation) {
		var registryName = tag.registry().location().toShortLanguageKey().replace('/', '.');
		var tagName = Util.makeDescriptionId("tag." + registryName, tag.location());
		builder.add(tagName, translation);
	}

	private void biome(TranslationBuilder builder, ResourceKey<Biome> biome, String translation) {
		builder.add(Util.makeDescriptionId("biome", biome.location()), translation);
	}
}
