package dev.cammiescorner.arcanuscontinuum.client.renderer.entity.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.AreaOfEffectModel;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.AreaOfEffect;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class AreaOfEffectEntityRenderer extends EntityRenderer<AreaOfEffect> {
	private static final ResourceLocation TEXTURE = Arcanus.id("textures/entity/magic/area_of_effect.png");
	private final AreaOfEffectModel model;

	public AreaOfEffectEntityRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		model = new AreaOfEffectModel(ctx.getModelSet().bakeLayer(AreaOfEffectModel.MODEL_LAYER));
	}

	@Override
	public void render(AreaOfEffect entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertices, int light) {
		super.render(entity, yaw, tickDelta, matrices, vertices, light);
		Color color = ArcanusHelper.getMagicColor(entity);
		float alpha = 1 - (Mth.clamp(entity.getTrueAge() - 80, 0, 20) / 20F);
		float r = color.redF() * alpha;
		float g = color.greenF() * alpha;
		float b = color.blueF() * alpha;
		color = Color.fromFloatsRGB(r, g, b);

		matrices.pushPose();
		matrices.mulPose(Axis.XP.rotationDegrees(180));
		matrices.translate(0, -1.51, 0);
		model.base.yRot = (entity.tickCount + tickDelta) * 0.015F;
		model.pillar.yRot = -model.base.yRot;
		model.walls.yRot = -(entity.tickCount + tickDelta) * 0.035F;
		model.renderToBuffer(matrices, vertices.getBuffer(ArcanusClient.getMagicCircles(TEXTURE)), light, OverlayTexture.NO_OVERLAY, color.redF(), color.greenF(), color.blueF(), color.alphaF());
		matrices.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(AreaOfEffect entity) {
		return TEXTURE;
	}
}
