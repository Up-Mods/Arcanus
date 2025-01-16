package dev.cammiescorner.arcanuscontinuum.common.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

public class WizardRobesArmorItem extends WizardArmorItem implements DyeableLeatherItem {
	public WizardRobesArmorItem(ArmorMaterial armorMaterial, Type equipmentSlot, double manaRegen, double magicResist, double spellPotency, double manaCostMultiplier, double spellCoolDown) {
		super(armorMaterial, equipmentSlot, manaRegen, magicResist, spellPotency, manaCostMultiplier, spellCoolDown);
	}

	@Override
	public int getColor(ItemStack stack) {
		CompoundTag tag = stack.getTagElement(ItemStack.TAG_DISPLAY);
		return tag != null && tag.contains(ItemStack.TAG_COLOR, Tag.TAG_ANY_NUMERIC) ? tag.getInt(ItemStack.TAG_COLOR) : 0x52392a;
	}
}
