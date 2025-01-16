package dev.cammiescorner.arcanus.fabric.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.fabric.common.util.Color;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricClient;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class CounterFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/misc/white.png");
	private final EntityModel<T> model;

	public CounterFeatureRenderer(RenderLayerParent<T, M> context) {
		super(context);
		model = context.getModel();
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource verticies, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if(ArcanusComponents.isCounterActive(entity)) {
			Color color = ArcanusComponents.getCounterColor(entity);
			long endTime = ArcanusComponents.getCounterEnd(entity);
			long timer = endTime - entity.level().getGameTime();
			float alpha = timer > 20 ? 1f : timer / 20f;
			float r = color.redF() * alpha;
			float g = color.greenF() * alpha;
			float b = color.blueF() * alpha;

			matrices.pushPose();
			matrices.scale(1.1F, 1.1F, 1.1F);
			model.renderToBuffer(matrices, verticies.getBuffer(FabricClient.getMagicCircles(TEXTURE)), light, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
			matrices.popPose();
		}
	}
}
