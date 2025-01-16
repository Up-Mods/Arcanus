package dev.cammiescorner.arcanus.fabric.client.renderer.entity.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanus.fabric.common.entities.magic.BeamEntity;
import dev.cammiescorner.arcanus.fabric.common.util.ArcanusHelper;
import dev.cammiescorner.arcanus.fabric.common.util.Color;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricClient;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3d;

public class BeamEntityRenderer extends EntityRenderer<BeamEntity> {
	private static final ResourceLocation BEAM_TEXTURE = Arcanus.id("textures/entity/magic/beam.png");
	private static final RenderType LAYER = FabricClient.getMagicCircles(BEAM_TEXTURE);
	private static final Vector3d UP = new Vector3d(0, 1, 0);

	public BeamEntityRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public void render(BeamEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertices, int light) {
		LivingEntity caster = entity.getCaster();
		Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

		if(caster != null) {
			Vec3 startPos = caster.getPosition(tickDelta).add(0, caster.getEyeHeight(caster.getPose()) * 0.9F, 0);
			Vec3 endPos = entity.getBeamPos(tickDelta);
			Vector3d axis = new Vector3d(endPos.x() - startPos.x(), endPos.y() - startPos.y(), endPos.z() - startPos.z()).normalize();
			VertexConsumer vertex = vertices.getBuffer(LAYER);
			Color color = ArcanusHelper.getMagicColor(entity);
			float distance = entity.distanceTo(caster) / 2F;

			matrices.pushPose();
			matrices.translate(-entity.getX(), -entity.getY(), -entity.getZ());

			for(int i = 0; i < 2; i++) {
				Vector3d vec = new Vector3d(cam.x(), cam.y(), cam.z()).sub(startPos.x(), startPos.y(), startPos.z()).cross(axis).normalize().mul(0.2);
				vec.rotateAxis(Math.toRadians(i == 0 ? 45 : -45), axis.x, axis.y, axis.z);
				Vec3 vert1 = startPos.add(vec.x, vec.y, vec.z);
				Vec3 vert2 = startPos.subtract(vec.x, vec.y, vec.z);
				Vec3 vert3 = endPos.add(vec.x, vec.y, vec.z);
				Vec3 vert4 = endPos.subtract(vec.x, vec.y, vec.z);
				float beamProgress = entity.getBeamProgress(tickDelta) * 2F;

				if(i > 0)
					beamProgress *= -1F;

				float minU = 0;
				float minV = -beamProgress;
				float maxU = 1;
				float maxV = distance - beamProgress;

				if(i > 0) {
					minV = 1 - minV;
					maxV = 1 - maxV;
				}

				vertex(vertex, matrices, vert4, color, maxU, minV);
				vertex(vertex, matrices, vert3, color, minU, minV);
				vertex(vertex, matrices, vert1, color, minU, maxV);
				vertex(vertex, matrices, vert2, color, maxU, maxV);
			}

			matrices.popPose();
		}
	}

	@Override
	public boolean shouldRender(BeamEntity entity, Frustum frustum, double x, double y, double z) {
		return true;
	}

	private static void vertex(VertexConsumer vertex, PoseStack matrices, Vec3 vert, Color color, float u, float v) {
		Matrix4f modelMatrix = matrices.last().pose();
		Matrix3f normalMatrix = matrices.last().normal();

		vertex.vertex(modelMatrix, (float) vert.x(), (float) vert.y(), (float) vert.z()).color(color.red(), color.green(), color.blue(), color.alpha()).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0F, 1F, 0F).endVertex();
	}

	@Override
	public ResourceLocation getTextureLocation(BeamEntity entity) {
		return BEAM_TEXTURE;
	}
}
