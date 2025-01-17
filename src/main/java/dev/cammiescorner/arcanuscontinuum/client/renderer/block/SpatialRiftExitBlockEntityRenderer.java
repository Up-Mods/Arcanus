package dev.cammiescorner.arcanuscontinuum.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.SpatialRiftEntitySigilModel;
import dev.cammiescorner.arcanuscontinuum.common.blocks.entities.SpatialRiftExitBlockEntity;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.Level;

public class SpatialRiftExitBlockEntityRenderer implements BlockEntityRenderer<SpatialRiftExitBlockEntity> {
	private static final RenderType LAYER = ArcanusClient.getMagicCircles(Arcanus.id("textures/entity/magic/spatial_rift_sigil.png"));
	private final SpatialRiftEntitySigilModel sigilModel;

	public SpatialRiftExitBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		sigilModel = new SpatialRiftEntitySigilModel(ctx.bakeLayer(SpatialRiftEntitySigilModel.MODEL_LAYER));
	}

	@Override
	public void render(SpatialRiftExitBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertices, int light, int overlay) {
		Level world = entity.getLevel();

		if(world != null) {
			Color color = ArcanusHelper.getMagicColor(entity);
			float ageDelta = world.getGameTime() + tickDelta;

			matrices.pushPose();
			matrices.translate(1.0F, 0.0F, 1.0F);
			matrices.scale(0.75F, 0.75F, 0.75F);
			sigilModel.sigil.yRot = ageDelta * 0.015F;
			sigilModel.renderToBuffer(matrices, vertices.getBuffer(LAYER), light, OverlayTexture.NO_OVERLAY, color.redF(), color.greenF(), color.blueF(), 1.0F);
			matrices.popPose();
		}
	}
}
