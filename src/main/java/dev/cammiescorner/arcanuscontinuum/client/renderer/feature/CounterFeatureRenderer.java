package dev.cammiescorner.arcanuscontinuum.client.renderer.feature;

import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class CounterFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	private static final Identifier TEXTURE = new Identifier("textures/misc/white.png");
	private final EntityModel<T> model;

	public CounterFeatureRenderer(FeatureRendererContext<T, M> context) {
		super(context);
		model = context.getModel();
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider verticies, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if(ArcanusComponents.isCounterActive(entity)) {
			Color color = ArcanusComponents.getCounterColor(entity);
			long endTime = ArcanusComponents.getCounterEnd(entity);
			long timer = endTime - entity.getWorld().getTime();
			float alpha = timer > 20 ? 1f : timer / 20f;
			float r = color.redF() * alpha;
			float g = color.greenF() * alpha;
			float b = color.blueF() * alpha;

			matrices.push();
			matrices.scale(1.1F, 1.1F, 1.1F);
			model.render(matrices, verticies.getBuffer(ArcanusClient.getMagicCircles(TEXTURE)), light, OverlayTexture.DEFAULT_UV, r, g, b, 1.0F);
			matrices.pop();
		}
	}
}
