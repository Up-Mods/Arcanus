package dev.cammiescorner.arcanuscontinuum.client.renderer.entity.magic;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.SmiteEntity;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
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

public class SmiteEntityRenderer extends EntityRenderer<SmiteEntity> {
	private static final Identifier TEXTURE = Arcanus.id("textures/block/magic_block.png");

	public SmiteEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Override
	public void render(SmiteEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light) {
		super.render(entity, yaw, tickDelta, matrices, provider, light);
		renderBeam(entity, matrices, provider, 0, (float) ((entity.getWorld().getHeight() + 2048) - entity.getY()), 0, tickDelta, OverlayTexture.DEFAULT_UV, light);
	}

	public void renderBeam(SmiteEntity entity, MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float z, float tickDelta, int overlay, int light) {
		int maxQuads = 16;
		float radius = 2.25F;
		Color color = entity.getColor();
		float squaredLength = x * x + y * y + z * z;
		float length = MathHelper.sqrt(squaredLength);
		float ageDelta = (entity.age - 1) + tickDelta;
		float scale = MathHelper.clamp(ageDelta < 3 ? (ageDelta) / 3F : ageDelta > 9 ? 1 - ((ageDelta - 9F) / 15F) : 1F, 0F, 1F);
		float alpha = ageDelta < 3 ? 1 : MathHelper.clamp(1 - ((ageDelta - 3) / 23F), 0, 1);
		float r = color.redF() * alpha;
		float g = color.greenF() * alpha;
		float b = color.blueF() * alpha;

		matrices.push();
		matrices.multiply(Axis.X_POSITIVE.rotationDegrees(-90));
		matrices.scale(scale, scale, 1);

		VertexConsumer vertexConsumer = provider.getBuffer(ArcanusClient.getMagicCircles(TEXTURE));
		MatrixStack.Entry entry = matrices.peek();
		Matrix4f matrix4f = entry.getModel();
		Matrix3f matrix3f = matrices.peek().getNormal();
		float vertX1 = 0F;
		float vertY1 = radius;

		for(int i = 1; i <= maxQuads; i++) {
			float vertX2 = MathHelper.sin(i * 6.2831855F / maxQuads) * radius;
			float vertY2 = MathHelper.cos(i * 6.2831855F / maxQuads) * radius;
			Vector3f u = new Vector3f(vertX2 - vertX1, vertY2 - vertY1, length);
			Vector3f v = new Vector3f(vertX1 - vertX2, vertY1 - vertY2, -length);
			Vector3f normal = u.cross(v);

			vertexConsumer.vertex(matrix4f, vertX1, vertY1, 0F).color(r, g, b, 1.0F).uv(0, 0).overlay(overlay).light(light).normal(matrix3f, normal.x, normal.y, normal.z).next();
			vertexConsumer.vertex(matrix4f, vertX1, vertY1, length).color(r, g, b, 1.0F).uv(0, 0).overlay(overlay).light(light).normal(matrix3f, normal.x, normal.y, normal.z).next();
			vertexConsumer.vertex(matrix4f, vertX2, vertY2, length).color(r, g, b, 1.0F).uv(0, 0).overlay(overlay).light(light).normal(matrix3f, normal.x, normal.y, normal.z).next();
			vertexConsumer.vertex(matrix4f, vertX2, vertY2, 0F).color(r, g, g, 1.0F).uv(0, 0).overlay(overlay).light(light).normal(matrix3f, normal.x, normal.y, normal.z).next();

			vertX1 = vertX2;
			vertY1 = vertY2;
		}

		matrices.pop();
	}

	@Override
	public boolean shouldRender(SmiteEntity entity, Frustum frustum, double x, double y, double z) {
		return true;
	}

	@Override
	public Identifier getTexture(SmiteEntity entity) {
		return TEXTURE;
	}
}
