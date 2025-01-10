package dev.cammiescorner.arcanuscontinuum.common.items;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.spells.Spell;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.cammiescorner.arcanuscontinuum.common.util.StaffType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Supplier;

public class StaffItem extends Item {
	public static final UUID ATTACK_RANGE_MODIFIER_ID = UUID.fromString("05869d86-c861-4954-9079-68c380ad063c");
	private final Supplier<Multimap<Attribute, AttributeModifier>> attributeModifiers;
	public final StaffType staffType;
	public final Color defaultPrimaryColor;
	public final Color defaultSecondaryColor;
	public final boolean isDonorOnly;

	public StaffItem(StaffType staffType, Color defaultPrimaryColor, Color defaultSecondaryColor) {
		this(staffType, defaultPrimaryColor, defaultSecondaryColor, false);
	}

	public StaffItem(StaffType staffType, Color defaultPrimaryColor, Color defaultSecondaryColor, boolean isDonorOnly) {
		super(new Item.Properties().stacksTo(1));
		this.attributeModifiers = Suppliers.memoize(() -> ImmutableMultimap.<Attribute, AttributeModifier>builder()
			.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -1, AttributeModifier.Operation.ADDITION))
			.put(ReachEntityAttributes.ATTACK_RANGE, new AttributeModifier(ATTACK_RANGE_MODIFIER_ID, "Weapon modifier", 0.5, AttributeModifier.Operation.ADDITION))
			.build()
		);
		this.staffType = staffType;
		this.defaultPrimaryColor = defaultPrimaryColor;
		this.defaultSecondaryColor = defaultSecondaryColor;
		this.isDonorOnly = isDonorOnly;
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level world, Player player) {
		if(!world.isClientSide()) {
			CompoundTag tag = stack.getOrCreateTagElement(Arcanus.MOD_ID);

			if(tag.isEmpty()) {
				ListTag list = new ListTag();

				for(int i = 0; i < 8; i++)
					list.add(i, new Spell().toNbt());

				tag.put("Spells", list);
			}
		}

		super.onCraftedBy(stack, world, player);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if(!world.isClientSide()) {
			CompoundTag tag = stack.getOrCreateTagElement(Arcanus.MOD_ID);

			if(tag.isEmpty()) {
				ListTag list = new ListTag();

				for(int i = 0; i < 8; i++)
					list.add(i, new Spell().toNbt());

				tag.put("Spells", list);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
		CompoundTag tag = stack.getTagElement(Arcanus.MOD_ID);
		int primaryColour = getPrimaryColorRGB(stack);
		int secondaryColour = getSecondaryColorRGB(stack);

		tooltip.add(Component.translatable("staff.arcanuscontinuum.primary_color").withStyle(style -> style.withColor(primaryColour)).append(Component.literal(": " + String.format(Locale.ROOT, "#%06X", primaryColour)).withStyle(ChatFormatting.GRAY)));
		tooltip.add(Component.translatable("staff.arcanuscontinuum.secondary_color").withStyle(style -> style.withColor(secondaryColour)).append(Component.literal(": " + String.format(Locale.ROOT, "#%06X", secondaryColour)).withStyle(ChatFormatting.GRAY)));
		tooltip.add(Component.empty());

		if(tag != null && !tag.isEmpty()) {
			ListTag list = tag.getList("Spells", Tag.TAG_COMPOUND);

			for(int i = 0; i < list.size(); i++) {
				Spell spell = Spell.fromNbt(list.getCompound(i));

				if(spell.getComponentGroups().isEmpty()) {
					tooltip.add(Component.translatable("staff.arcanuscontinuum.invalid_data").withStyle(ChatFormatting.DARK_RED));
					return;
				}

				MutableComponent text = Component.literal(spell.getName()).withStyle(spell.isEmpty() ? ChatFormatting.GRAY : ChatFormatting.GREEN);
				tooltip.add(text.append(Component.literal(" (").withStyle(ChatFormatting.DARK_GRAY)).append(Arcanus.getSpellPatternAsText(i).withStyle(ChatFormatting.GRAY)).append(Component.literal(")").withStyle(ChatFormatting.DARK_GRAY)));
			}
		}
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player miner) {
		return false;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? attributeModifiers.get() : super.getDefaultAttributeModifiers(slot);
	}

	public static void setPrimaryColor(ItemStack stack, Color color) {
		stack.getOrCreateTagElement(Arcanus.MOD_ID).putInt("PrimaryColor", color.asInt(Color.Ordering.RGB));
	}

	public static Color getPrimaryColor(ItemStack stack) {
		var color = ((StaffItem) stack.getItem()).defaultPrimaryColor;
		var tag = stack.getTagElement(Arcanus.MOD_ID);

		if(tag != null && tag.contains("PrimaryColor", Tag.TAG_INT)) {
			color = Color.fromInt(tag.getInt("PrimaryColor"), Color.Ordering.RGB);
		}

		return color;
	}

	public static int getPrimaryColorRGB(ItemStack stack) {
		return getPrimaryColor(stack).asInt(Color.Ordering.RGB);
	}

	public static void setSecondaryColor(ItemStack stack, Color color) {
		stack.getOrCreateTagElement(Arcanus.MOD_ID).putInt("SecondaryColor", color.asInt(Color.Ordering.RGB));
	}

	public static Color getSecondaryColor(ItemStack stack) {
		var color = ((StaffItem) stack.getItem()).defaultSecondaryColor;
		var tag = stack.getTagElement(Arcanus.MOD_ID);
		if(tag != null && tag.contains("SecondaryColor", Tag.TAG_INT)) {
			color = Color.fromInt(tag.getInt("SecondaryColor"), Color.Ordering.RGB);
		}

		return color;
	}

	public static int getSecondaryColorRGB(ItemStack stack) {
		return getSecondaryColor(stack).asInt(Color.Ordering.RGB);
	}

	public static ItemStack setCraftedBy(ItemStack stack, UUID uuid) {
		CompoundTag tag = stack.getOrCreateTagElement(Arcanus.MOD_ID);
		tag.putUUID("OwnerId", uuid);
		return stack;
	}
}
