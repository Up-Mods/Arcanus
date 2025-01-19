package dev.cammiescorner.arcanuscontinuum.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FoodData.class)
public class FoodDataMixin {
	@WrapOperation(method = "tick", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z",
		ordinal = 0
	))
	public boolean hasBurnout(GameRules instance, GameRules.Key<GameRules.BooleanValue> gameRuleKey, Operation<Boolean> original, Player player) {
		return original.call(instance, gameRuleKey) && ArcanusComponents.getBurnout(player) <= 0.0D;
	}
}
