package dev.cammiescorner.arcanuscontinuum.client.renderer.entity.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.EntangledOrbModel;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.EntangledOrb;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class EntangledOrbEntityRenderer extends EntityRenderer<EntangledOrb> {
	private static final ResourceLocation TEXTURE = Arcanus.id("textures/entity/magic/lob.png");
	private final EntangledOrbModel model;

	public EntangledOrbEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		model = new EntangledOrbModel(context.getModelSet().bakeLayer(EntangledOrbModel.MODEL_LAYER));
	}

	@Override
	public void render(EntangledOrb entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertices, int light) {
		VertexConsumer consumer = vertices.getBuffer(ArcanusClient.getMagicCircles(getTextureLocation(entity)));
		Color color = ArcanusHelper.getMagicColor(entity);

		matrices.pushPose();
		matrices.translate(0, 0.2, 0);
		model.cube1.xRot = (entity.tickCount + tickDelta) * 0.1F;
		model.cube1.yRot = (entity.tickCount + tickDelta) * 0.1F;
		model.cube2.yRot = -(entity.tickCount + tickDelta) * 0.125F;
		model.cube2.zRot = -(entity.tickCount + tickDelta) * 0.125F;
		model.cube3.zRot = (entity.tickCount + tickDelta) * 0.15F;
		model.cube3.xRot = (entity.tickCount + tickDelta) * 0.15F;
		model.renderToBuffer(matrices, consumer, light, OverlayTexture.NO_OVERLAY, color.redF(), color.greenF(), color.blueF(), 1.0F);
		matrices.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntangledOrb entity) {
		return TEXTURE;
	}
}
