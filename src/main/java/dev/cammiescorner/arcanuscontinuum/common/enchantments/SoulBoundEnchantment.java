package dev.cammiescorner.arcanuscontinuum.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SoulBoundEnchantment extends Enchantment {
	public SoulBoundEnchantment() {
		super(Rarity.RARE, EnchantmentCategory.WEARABLE, EquipmentSlot.values());
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return stack.isEnchantable();
	}
}
