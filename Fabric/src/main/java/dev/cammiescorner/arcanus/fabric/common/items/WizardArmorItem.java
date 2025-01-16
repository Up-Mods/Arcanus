package dev.cammiescorner.arcanus.fabric.common.items;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.cammiescorner.arcanus.api.entities.ArcanusEntityAttributes;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class WizardArmorItem extends ArmorItem {
	private static final Map<Type, UUID> MODIFIER_IDS = Map.of(
		Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
		Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
		Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
		Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
	);
	private final Supplier<Multimap<Attribute, AttributeModifier>> attributeModifiers;

	public WizardArmorItem(ArmorMaterial armorMaterial, Type equipmentSlot, double manaRegen, double magicResist, double spellPotency, double manaCostMultiplier, double spellCoolDown) {
		super(armorMaterial, equipmentSlot, new Item.Properties().stacksTo(1));

		this.attributeModifiers = Suppliers.memoize(() -> {
			UUID modifierID = MODIFIER_IDS.get(equipmentSlot);
			return ImmutableMultimap.<Attribute, AttributeModifier>builder()
				.put(Attributes.ARMOR, new AttributeModifier(modifierID, "Armor modifier", armorMaterial.getDefenseForType(equipmentSlot), AttributeModifier.Operation.ADDITION))
				.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(modifierID, "Armor toughness", armorMaterial.getToughness(), AttributeModifier.Operation.ADDITION))
				.put(ArcanusEntityAttributes.MANA_REGEN.get(), new AttributeModifier(modifierID, "Armor modifier", manaRegen, AttributeModifier.Operation.ADDITION))
				.put(ArcanusEntityAttributes.MAGIC_RESISTANCE.get(), new AttributeModifier(modifierID, "Armor modifier", magicResist, AttributeModifier.Operation.MULTIPLY_BASE))
				.put(ArcanusEntityAttributes.SPELL_POTENCY.get(), new AttributeModifier(modifierID, "Armor modifier", spellPotency, AttributeModifier.Operation.MULTIPLY_BASE))
				.put(ArcanusEntityAttributes.MANA_COST.get(), new AttributeModifier(modifierID, "Armor modifier", manaCostMultiplier, AttributeModifier.Operation.MULTIPLY_BASE))
				.put(ArcanusEntityAttributes.SPELL_COOL_DOWN.get(), new AttributeModifier(modifierID, "Armor modifier", spellCoolDown, AttributeModifier.Operation.MULTIPLY_BASE))
				.build();
		});

		CauldronInteraction.WATER.put(this, CauldronInteraction.DYED_ITEM);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		double manaDrain = 1;

		if(!world.isClientSide() && stack.isDamaged() && entity instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(getEquipmentSlot()) == stack && ArcanusComponents.getMana(livingEntity) >= manaDrain && ArcanusComponents.drainMana(livingEntity, manaDrain, false))
			stack.setDamageValue(stack.getDamageValue() - 1);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return slot == getEquipmentSlot() ? attributeModifiers.get() : super.getDefaultAttributeModifiers(slot);
	}
}
