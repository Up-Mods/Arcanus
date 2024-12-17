package dev.cammiescorner.arcanuscontinuum.client.renderer.block;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.SpatialRiftEntitySigilModel;
import dev.cammiescorner.arcanuscontinuum.common.blocks.entities.SpatialRiftExitBlockEntity;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.World;

public class SpatialRiftExitBlockEntityRenderer implements BlockEntityRenderer<SpatialRiftExitBlockEntity> {
	private static final RenderLayer LAYER = ArcanusClient.getMagicCircles(Arcanus.id("textures/entity/magic/spatial_rift_sigil.png"));
	private final SpatialRiftEntitySigilModel sigilModel;

	public SpatialRiftExitBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		sigilModel = new SpatialRiftEntitySigilModel(ctx.getLayerModelPart(SpatialRiftEntitySigilModel.MODEL_LAYER));
	}

	@Override
	public void render(SpatialRiftExitBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertices, int light, int overlay) {
		World world = entity.getWorld();

		if(world != null) {
			Color color = ArcanusHelper.getMagicColor(entity);
			float ageDelta = world.getTime() + tickDelta;

			matrices.push();
			matrices.translate(1.0F, 0.0F, 1.0F);
			matrices.scale(0.75F, 0.75F, 0.75F);
			sigilModel.sigil.yaw = ageDelta * 0.015F;
			sigilModel.render(matrices, vertices.getBuffer(LAYER), light, OverlayTexture.DEFAULT_UV, color.redF(), color.greenF(), color.blueF(), 1.0F);
			matrices.pop();
		}
	}
}
