package dev.cammiescorner.arcanus.fabric.client.renderer.entity.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricClient;
import dev.cammiescorner.arcanus.fabric.client.models.entity.magic.MagicLobEntityModel;
import dev.cammiescorner.arcanus.fabric.client.models.entity.magic.MagicProjectileEntityModel;
import dev.cammiescorner.arcanus.fabric.common.entities.magic.MagicProjectileEntity;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusSpellComponents;
import dev.cammiescorner.arcanus.fabric.common.util.ArcanusHelper;
import dev.cammiescorner.arcanus.fabric.common.util.Color;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MagicProjectileEntityRenderer extends ArrowRenderer<MagicProjectileEntity> {
	private static final ResourceLocation PROJECTILE_TEXTURE = FabricMain.id("textures/entity/magic/projectile.png");
	private static final ResourceLocation LOB_TEXTURE = FabricMain.id("textures/entity/magic/lob.png");
	private final MagicLobEntityModel lobModel;
	private final MagicProjectileEntityModel projectileModel;

	public MagicProjectileEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		lobModel = new MagicLobEntityModel(context.getModelSet().bakeLayer(MagicLobEntityModel.MODEL_LAYER));
		projectileModel = new MagicProjectileEntityModel(context.getModelSet().bakeLayer(MagicProjectileEntityModel.MODEL_LAYER));
	}

	@Override
	public void render(MagicProjectileEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertices, int light) {
		VertexConsumer consumer = vertices.getBuffer(FabricClient.getMagicCircles(getTextureLocation(entity)));
		boolean isProjectile = ArcanusSpellComponents.PROJECTILE.is(entity.getShape());
		Color color = ArcanusHelper.getMagicColor(entity);

		matrices.pushPose();

		if(isProjectile) {
			matrices.mulPose(Axis.YP.rotationDegrees(Mth.lerp(tickDelta, entity.yRotO, entity.getYRot()) - 180.0F));
			matrices.mulPose(Axis.XP.rotationDegrees(Mth.lerp(tickDelta, entity.xRotO, entity.getXRot())));
			matrices.translate(0.0F, -1.0F, 0.0F);
			projectileModel.ring1.zRot = (entity.tickCount + tickDelta) * 0.1F;
			projectileModel.ring2.zRot = -(entity.tickCount + tickDelta) * 0.125F;
			projectileModel.ring3.zRot = (entity.tickCount + tickDelta) * 0.15F;
			projectileModel.renderToBuffer(matrices, consumer, light, OverlayTexture.NO_OVERLAY, color.redF(), color.greenF(), color.blueF(), 1.0F);
		}
		else {
			matrices.translate(0, 0.3, 0);
			lobModel.cube1.xRot = (entity.tickCount + tickDelta) * 0.1F;
			lobModel.cube1.yRot = (entity.tickCount + tickDelta) * 0.1F;
			lobModel.cube2.yRot = -(entity.tickCount + tickDelta) * 0.125F;
			lobModel.cube2.zRot = -(entity.tickCount + tickDelta) * 0.125F;
			lobModel.cube3.zRot = (entity.tickCount + tickDelta) * 0.15F;
			lobModel.cube3.xRot = (entity.tickCount + tickDelta) * 0.15F;
			lobModel.renderToBuffer(matrices, consumer, light, OverlayTexture.NO_OVERLAY, color.redF(), color.greenF(), color.blueF(), 1.0F);
		}

		matrices.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(MagicProjectileEntity entity) {
		return ArcanusSpellComponents.PROJECTILE.is(entity.getShape()) ? PROJECTILE_TEXTURE : LOB_TEXTURE;
	}
}
