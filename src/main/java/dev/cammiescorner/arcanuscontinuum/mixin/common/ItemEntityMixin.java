package dev.cammiescorner.arcanuscontinuum.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusItemTags;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusMobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements TraceableEntity {
	public ItemEntityMixin(EntityType<?> variant, Level world) {
		super(variant, world);
	}

	@WrapOperation(method = "playerTouch", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"
	))
	private boolean arcanuscontinuum$copperCurse(Inventory instance, ItemStack stack, Operation<Boolean> original, Player player) {
		if(player.hasEffect(ArcanusMobEffects.COPPER_CURSE.get()) && !stack.is(ArcanusItemTags.COPPER_CURSE_IMMUNE) && stack.getItem() != Items.RAW_COPPER && (instance.getSlotWithRemainingSpace(stack) >= 0 || instance.getFreeSlot() >= 0)) {
			int originalStackCount = stack.getCount();

			for(int i = 0; i < originalStackCount; i++)
				if(random.nextFloat() < ArcanusConfig.AttackEffects.CopperCurseEffectProperties.baseChanceToActivate)
					stack.shrink(1);

			if(originalStackCount - stack.getCount() > 0)
				instance.placeItemBackInInventory(new ItemStack(Items.RAW_COPPER, originalStackCount - stack.getCount()));
		}

		return original.call(instance, stack);
	}
}
