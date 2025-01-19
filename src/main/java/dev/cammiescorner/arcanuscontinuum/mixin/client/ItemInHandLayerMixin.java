package dev.cammiescorner.arcanuscontinuum.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanuscontinuum.common.items.StaffItem;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.StaffType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {
	@Inject(method = "renderArmWithItem", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
	))
	private void adjustItem(LivingEntity entity, ItemStack stack, ItemDisplayContext transformationMode, HumanoidArm arm, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
		if(ArcanusComponents.CASTING_COMPONENT.isProvidedBy(entity) && entity.getMainHandItem().getItem() instanceof StaffItem item && ArcanusComponents.isCasting(entity)) {
			if(item.staffType == StaffType.STAFF) {
				matrices.translate(0, 0.1, 0);
				matrices.mulPose(Axis.XP.rotationDegrees(-15));
			}
			else if(item.staffType == StaffType.GUN) {
				matrices.mulPose(Axis.YP.rotationDegrees(-45));
				matrices.translate(0.125, 0, 0);
			}
			else if(item.staffType == StaffType.WAND) {
				matrices.translate(0, -0.2, 0);
				matrices.mulPose(Axis.XP.rotationDegrees(-65));
			}
		}
	}
}
