package dev.cammiescorner.arcanuscontinuum.client.renderer.entity.magic;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.ManaShieldEntity;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.List;

public class ManaShieldEntityRenderer extends EntityRenderer<ManaShieldEntity> {
	private static final Identifier TEXTURE = Arcanus.id("textures/block/magic_block.png");
	private static final RenderLayer LAYER = ArcanusClient.getMagicCirclesTri(TEXTURE);
	public static final List<Vector3f> VERTICES = List.of(
			new Vector3f(0, 0, 1), new Vector3f(0.894F, 0F, 0.447F), new Vector3f(0.276F, 0.851F, 0.447F),
			new Vector3f(-0.724F, 0.526F, 0.447F), new Vector3f(-0.724F, -0.526F, 0.447F), new Vector3f(0.276F, -0.851F, 0.447F),
			new Vector3f(0.724F, 0.526F, -0.447F), new Vector3f(-0.276F, 0.851F, -0.447F), new Vector3f(-0.894F, 0F, -0.447F),
			new Vector3f(-0.276F, -0.851F, -0.447F), new Vector3f(0.724F, -0.526F, -0.447F), new Vector3f(0F, 0F, -1F)
	);
	public static final List<Vector3i> FACES = List.of(
			new Vector3i(0, 1, 2), new Vector3i(0, 2, 3), new Vector3i(0, 3, 4), new Vector3i(0, 4, 5), new Vector3i(0, 5, 1),
			new Vector3i(11, 6, 7), new Vector3i(11, 7, 8), new Vector3i(11, 8, 9), new Vector3i(11, 9, 10), new Vector3i(11, 10, 6),
			new Vector3i(1, 2, 6), new Vector3i(2, 3, 7), new Vector3i(3, 4, 8), new Vector3i(4, 5, 9), new Vector3i(5, 1, 10),
			new Vector3i(6, 7, 2), new Vector3i(7, 8, 3), new Vector3i(8, 9, 4), new Vector3i(9, 10, 5), new Vector3i(10, 6, 1)
	);

	public ManaShieldEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Override
	public void render(ManaShieldEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertices, int light) {
		float alpha = MathHelper.clamp(((entity.getMaxAge() - entity.getTrueAge()) - tickDelta) / 20F, 0, 1);

		matrices.push();
		matrices.translate(0, 2, 0);
		matrices.multiply(Axis.X_NEGATIVE.rotationDegrees(90));
		matrices.multiply(Axis.Z_POSITIVE.rotationDegrees((entity.age + tickDelta) * 0.25F));
		matrices.scale(3 * alpha, 3 * alpha, 3 * alpha);
		drawIcosahedron(matrices, vertices.getBuffer(LAYER), ArcanusHelper.getMagicColor(entity), alpha, light, OverlayTexture.DEFAULT_UV);
		matrices.pop();
	}

	@Override
	public Identifier getTexture(ManaShieldEntity entity) {
		return TEXTURE;
	}

	public static void drawIcosahedron(MatrixStack matrices, VertexConsumer consumer, Color color, float alpha, int light, int overlay) {
		Matrix4f matrix4f = matrices.peek().getModel();
		Matrix3f matrix3f = matrices.peek().getNormal();
		float r = color.redF() * alpha;
		float g = color.greenF() * alpha;
		float b = color.blueF() * alpha;

		for(Vector3i face : FACES) {
			Vector3f vert1 = ManaShieldEntityRenderer.VERTICES.get(face.x);
			Vector3f vert2 = ManaShieldEntityRenderer.VERTICES.get(face.y);
			Vector3f vert3 = ManaShieldEntityRenderer.VERTICES.get(face.z);
			Vector3f u = new Vector3f(vert2.x - vert1.x, vert2.y - vert1.y, vert2.z - vert1.z);
			Vector3f v = new Vector3f(vert3.x - vert1.x, vert3.y - vert1.y, vert3.z - vert1.z);
			Vector3f normal = u.cross(v);

			consumer.vertex(matrix4f, vert1.x, vert1.y, vert1.z).color(r, g, b, 1.0F).uv(0, 0).overlay(overlay).light(light).normal(matrix3f, normal.x, normal.y, normal.z).next();
			consumer.vertex(matrix4f, vert2.x, vert2.y, vert2.z).color(r, g, b, 1.0F).uv(0, 0).overlay(overlay).light(light).normal(matrix3f, normal.x, normal.y, normal.z).next();
			consumer.vertex(matrix4f, vert3.x, vert3.y, vert3.z).color(r, g, b, 1.0F).uv(0, 0).overlay(overlay).light(light).normal(matrix3f, normal.x, normal.y, normal.z).next();
		}
	}
}
