package dev.cammiescorner.arcanuscontinuum.client.renderer.entity.magic;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.PocketDimensionPortalModel;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.SpatialRiftSigilModel;
import dev.cammiescorner.arcanuscontinuum.client.utils.StencilBuffer;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.PocketDimensionPortal;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL31;

public class PocketDimensionPortalEntityRenderer extends EntityRenderer<PocketDimensionPortal> {
	private static final ResourceLocation PORTAL_TEXTURE = Arcanus.id("textures/entity/magic/pocket_dimension_portal.png");
	private static final ResourceLocation SIGIL_TEXTURE = Arcanus.id("textures/entity/magic/spatial_rift_sigil.png");
	private final Minecraft client = Minecraft.getInstance();
	private final Tesselator tesselator = Tesselator.getInstance();
	private final PocketDimensionPortalModel portalModel;
	private final SpatialRiftSigilModel sigilModel;

	public PocketDimensionPortalEntityRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		portalModel = new PocketDimensionPortalModel(ctx.getModelSet().bakeLayer(PocketDimensionPortalModel.MODEL_LAYER));
		sigilModel = new SpatialRiftSigilModel(ctx.getModelSet().bakeLayer(SpatialRiftSigilModel.MODEL_LAYER));
	}

	@Override
	public void render(PocketDimensionPortal entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertices, int light) {
		super.render(entity, yaw, tickDelta, matrices, vertices, light);
		StencilBuffer stencilBuffer = ((StencilBuffer) client.getMainRenderTarget());
		RenderType portalLayer = ArcanusClient.getMagicPortal(PORTAL_TEXTURE);
		RenderType sigilLayer = ArcanusClient.getMagicCircles(SIGIL_TEXTURE);
		Color pocketDimColor = ArcanusHelper.getPocketDimensionColor(entity);
		Color magicColor = ArcanusHelper.getMagicColor(entity);
		float ageDelta = entity.getTrueAge() + tickDelta;
		float maxScale = 0.75f;
		float scale = entity.getTrueAge() <= 100 ? Math.min(maxScale, (ageDelta / 100f) * maxScale) : entity.getTrueAge() > 700 ? Math.max(0, (1 - (ageDelta - 700) / 20f) * maxScale) : maxScale;

		matrices.pushPose();
		matrices.translate(0, 1.625, 0);
		matrices.mulPose(Axis.ZP.rotationDegrees(180));
		matrices.scale(scale, 1, scale);
		portalModel.skybox.render(matrices, vertices.getBuffer(RenderType.entitySolid(PORTAL_TEXTURE)), light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
		matrices.popPose();

		matrices.pushPose();
		matrices.translate(0, 0.05, 0);
		matrices.scale(scale, 1, scale);
		matrices.mulPose(Axis.ZP.rotationDegrees(90));

		if(!stencilBuffer.arcanus$isStencilBufferEnabled())
			stencilBuffer.arcanus$enableStencilBufferAndReload(true);

		GL31.glEnable(GL31.GL_STENCIL_TEST);

		GL31.glDepthMask(false);
		GL31.glColorMask(false, false, false, true);
		GL31.glStencilOp(GL31.GL_KEEP, GL31.GL_KEEP, GL31.GL_INCR);
		GL31.glStencilFunc(GL31.GL_EQUAL, 0, 0xFF);
		GL31.glStencilMask(0xFF);

		RenderType.waterMask().setupRenderState();
		GameRenderer.getPositionShader().apply();
		GL31.glColorMask(true, false, false, true);
		GL31.glDepthFunc(GL31.GL_LEQUAL);
		drawStencil(matrices, tesselator);
		GameRenderer.getPositionShader().clear();
		RenderType.waterMask().clearRenderState();

		GL31.glColorMask(true, true, true, true);
		GL31.glStencilFunc(GL31.GL_NOTEQUAL, 0, 0xFF);
		GL31.glStencilMask(0x00);

		matrices.pushPose();
		matrices.translate(-0.375, 0, 0);
		matrices.mulPose(Axis.ZP.rotationDegrees(90));
		matrices.scale(maxScale, maxScale, maxScale);
		portalModel.renderToBuffer(matrices, vertices.getBuffer(portalLayer), light, OverlayTexture.NO_OVERLAY, pocketDimColor.redF(), pocketDimColor.greenF(), pocketDimColor.blueF(), 1.0F);
		matrices.popPose();

		if(vertices instanceof MultiBufferSource.BufferSource immediate) {
			immediate.endBatch();
			GL31.glDepthMask(true);
		}

		GL31.glColorMask(false, false, false, true);
		GL31.glStencilOp(GL31.GL_KEEP, GL31.GL_KEEP, GL31.GL_DECR);
		GL31.glStencilFunc(GL31.GL_EQUAL, 1, 0xFF);
		GL31.glStencilMask(0xFF);

		RenderType.waterMask().setupRenderState();
		GameRenderer.getPositionShader().apply();
		GL31.glDepthFunc(GL31.GL_ALWAYS);
		drawStencil(matrices, tesselator);
		GameRenderer.getPositionShader().clear();
		RenderType.waterMask().clearRenderState();

		GL31.glStencilFunc(GL31.GL_EQUAL, 0, 0xFF);
		GL31.glStencilMask(0x00);

		GL31.glDisable(GL31.GL_STENCIL_TEST);
		GL31.glEnable(GL31.GL_DEPTH_TEST);
		GL31.glDepthFunc(GL31.GL_LEQUAL);

		matrices.popPose();

		matrices.pushPose();
		matrices.translate(0.0D, 1.51D, 0.0D);
		matrices.mulPose(Axis.XP.rotationDegrees(180.0F));
		matrices.scale(scale / maxScale, 1.0F, scale / maxScale);
		sigilModel.sigil.yRot = (entity.tickCount + tickDelta) * 0.015F;
		sigilModel.renderToBuffer(matrices, vertices.getBuffer(sigilLayer), light, OverlayTexture.NO_OVERLAY, magicColor.redF(), magicColor.greenF(), magicColor.blueF(), 1.0F);
		matrices.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(PocketDimensionPortal entity) {
		return PORTAL_TEXTURE;
	}

	public static void drawStencil(PoseStack matrices, Tesselator tessellator) {
		BufferBuilder builder = tessellator.getBuilder();
		builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
		builder.vertex(matrices.last().pose(), 0, -1, -1).endVertex();
		builder.vertex(matrices.last().pose(), 0, 1, -1).endVertex();
		builder.vertex(matrices.last().pose(), 0, 1, 1).endVertex();
		builder.vertex(matrices.last().pose(), 0, -1, 1).endVertex();
		tessellator.end();
	}
}
