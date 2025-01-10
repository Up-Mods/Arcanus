package dev.cammiescorner.arcanuscontinuum.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.renderer.feature.CounterFeatureRenderer;
import dev.cammiescorner.arcanuscontinuum.client.renderer.feature.ManaWingsFeatureRenderer;
import dev.cammiescorner.arcanuscontinuum.common.items.StaffItem;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.StaffType;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
	@Shadow
	protected abstract boolean addLayer(RenderLayer<T, M> feature);

	protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void arcanuscontinuum$init(EntityRendererProvider.Context ctx, EntityModel model, float shadowRadius, CallbackInfo ci) {
		addLayer(new ManaWingsFeatureRenderer<>(this));
		addLayer(new CounterFeatureRenderer<>(this));
	}

	@Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE",
																																											 target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
	))
	private void arcanuscontinuum$boltRenderer(T livingEntity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertices, int light, CallbackInfo info) {
		Vec3 offset = new Vec3(Mth.lerp(tickDelta, livingEntity.xOld, livingEntity.getX()), Mth.lerp(tickDelta, livingEntity.yOld, livingEntity.getY()), Mth.lerp(tickDelta, livingEntity.zOld, livingEntity.getZ())).add(getRenderOffset(livingEntity, tickDelta));

		matrices.pushPose();
		matrices.translate(-offset.x(), -offset.y(), -offset.z());
		ArcanusClient.renderBolts(livingEntity, livingEntity.getPosition(tickDelta).add(0, livingEntity.getEyeHeight(livingEntity.getPose()) * 0.9, 0), matrices, vertices);
		matrices.popPose();
	}

	@Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE",
																																											 target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getAttackAnim(Lnet/minecraft/world/entity/LivingEntity;F)F"
	))
	private void arcanuscontinuum$render(T livingEntity, float f, float g, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo info) {
		if(livingEntity instanceof Player player && ArcanusComponents.CASTING_COMPONENT.isProvidedBy(livingEntity) && livingEntity.getMainHandItem().getItem() instanceof StaffItem item && item.staffType == StaffType.STAFF && ArcanusComponents.isCasting(livingEntity))
			matrices.mulPose(Axis.YP.rotationDegrees(player.getMainArm() == HumanoidArm.RIGHT ? 65 : -65));
	}
}
