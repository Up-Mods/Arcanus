package dev.cammiescorner.arcanus.fabric.common.enchantments;

import dev.cammiescorner.arcanus.ArcanusConfig;
import dev.cammiescorner.arcanus.fabric.common.data.ArcanusEnchantmentTags;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.EnumMap;
import java.util.UUID;

public class ManaPoolEnchantment extends Enchantment {
	public static final EnumMap<EquipmentSlot, UUID> EQUIPMENT_UUIDS = Util.make(new EnumMap<>(EquipmentSlot.class), enumMap -> {
		enumMap.put(EquipmentSlot.HEAD, UUID.fromString("1562d8cd-bf92-4e10-8345-c8baffb944f5"));
		enumMap.put(EquipmentSlot.CHEST, UUID.fromString("cace3de8-0c66-4166-bca2-fcac7b50001a"));
		enumMap.put(EquipmentSlot.LEGS, UUID.fromString("22917aed-0acf-4884-8e67-986a4650fa33"));
		enumMap.put(EquipmentSlot.FEET, UUID.fromString("a3766725-15d8-4a0a-afd5-13d32fbfe0b8"));
		enumMap.put(EquipmentSlot.MAINHAND, UUID.fromString("f907c4a6-a768-4b52-b57a-e4a5c0aee381"));
		enumMap.put(EquipmentSlot.OFFHAND, UUID.fromString("a1afd122-7963-4192-a759-cd5e9127c46b"));
	});

	public ManaPoolEnchantment() {
		super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR, new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET });
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@Override
	public int getMaxLevel() {
		return ArcanusConfig.Enchantments.ManaPool.maxLevel;
	}

	@Override
	public boolean checkCompatibility(Enchantment other) {
		return BuiltInRegistries.ENCHANTMENT.getOrCreateTag(ArcanusEnchantmentTags.MANA_POOL_COMPATIBLE_WITH).contains(BuiltInRegistries.ENCHANTMENT.wrapAsHolder(other));
	}

	public static UUID getUuidForSlot(EquipmentSlot slot) {
		return EQUIPMENT_UUIDS.get(slot);
	}
}
