package dev.cammiescorner.arcanuscontinuum.client.renderer.entity.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.MagicRuneModel;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.MagicRune;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class MagicRuneEntityRenderer extends EntityRenderer<MagicRune> {
	private static final ResourceLocation TEXTURE = Arcanus.id("textures/entity/magic/rune.png");
	private final MagicRuneModel model;
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

	public MagicRuneEntityRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		model = new MagicRuneModel(Minecraft.getInstance().getEntityModels().bakeLayer(MagicRuneModel.MODEL_LAYER));
	}

	@Override
	public void render(MagicRune entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertices, int light) {
		super.render(entity, yaw, tickDelta, matrices, vertices, light);
		Color color = ArcanusHelper.getMagicColor(entity);
		float alpha = keyFrames[entity.tickCount % keyFrames.length] / 16F;
		float r = color.redF() * alpha;
		float g = color.greenF() * alpha;
		float b = color.blueF() * alpha;
		color = Color.fromFloatsRGB(r, g, b);

		matrices.pushPose();
		matrices.translate(0, Math.sin((entity.tickCount + tickDelta) * 0.125) * 0.05, 0);
		matrices.mulPose(Axis.YP.rotationDegrees(entity.tickCount + tickDelta));
		model.renderToBuffer(matrices, vertices.getBuffer(ArcanusClient.getMagicCircles(TEXTURE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color.redF(), color.greenF(), color.blueF(), color.alphaF());
		matrices.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(MagicRune entity) {
		return TEXTURE;
	}
}
