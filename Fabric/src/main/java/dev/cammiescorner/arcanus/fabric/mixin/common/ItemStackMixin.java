package dev.cammiescorner.arcanus.fabric.mixin.common;

import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.cammiescorner.arcanus.api.entities.ArcanusEntityAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Inject(method = "getTooltipLines", at = @At(
			value = "INVOKE",
			target = "Ljava/util/Map$Entry;getValue()Ljava/lang/Object;"
	), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void arcanus$captureEntry(Player player, TooltipFlag context, CallbackInfoReturnable<List<Component>> cir, List list, MutableComponent mutableText, int i, EquipmentSlot[] var6, int var7, int var8, EquipmentSlot equipmentSlot, Multimap multimap, Iterator var11, Map.Entry<Attribute, AttributeModifier> entry, @Share("entry") LocalRef<Map.Entry<Attribute, AttributeModifier>> ref) {
		ref.set(entry);
	}

	@ModifyArg(method = "getTooltipLines", slice = @Slice(from = @At(
			value = "FIELD",
			target = "Lnet/minecraft/ChatFormatting;BLUE:Lnet/minecraft/ChatFormatting;"
	)), at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;"
	))
	private ChatFormatting arcanus$switchColour(ChatFormatting formatting, @Share("entry") LocalRef<Map.Entry<Attribute, AttributeModifier>> ref) {
		return ref.get() != null && (ref.get().getKey() == ArcanusEntityAttributes.MANA_COST.get() || ref.get().getKey() == ArcanusEntityAttributes.SPELL_COOL_DOWN.get()) ? (formatting == ChatFormatting.BLUE ? ChatFormatting.RED : ChatFormatting.BLUE) : formatting;
	}
}
