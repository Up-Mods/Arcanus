package dev.cammiescorner.arcanus.fabric.mixin.common;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public abstract class MilkBucketItemMixin extends Item {
	public MilkBucketItemMixin(Properties settings) {
		super(settings);
	}

	@Inject(method = "finishUsingItem", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;removeAllEffects()Z"
	))
	private void arcanus$dontClearCopperCurse(ItemStack stack, Level world, LivingEntity user, CallbackInfoReturnable<ItemStack> info, @Share("copperCurse") LocalRef<MobEffectInstance> copperCurse) {
		if(user.hasEffect(ArcanusMobEffects.COPPER_CURSE.get()))
			copperCurse.set(user.getEffect(ArcanusMobEffects.COPPER_CURSE.get()));
	}

	@Inject(method = "finishUsingItem", at = @At(
			value = "INVOKE_ASSIGN",
			target = "Lnet/minecraft/world/entity/LivingEntity;removeAllEffects()Z"
	))
	private void arcanus$dontClearCopperCurse2(ItemStack stack, Level world, LivingEntity user, CallbackInfoReturnable<ItemStack> info, @Share("copperCurse") LocalRef<MobEffectInstance> copperCurse) {
		var curse = copperCurse.get();

		if(curse != null)
			user.addEffect(curse);
	}
}
