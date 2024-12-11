package dev.cammiescorner.arcanuscontinuum.client.renderer.feature;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusStatusEffects;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class ManaWingsFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	private static final Identifier TEXTURE = Arcanus.id("/textures/entity/feature/mana_wings.png");
	private final ElytraEntityModel<T> model;

	public ManaWingsFeatureRenderer(FeatureRendererContext<T, M> context) {
		super(context);
		this.model = new ElytraEntityModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.ELYTRA));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertices, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if(entity.hasStatusEffect(ArcanusStatusEffects.MANA_WINGS.get())) {

			var color = ArcanusHelper.getMagicColor(entity);

			matrices.push();
			matrices.translate(0.0F, 0.0F, 0.125F);
			getContextModel().copyStateTo(model);
			model.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
			VertexConsumer layer = vertices.getBuffer(ArcanusClient.getMagicCircles(TEXTURE));
			model.render(matrices, layer, light, OverlayTexture.DEFAULT_UV, color.redF(), color.greenF(), color.blueF(), 1.0F);
			matrices.pop();
		}
	}
}
