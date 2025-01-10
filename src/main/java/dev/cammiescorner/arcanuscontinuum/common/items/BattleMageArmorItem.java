package dev.cammiescorner.arcanuscontinuum.common.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;

import java.util.Locale;
import java.util.function.Supplier;

public class BattleMageArmorItem extends WizardArmorItem {
	public BattleMageArmorItem(ArmorMaterial armorMaterial, Type equipmentSlot, double manaRegen, double magicResist, double spellPotency, double manaCostMultiplier, double spellCoolDown) {
		super(armorMaterial, equipmentSlot, manaRegen, magicResist, spellPotency, manaCostMultiplier, spellCoolDown);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);

		if(world instanceof ServerLevel serverWorld && entity instanceof LivingEntity living && living.getItemBySlot(getEquipmentSlot()).equals(stack) && !isWaxed(stack)) {
			int randomTickSpeed = serverWorld.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
			int oxidation = getOxidation(stack).ordinal();
			CompoundTag nbt = stack.getOrCreateTagElement(ItemStack.TAG_DISPLAY);

			if(serverWorld.random.nextFloat() < 0.00001 * randomTickSpeed && oxidation < WeatheringCopper.WeatherState.values().length - 1)
				nbt.putInt("oxidation", oxidation + 1);
		}
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		String s = "_";

		if(isWaxed(stack))
			s = "_waxed_";

		return super.getDescriptionId(stack) + s + getOxidation(stack).name().toLowerCase(Locale.ROOT);
	}

	@Override
	public int getColor(ItemStack stack) {
		CompoundTag tag = stack.getTagElement(ItemStack.TAG_DAMAGE);
		return tag != null && tag.contains(ItemStack.TAG_COLOR, Tag.TAG_ANY_NUMERIC) ? tag.getInt(ItemStack.TAG_COLOR) : 0xb38ef3;
	}

	public static boolean isWaxed(ItemStack stack) {
		CompoundTag tag = stack.getTagElement(ItemStack.TAG_DISPLAY);
		return tag != null && tag.getBoolean("waxed");
	}

	public static void setWaxed(ItemStack stack, boolean waxed) {
		stack.getOrCreateTagElement(ItemStack.TAG_DISPLAY).putBoolean("waxed", waxed);
	}

	public static WeatheringCopper.WeatherState getOxidation(ItemStack stack) {
		CompoundTag tag = stack.getTagElement(ItemStack.TAG_DISPLAY);
		return tag != null && tag.contains("oxidation", Tag.TAG_ANY_NUMERIC) ? WeatheringCopper.WeatherState.values()[tag.getInt("oxidation")] : WeatheringCopper.WeatherState.UNAFFECTED;
	}

	public static void setOxidation(ItemStack stack, WeatheringCopper.WeatherState oxidizationLevel) {
		stack.getOrCreateTagElement(ItemStack.TAG_DISPLAY).putInt("oxidation", oxidizationLevel.ordinal());
	}

	public static ItemStack getStack(Supplier<? extends ItemLike> itemProvider, WeatheringCopper.WeatherState oxidizationLevel, boolean waxed) {
		var stack = new ItemStack(itemProvider.get());
		if(waxed) {
			setWaxed(stack, true);
		}
		if(oxidizationLevel != WeatheringCopper.WeatherState.UNAFFECTED) {
			setOxidation(stack, oxidizationLevel);
		}
		return stack;
	}
}
