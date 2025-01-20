package dev.cammiescorner.arcanuscontinuum.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusMobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@ModifyReturnValue(method = "getName", at = @At("RETURN"))
	private Component getName(Component original) {
		if(hasEffect(ArcanusMobEffects.ANONYMITY.get()))
			return Component.literal("Yog-Sothoth").withStyle(ChatFormatting.OBFUSCATED);

		return original;
	}

	@ModifyReturnValue(method = "getScoreboardName", at = @At("RETURN"))
	private String getEntityName(String original) {
		if(hasEffect(ArcanusMobEffects.ANONYMITY.get()))
			return "Yog-Sothoth";

		return original;
	}
}