package dev.cammiescorner.arcanuscontinuum.client.renderer.entity.magic;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.PocketDimensionPortalEntityModel;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.SpatialRiftEntitySigilModel;
import dev.cammiescorner.arcanuscontinuum.client.utils.StencilBuffer;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.PocketDimensionPortalEntity;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import org.lwjgl.opengl.GL31;

public class PocketDimensionPortalEntityRenderer extends EntityRenderer<PocketDimensionPortalEntity> {
	private static final Identifier TEXTURE = Arcanus.id("textures/entity/magic/pocket_dimension_portal.png");
	private static final Identifier SIGIL_TEXTURE = Arcanus.id("textures/entity/magic/spatial_rift_sigil.png");
	private final MinecraftClient client = MinecraftClient.getInstance();
	private final Tessellator tessellator = Tessellator.getInstance();
	private final PocketDimensionPortalEntityModel model;
	private final SpatialRiftEntitySigilModel sigilModel;

	public PocketDimensionPortalEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
		model = new PocketDimensionPortalEntityModel(ctx.getModelLoader().getModelPart(PocketDimensionPortalEntityModel.MODEL_LAYER));
		sigilModel = new SpatialRiftEntitySigilModel(ctx.getModelLoader().getModelPart(SpatialRiftEntitySigilModel.MODEL_LAYER));
	}

	@Override
	public void render(PocketDimensionPortalEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertices, int light) {
		super.render(entity, yaw, tickDelta, matrices, vertices, light);
		StencilBuffer stencilBuffer = ((StencilBuffer) client.getFramebuffer());
		RenderLayer layer = ArcanusClient.getMagicPortal(TEXTURE);
		RenderLayer sigilLayer = ArcanusClient.getMagicPortal(SIGIL_TEXTURE);
		Color color = entity.getColor();
		float ageDelta = entity.getTrueAge() + tickDelta;
		float maxScale = 0.75f;
		float scale = entity.getTrueAge() <= 100 ? Math.min(maxScale, (ageDelta / 100f) * maxScale) : entity.getTrueAge() > 700 ? Math.max(0, (1 - (ageDelta - 700) / 20f) * maxScale) : maxScale;

		matrices.push();
		matrices.translate(0, 1.625, 0);
		matrices.multiply(Axis.Z_POSITIVE.rotationDegrees(180));
		matrices.scale(scale, 1, scale);
		model.skybox.render(matrices, vertices.getBuffer(RenderLayer.getEntitySolid(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
		matrices.pop();

		matrices.push();
		matrices.translate(0, 0.05, 0);
		matrices.scale(scale, 1, scale);
		matrices.multiply(Axis.Z_POSITIVE.rotationDegrees(90));

		if(!stencilBuffer.arcanuscontinuum$isStencilBufferEnabled())
			stencilBuffer.arcanuscontinuum$enableStencilBufferAndReload(true);

		GL31.glDisable(GL31.GL_DEPTH_TEST);
		GL31.glEnable(GL31.GL_STENCIL_TEST);

		GL31.glDepthMask(false);
		GL31.glColorMask(false, false, false, true);
		GL31.glStencilOp(GL31.GL_KEEP, GL31.GL_KEEP, GL31.GL_INCR);
		GL31.glStencilFunc(GL31.GL_EQUAL, 0, 0xFF);
		GL31.glStencilMask(0xFF);

		RenderLayer.getWaterMask().startDrawing();
		GameRenderer.getPositionShader().bind();
		GL31.glColorMask(true, false, false, true);
		GL31.glDepthFunc(GL31.GL_LEQUAL);
		drawStencil(matrices, tessellator);
		GameRenderer.getPositionShader().unbind();
		RenderLayer.getWaterMask().endDrawing();

		GL31.glColorMask(true, true, true, true);
		GL31.glStencilFunc(GL31.GL_NOTEQUAL, 0, 0xFF);
		GL31.glStencilMask(0x00);

		matrices.push();
		matrices.translate(-0.375, 0, 0);
		matrices.multiply(Axis.Z_POSITIVE.rotationDegrees(90));
		matrices.scale(maxScale, maxScale, maxScale);
		model.render(matrices, vertices.getBuffer(layer), light, OverlayTexture.DEFAULT_UV, color.redF(), color.greenF(), color.blueF(), 1.0F);
		matrices.pop();

		if(vertices instanceof VertexConsumerProvider.Immediate immediate) {
			immediate.draw();
			GL31.glDepthMask(true);
		}

		GL31.glColorMask(false, false, false, true);
		GL31.glStencilOp(GL31.GL_KEEP, GL31.GL_KEEP, GL31.GL_DECR);
		GL31.glStencilFunc(GL31.GL_EQUAL, 1, 0xFF);
		GL31.glStencilMask(0xFF);

		RenderLayer.getWaterMask().startDrawing();
		GameRenderer.getPositionShader().bind();
		GL31.glDepthFunc(GL31.GL_ALWAYS);
		drawStencil(matrices, tessellator);
		GameRenderer.getPositionShader().unbind();
		RenderLayer.getWaterMask().endDrawing();

		GL31.glStencilFunc(GL31.GL_EQUAL, 0, 0xFF);
		GL31.glStencilMask(0x00);

		GL31.glDisable(GL31.GL_STENCIL_TEST);
		GL31.glEnable(GL31.GL_DEPTH_TEST);
		GL31.glDepthFunc(GL31.GL_LEQUAL);

		matrices.pop();

		matrices.push();
		matrices.translate(0.0D, 1.51D, 0.0D);
		matrices.multiply(Axis.X_POSITIVE.rotationDegrees(180.0F));
		matrices.scale(scale / maxScale, 1.0F, scale / maxScale);
		sigilModel.sigil.yaw = (entity.age + tickDelta) * 0.015F;
		sigilModel.render(matrices, vertices.getBuffer(sigilLayer), light, OverlayTexture.DEFAULT_UV, color.redF(), color.greenF(), color.blueF(), 1.0F);
		matrices.pop();
	}

	@Override
	public Identifier getTexture(PocketDimensionPortalEntity entity) {
		return TEXTURE;
	}

	public static void drawStencil(MatrixStack matrices, Tessellator tessellator) {
		BufferBuilder builder = tessellator.getBufferBuilder();
		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
		builder.vertex(matrices.peek().getModel(), 0, -1, -1).next();
		builder.vertex(matrices.peek().getModel(), 0, 1, -1).next();
		builder.vertex(matrices.peek().getModel(), 0, 1, 1).next();
		builder.vertex(matrices.peek().getModel(), 0, -1, 1).next();
		tessellator.draw();
	}
}
