package dev.cammiescorner.arcanus.fabric.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricClient;
import dev.cammiescorner.arcanus.fabric.client.utils.ClientUtils;
import dev.cammiescorner.arcanus.fabric.common.items.StaffItem;
import dev.cammiescorner.arcanus.fabric.common.util.StaffType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class HeldItemRendererMixin {
	@Shadow @Final private Minecraft minecraft;
	@Shadow private ItemStack mainHandItem;
	@Shadow private float oMainHandHeight;
	@Shadow private float mainHandHeight;

	@Shadow
	protected abstract void renderPlayerArm(PoseStack matrices, MultiBufferSource vertexConsumers, int light, float equipProgress, float swingProgress, HumanoidArm arm);

	@Inject(method = "renderHandsWithItems", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
		ordinal = 0
	))
	private void arcanus$animateStaff(float tickDelta, PoseStack matrices, MultiBufferSource.BufferSource vertexConsumers, LocalPlayer player, int light, CallbackInfo info) {
		boolean isCasting = ((ClientUtils) minecraft).isCasting();

		if(minecraft.player != null && isCasting && mainHandItem.getItem() instanceof StaffItem item) {
			double time = minecraft.player.tickCount + tickDelta;

			if(item.staffType == StaffType.STAFF) {
				matrices.mulPose(Axis.XP.rotationDegrees(-65F));
				matrices.mulPose(Axis.YP.rotationDegrees((float) Math.cos(time * 0.25)));
				matrices.mulPose(Axis.ZP.rotationDegrees(20F + (float) Math.sin(time * 0.25)));
				matrices.translate(0.1, 1.2, -0.4);
			}
			else if(item.staffType == StaffType.BOOK && (!FabricClient.FIRST_PERSON_MODEL_ENABLED.getAsBoolean() || FabricClient.FIRST_PERSON_SHOW_HANDS.getAsBoolean())) {
				float swingProgress = player.getAttackAnim(tickDelta);
				float equipProgress = 1F - Mth.lerp(tickDelta, oMainHandHeight, mainHandHeight);

				matrices.pushPose();
				matrices.mulPose(Axis.YP.rotationDegrees((float) Math.cos(time * 0.25)));
				matrices.mulPose(Axis.ZP.rotationDegrees((float) Math.sin(time * 0.25)));
				renderPlayerArm(matrices, vertexConsumers, light, equipProgress, swingProgress, player.getMainArm().getOpposite());
				matrices.popPose();
			}
			else if(item.staffType == StaffType.WAND) {
				matrices.mulPose(Axis.XP.rotationDegrees(-65F));
				matrices.mulPose(Axis.YP.rotationDegrees((float) Math.cos(time * 0.25)));
				matrices.mulPose(Axis.ZP.rotationDegrees(20F + (float) Math.sin(time * 0.25)));
				matrices.translate(0.1, 1, -0.4);
			}
			else if(item.staffType == StaffType.GAUNTLET) {
				matrices.mulPose(Axis.YP.rotationDegrees((float) Math.cos(time * 0.25)));
				matrices.mulPose(Axis.ZP.rotationDegrees((float) Math.sin(time * 0.25)));
			}
			else if(item.staffType == StaffType.GUN) {
				matrices.mulPose(Axis.YP.rotationDegrees((float) Math.cos(time * 0.25)));
				matrices.mulPose(Axis.ZP.rotationDegrees((float) Math.sin(time * 0.25)));
				matrices.translate(-0.465, 0, 0);
			}
		}
	}
}
