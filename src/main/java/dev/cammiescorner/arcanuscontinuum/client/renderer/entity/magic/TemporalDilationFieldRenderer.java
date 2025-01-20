package dev.cammiescorner.arcanuscontinuum.client.renderer.entity.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.TemporalDilationFieldModel;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.TemporalDilationField;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class TemporalDilationFieldRenderer extends EntityRenderer<TemporalDilationField> {
	private static final ResourceLocation TEXTURE = Arcanus.id("textures/entity/magic/temporal_dilation_field.png");
	private final TemporalDilationFieldModel model;

	public TemporalDilationFieldRenderer(EntityRendererProvider.Context context) {
		super(context);
		model = new TemporalDilationFieldModel(context.getModelSet().bakeLayer(TemporalDilationFieldModel.MODEL_LAYER));
	}

	@Override
	public void render(TemporalDilationField entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
		Minecraft client = Minecraft.getInstance();
		Color color = ArcanusHelper.getMagicColor(entity);
		Vec3 cameraPos = client.getCameraEntity() != null ? new Vec3(client.getCameraEntity().getX(), 0, client.getCameraEntity().getZ()) : Vec3.ZERO;
		Vec3 fieldPos = new Vec3(entity.getX(), 0, entity.getZ());
		Vec3 directionToCamera = cameraPos.subtract(fieldPos);
		float scale = (float) (entity.getBoundingBox().getXsize() / 3f);
		float handProgress = (float) Math.toRadians((360f / entity.getMaxAge()) * entity.getAge() + partialTick);

		poseStack.pushPose();
		poseStack.mulPose(Axis.XP.rotationDegrees(180));
		poseStack.scale(scale, scale, scale);
		poseStack.translate(0, -1.51, 0);

		model.xPlaneRing.yRot = (float) Mth.atan2(directionToCamera.z(), directionToCamera.x());
		model.yPlaneRing.yRot = (float) (Math.toRadians(-90) + Mth.atan2(directionToCamera.z(), directionToCamera.x()));
		model.xClockHand.xRot = -handProgress;
		model.yClockHand.yRot = handProgress;

		model.renderToBuffer(poseStack, buffer.getBuffer(ArcanusClient.getMagicCircles(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, color.redF(), color.greenF(), color.blueF(), color.alphaF());
		poseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(TemporalDilationField entity) {
		return TEXTURE;
	}
}
