package dev.cammiescorner.arcanuscontinuum.client.renderer.entity.magic;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.AreaOfEffectEntityModel;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.AreaOfEffectEntity;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.MathHelper;

public class AreaOfEffectEntityRenderer extends EntityRenderer<AreaOfEffectEntity> {
	private static final Identifier TEXTURE = Arcanus.id("textures/entity/magic/area_of_effect.png");
	private final AreaOfEffectEntityModel model;

	public AreaOfEffectEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
		model = new AreaOfEffectEntityModel(ctx.getModelLoader().getModelPart(AreaOfEffectEntityModel.MODEL_LAYER));
	}

	@Override
	public void render(AreaOfEffectEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertices, int light) {
		super.render(entity, yaw, tickDelta, matrices, vertices, light);
		Color color = ArcanusHelper.getMagicColor(entity);
		float alpha = 1 - (MathHelper.clamp(entity.getTrueAge() - 80, 0, 20) / 20F);
		float r = color.redF() * alpha;
		float g = color.greenF() * alpha;
		float b = color.blueF() * alpha;
		color = Color.fromFloatsRGB(r, g, b);

		matrices.push();
		matrices.multiply(Axis.X_POSITIVE.rotationDegrees(180));
		matrices.translate(0, -1.51, 0);
		model.base.yaw = (entity.age + tickDelta) * 0.015F;
		model.pillar.yaw = -model.base.yaw;
		model.walls.yaw = -(entity.age + tickDelta) * 0.035F;
		model.render(matrices, vertices.getBuffer(ArcanusClient.getMagicCircles(TEXTURE)), light, OverlayTexture.DEFAULT_UV, color.redF(), color.greenF(), color.blueF(), color.alphaF());
		matrices.pop();
	}

	@Override
	public Identifier getTexture(AreaOfEffectEntity entity) {
		return TEXTURE;
	}
}
