package dev.cammiescorner.arcanuscontinuum.client.renderer.entity.magic;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.BeamEntity;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3d;

public class BeamEntityRenderer extends EntityRenderer<BeamEntity> {
	private static final Identifier BEAM_TEXTURE = Arcanus.id("textures/entity/magic/beam.png");
	private static final RenderLayer LAYER = ArcanusClient.getMagicCircles(BEAM_TEXTURE);
	private static final Vector3d UP = new Vector3d(0, 1, 0);

	public BeamEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Override
	public void render(BeamEntity beamEntity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertices, int light) {
		LivingEntity caster = beamEntity.getCaster();
		Vec3d cam = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();

		if(caster != null) {
			Vec3d startPos = caster.getLerpedPos(tickDelta).add(0, caster.getEyeHeight(caster.getPose()) * 0.9F, 0);
			Vec3d endPos = beamEntity.getBeamPos(tickDelta);
			Vector3d axis = new Vector3d(endPos.getX() - startPos.getX(), endPos.getY() - startPos.getY(), endPos.getZ() - startPos.getZ()).normalize();
			VertexConsumer vertex = vertices.getBuffer(LAYER);
			Color color = beamEntity.getColor();
			float distance = beamEntity.distanceTo(caster) / 2F;

			matrices.push();
			matrices.translate(-beamEntity.getX(), -beamEntity.getY(), -beamEntity.getZ());

			for(int i = 0; i < 2; i++) {
				Vector3d vec = new Vector3d(cam.getX(), cam.getY(), cam.getZ()).sub(startPos.getX(), startPos.getY(), startPos.getZ()).cross(axis).normalize().mul(0.2);
				vec.rotateAxis(Math.toRadians(i == 0 ? 45 : -45), axis.x, axis.y, axis.z);
				Vec3d vert1 = startPos.add(vec.x, vec.y, vec.z);
				Vec3d vert2 = startPos.subtract(vec.x, vec.y, vec.z);
				Vec3d vert3 = endPos.add(vec.x, vec.y, vec.z);
				Vec3d vert4 = endPos.subtract(vec.x, vec.y, vec.z);
				float beamProgress = beamEntity.getBeamProgress(tickDelta) * 2F;

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

			matrices.pop();
		}
	}

	@Override
	public boolean shouldRender(BeamEntity entity, Frustum frustum, double x, double y, double z) {
		return true;
	}

	private static void vertex(VertexConsumer vertex, MatrixStack matrices, Vec3d vert, Color color, float u, float v) {
		Matrix4f modelMatrix = matrices.peek().getModel();
		Matrix3f normalMatrix = matrices.peek().getNormal();

		vertex.vertex(modelMatrix, (float) vert.getX(), (float) vert.getY(), (float) vert.getZ()).color(color.red(), color.green(), color.blue(), color.alpha()).uv(u, v).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0F, 1F, 0F).next();
	}

	@Override
	public Identifier getTexture(BeamEntity entity) {
		return BEAM_TEXTURE;
	}
}
