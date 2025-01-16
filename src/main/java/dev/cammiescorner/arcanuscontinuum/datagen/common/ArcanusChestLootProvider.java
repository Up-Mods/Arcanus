package dev.cammiescorner.arcanuscontinuum.datagen.common;

import dev.cammiescorner.arcanuscontinuum.api.spells.Spell;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusLootTables;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusItems;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.joml.Vector2i;

import java.util.List;
import java.util.function.BiConsumer;

public class ArcanusChestLootProvider extends SimpleFabricLootTableProvider {

	public ArcanusChestLootProvider(FabricDataOutput output) {
		super(output, LootContextParamSets.CHEST);
	}

	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
		output.accept(ArcanusLootTables.WIZARD_TOWER_CHEST, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(BuiltInLootTables.IGLOO_CHEST))));
		output.accept(ArcanusLootTables.WIZARD_TOWER_BOOKSHELF, LootTable.lootTable()
			.withPool(LootPool.lootPool()
				.setRolls(UniformGenerator.between(0, 6))
				.add(AlternativesEntry.alternatives(
					LootItem.lootTableItem(ArcanusItems.SPELL_BOOK.get())
						.apply(SetNbtFunction.setTag(healSelfSpell()))
						.when(LootItemRandomChanceCondition.randomChance(0.25F)),
					LootItem.lootTableItem(ArcanusItems.SPELL_BOOK.get())
						.apply(SetNbtFunction.setTag(healAllySpell()))
						.when(LootItemRandomChanceCondition.randomChance(0.25F)),
					LootItem.lootTableItem(ArcanusItems.SPELL_BOOK.get())
						.apply(SetNbtFunction.setTag(paladinsShieldSpell()))
						.when(LootItemRandomChanceCondition.randomChance(0.25F)),
					LootItem.lootTableItem(ArcanusItems.SPELL_BOOK.get())
						.apply(SetNbtFunction.setTag(fireballSpell()))
						.when(LootItemRandomChanceCondition.randomChance(0.25F)),
					LootItem.lootTableItem(ArcanusItems.SPELL_BOOK.get())
						.apply(SetNbtFunction.setTag(magicMissileSpell()))
						.when(LootItemRandomChanceCondition.randomChance(0.25F)),
					LootItem.lootTableItem(ArcanusItems.SPELL_BOOK.get())
						.apply(SetNbtFunction.setTag(smiteSpell()))
						.when(LootItemRandomChanceCondition.randomChance(0.25F)),
					LootItem.lootTableItem(ArcanusItems.SPELL_BOOK.get())
						.apply(SetNbtFunction.setTag(blinkSpell()))
						.when(LootItemRandomChanceCondition.randomChance(0.25F)),
					LootItem.lootTableItem(ArcanusItems.SPELL_BOOK.get())
						.apply(SetNbtFunction.setTag(zoomiesSpell()))
						.when(LootItemRandomChanceCondition.randomChance(0.25F))
				))
				//-----------
				// TODO written books?
				//-----------
				.add(LootItem.lootTableItem(Items.WRITABLE_BOOK).setWeight(5))
				.add(LootItem.lootTableItem(Items.BOOK).setWeight(5))
				.add(LootItem.lootTableItem(Items.BOOK).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(1, 3))))
				.add(LootItem.lootTableItem(Items.BOOK).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(11, 20))))
				.add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(21, 30))))


			)
		);
	}

	private static CompoundTag healSelfSpell() {
		var spell = new Spell(List.of(new SpellGroup(ArcanusSpellComponents.SELF.get(), List.of(ArcanusSpellComponents.HEAL.get()), List.of(new Vector2i(82, 83), new Vector2i(146, 81)))), "Heal Self");
		return spell.toNbt();
	}

	private static CompoundTag healAllySpell() {
		var spell = new Spell(List.of(new SpellGroup(ArcanusSpellComponents.PROJECTILE.get(), List.of(ArcanusSpellComponents.HEAL.get(), ArcanusSpellComponents.HEAL.get()), List.of(new Vector2i(80, 84), new Vector2i(133, 48), new Vector2i(139, 118)))), "Heal Ally");
		return spell.toNbt();
	}

	private static CompoundTag paladinsShieldSpell() {
		var spell = new Spell(List.of(
			new SpellGroup(ArcanusSpellComponents.SELF.get(), List.of(ArcanusSpellComponents.MANA_SHIELD.get()), List.of(new Vector2i(119, 40), new Vector2i(172, 68))),
			new SpellGroup(ArcanusSpellComponents.AOE.get(), List.of(ArcanusSpellComponents.HEAL.get(), ArcanusSpellComponents.HEAL.get(), ArcanusSpellComponents.HEAL.get()), List.of(new Vector2i(146, 120), new Vector2i(94, 133), new Vector2i(67, 76), new Vector2i(120, 85)))
		), "Paladin's Shield");
		return spell.toNbt();
	}

	private static CompoundTag fireballSpell() {
		var spell = new Spell(List.of(new SpellGroup(ArcanusSpellComponents.LOB.get(), List.of(ArcanusSpellComponents.DAMAGE.get(), ArcanusSpellComponents.DAMAGE.get(), ArcanusSpellComponents.DAMAGE.get(), ArcanusSpellComponents.FIRE.get()), List.of(new Vector2i(118, 81), new Vector2i(116, 36), new Vector2i(71, 82), new Vector2i(118, 129), new Vector2i(169, 81)))), "Fireball");
		return spell.toNbt();
	}

	private static CompoundTag magicMissileSpell() {
		var spell = new Spell(List.of(new SpellGroup(ArcanusSpellComponents.PROJECTILE.get(), List.of(ArcanusSpellComponents.DAMAGE.get(), ArcanusSpellComponents.DAMAGE.get()), List.of(new Vector2i(86, 82), new Vector2i(146, 81), new Vector2i(117, 127)))), "Magic Missile");
		return spell.toNbt();
	}

	private static CompoundTag smiteSpell() {
		var spell = new Spell(List.of(
			new SpellGroup(ArcanusSpellComponents.SMITE.get(), List.of(ArcanusSpellComponents.DAMAGE.get(), ArcanusSpellComponents.DAMAGE.get(), ArcanusSpellComponents.DAMAGE.get(), ArcanusSpellComponents.DAMAGE.get()), List.of(new Vector2i(117, 46), new Vector2i(173, 73), new Vector2i(144, 128), new Vector2i(77, 124), new Vector2i(60, 65))),
			new SpellGroup(ArcanusSpellComponents.EXPLOSION.get(), List.of(), List.of(new Vector2i(117, 91)))
		), "Smite");
		return spell.toNbt();
	}

	private static CompoundTag blinkSpell() {
		var spell = new Spell(List.of(new SpellGroup(ArcanusSpellComponents.BOLT.get(), List.of(ArcanusSpellComponents.TELEPORT.get(), ArcanusSpellComponents.TELEPORT.get(), ArcanusSpellComponents.TELEPORT.get()), List.of(new Vector2i(119, 50), new Vector2i(159, 98), new Vector2i(116, 135), new Vector2i(79, 92)))), "Blink");
		return spell.toNbt();
	}

	private static CompoundTag zoomiesSpell() {
		var spell = new Spell(List.of(new SpellGroup(ArcanusSpellComponents.SELF.get(), List.of(ArcanusSpellComponents.SPEED.get(), ArcanusSpellComponents.SPEED.get(), ArcanusSpellComponents.SPEED.get(), ArcanusSpellComponents.SPEED.get()), List.of(new Vector2i(121, 41), new Vector2i(168, 88), new Vector2i(115, 134), new Vector2i(69, 84), new Vector2i(118, 85)))), "Zoomies");
		return spell.toNbt();
	}
}
