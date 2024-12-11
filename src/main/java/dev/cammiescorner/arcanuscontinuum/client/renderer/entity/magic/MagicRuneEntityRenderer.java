package dev.cammiescorner.arcanuscontinuum.client.renderer.entity.magic;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.MagicRuneEntityModel;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.MagicRuneEntity;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;

public class MagicRuneEntityRenderer extends EntityRenderer<MagicRuneEntity> {
	private static final Identifier TEXTURE = Arcanus.id("textures/entity/magic/rune.png");
	private final MagicRuneEntityModel model;
	private final int[] keyFrames = {
			16, 16, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4,
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
			4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 16, 16
	};

	public MagicRuneEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
		model = new MagicRuneEntityModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(MagicRuneEntityModel.MODEL_LAYER));
	}

	@Override
	public void render(MagicRuneEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertices, int light) {
		super.render(entity, yaw, tickDelta, matrices, vertices, light);
		Color color = entity.getColor();
		float alpha = keyFrames[entity.age % keyFrames.length] / 16F;
		float r = color.redF() * alpha;
		float g = color.greenF() * alpha;
		float b = color.blueF() * alpha;
		color = Color.fromFloatsRGB(r, g, b);

		matrices.push();
		matrices.translate(0, Math.sin((entity.age + tickDelta) * 0.125) * 0.05, 0);
		matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(entity.age + tickDelta));
		model.render(matrices, vertices.getBuffer(ArcanusClient.getMagicCircles(TEXTURE)), LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, color.redF(), color.greenF(), color.blueF(), color.alphaF());
		matrices.pop();
	}

	@Override
	public Identifier getTexture(MagicRuneEntity entity) {
		return TEXTURE;
	}
}
