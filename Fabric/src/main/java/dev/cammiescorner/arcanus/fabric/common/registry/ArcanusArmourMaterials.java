package dev.cammiescorner.arcanus.fabric.common.registry;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum ArcanusArmourMaterials implements StringRepresentable, ArmorMaterial {
	WIZARD("wizard", 5, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
		map.put(ArmorItem.Type.BOOTS, 1);
		map.put(ArmorItem.Type.LEGGINGS, 2);
		map.put(ArmorItem.Type.CHESTPLATE, 3);
		map.put(ArmorItem.Type.HELMET, 1);
	}), 25, SoundEvents.ARMOR_EQUIP_LEATHER, 0f, 0f, () -> Ingredient.of(Items.LEATHER)),
	BATTLE_MAGE("battle_mage", 35, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
		map.put(ArmorItem.Type.BOOTS, 2);
		map.put(ArmorItem.Type.LEGGINGS, 7);
		map.put(ArmorItem.Type.CHESTPLATE, 8);
		map.put(ArmorItem.Type.HELMET, 3);
	}), 25, SoundEvents.ARMOR_EQUIP_IRON, 0f, 0f, () -> Ingredient.of(Items.AMETHYST_SHARD));

	public static final Codec<ArmorMaterials> CODEC = StringRepresentable.fromEnum(ArmorMaterials::values);
	private static final EnumMap<ArmorItem.Type, Integer> BASE_DURABILITY_VALUES = Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
		map.put(ArmorItem.Type.BOOTS, 13);
		map.put(ArmorItem.Type.LEGGINGS, 15);
		map.put(ArmorItem.Type.CHESTPLATE, 16);
		map.put(ArmorItem.Type.HELMET, 11);
	});
	private final String name;
	private final int durabilityMultiplier;
	private final EnumMap<ArmorItem.Type, Integer> slotProtections;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final float knockbackResistance;
	private final LazyLoadedValue<Ingredient> repairIngredientSupplier;

	ArcanusArmourMaterials(String name, int durability, EnumMap<ArmorItem.Type, Integer> slotProtections, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> supplier) {
		this.name = name;
		this.durabilityMultiplier = durability;
		this.slotProtections = slotProtections;
		this.enchantability = enchantability;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredientSupplier = new LazyLoadedValue<>(supplier);
	}

	@Override
	public int getDurabilityForType(ArmorItem.Type slot) {
		return BASE_DURABILITY_VALUES.get(slot) * durabilityMultiplier;
	}

	@Override
	public int getDefenseForType(ArmorItem.Type slot) {
		return slotProtections.get(slot);
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return this.equipSound;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredientSupplier.get();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}

	@Override
	public String getSerializedName() {
		return name;
	}
}
