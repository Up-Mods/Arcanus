package dev.cammiescorner.arcanuscontinuum.mixin.common;

import dev.cammiescorner.arcanuscontinuum.common.items.BattleMageArmorItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStand.class)
public abstract class ArmorStandEntityMixin extends LivingEntity {
	@Shadow protected abstract EquipmentSlot getClickedSlot(Vec3 hitPos);

	protected ArmorStandEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "interactAt", at = @At("HEAD"), cancellable = true)
	private void arcanuscontinuum$waxOnWaxOff(Player player, Vec3 hitPos, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
		EquipmentSlot slot = getClickedSlot(hitPos);
		ItemStack stack = getItemBySlot(slot);
		ItemStack heldStack = player.getItemInHand(hand);

		if(stack.getItem() instanceof BattleMageArmorItem) {
			if(heldStack.is(ItemTags.AXES)) {
				if(BattleMageArmorItem.isWaxed(stack)) {
					level().playSound(player, blockPosition(), SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1f, 1f);
					BattleMageArmorItem.setWaxed(stack, false);
					heldStack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(hand));
					info.setReturnValue(InteractionResult.SUCCESS);
					return;
				}

				if(BattleMageArmorItem.getOxidation(stack) != WeatheringCopper.WeatherState.UNAFFECTED) {
					level().playSound(player, blockPosition(), SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1f, 1f);
					BattleMageArmorItem.setOxidation(stack, WeatheringCopper.WeatherState.values()[BattleMageArmorItem.getOxidation(stack).ordinal() - 1]);
					heldStack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(hand));
					info.setReturnValue(InteractionResult.SUCCESS);
					return;
				}
			}

			if(heldStack.getItem() == Items.HONEYCOMB && !BattleMageArmorItem.isWaxed(stack)) {
				level().playSound(player, blockPosition(), SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1f, 1f);
				BattleMageArmorItem.setWaxed(stack, true);
				heldStack.shrink(1);
				info.setReturnValue(InteractionResult.SUCCESS);
			}
		}
	}
}
