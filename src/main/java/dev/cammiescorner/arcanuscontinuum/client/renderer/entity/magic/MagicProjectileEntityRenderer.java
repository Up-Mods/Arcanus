package dev.cammiescorner.arcanuscontinuum.client.renderer.entity.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.MagicLobModel;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.MagicProjectileModel;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.MagicProjectile;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MagicProjectileEntityRenderer extends ArrowRenderer<MagicProjectile> {
	private static final ResourceLocation PROJECTILE_TEXTURE = Arcanus.id("textures/entity/magic/projectile.png");
	private static final ResourceLocation LOB_TEXTURE = Arcanus.id("textures/entity/magic/lob.png");
	private final MagicLobModel lobModel;
	private final MagicProjectileModel projectileModel;

	public MagicProjectileEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		lobModel = new MagicLobModel(context.getModelSet().bakeLayer(MagicLobModel.MODEL_LAYER));
		projectileModel = new MagicProjectileModel(context.getModelSet().bakeLayer(MagicProjectileModel.MODEL_LAYER));
	}

	@Override
	public void render(MagicProjectile entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertices, int light) {
		VertexConsumer consumer = vertices.getBuffer(ArcanusClient.getMagicCircles(getTextureLocation(entity)));
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
	public ResourceLocation getTextureLocation(MagicProjectile entity) {
		return ArcanusSpellComponents.PROJECTILE.is(entity.getShape()) ? PROJECTILE_TEXTURE : LOB_TEXTURE;
	}
}
