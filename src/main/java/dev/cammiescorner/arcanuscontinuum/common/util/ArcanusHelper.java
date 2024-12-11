package dev.cammiescorner.arcanuscontinuum.common.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.WizardData;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.UUID;

public class ArcanusHelper {

	public static Color getMagicColor(@Nullable Entity entity) {
		if(entity == null) {
			return Arcanus.DEFAULT_MAGIC_COLOUR;
		}

		if(entity instanceof PlayerEntity player) {
			return getMagicColor(player.getGameProfile().getId());
		}

		if(entity instanceof Ownable ownable) {
			var owner = ownable.getOwner();
			if(owner != null) {
				return getMagicColor(owner);
			}
		}

		return Arcanus.DEFAULT_MAGIC_COLOUR;
	}

	public static Color getMagicColor(UUID playerId) {
		return WizardData.getOrEmpty(playerId).magicColor();
	}

	public static HitResult raycast(Entity origin, double maxDistance, boolean includeEntities, boolean includeFluids) {
		Vec3d startPos = origin.getCameraPosVec(1F);
		Vec3d rotation = origin.getRotationVec(1F);
		Vec3d endPos = startPos.add(rotation.multiply(maxDistance));
		HitResult hitResult = origin.getWorld().raycast(new RaycastContext(startPos, endPos, RaycastContext.ShapeType.COLLIDER, includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, origin));

		endPos = hitResult.getPos();
		maxDistance *= maxDistance;
		HitResult entityHitResult = ProjectileUtil.raycast(origin, startPos, endPos, origin.getBoundingBox().stretch(rotation.multiply(maxDistance)).expand(1.0D, 1D, 1D), entity -> !entity.isSpectator() && entity instanceof Targetable targetable && targetable.arcanuscontinuum$canBeTargeted(), maxDistance);

		if(includeEntities && entityHitResult != null)
			hitResult = entityHitResult;

		return hitResult;
	}

	@ClientOnly
	public static void renderBolts(LivingEntity entity, Vec3d startPos, MatrixStack matrices, VertexConsumerProvider vertices) {
		if(ArcanusComponents.shouldRenderBolt(entity)) {
			VertexConsumer vertex = vertices.getBuffer(ArcanusClient.getMagicCircles(ArcanusClient.WHITE));
			RandomGenerator random = RandomGenerator.createLegacy((entity.age + entity.getId()) / 2);
			Vec3d endPos = ArcanusComponents.getBoltPos(entity);

			var color = getMagicColor(entity);

			int steps = (int) (startPos.distanceTo(endPos) * 5);

			renderBolt(matrices, vertex, random, startPos, endPos, steps, 0, true, color.redF(), color.greenF(), color.blueF(), OverlayTexture.DEFAULT_UV, LightmapTextureManager.MAX_LIGHT_COORDINATE);
		}
	}

	@ClientOnly
	private static void renderBolt(MatrixStack matrices, VertexConsumer vertex, RandomGenerator random, Vec3d startPos, Vec3d endPos, int steps, int currentStep, boolean recurse, float r, float g, float b, int overlay, int light) {
		Vec3d direction = endPos.subtract(startPos);
		Vec3d lastPos = startPos;
		Matrix4f modelMatrix = matrices.peek().getModel();
		Matrix3f normalMatrix = matrices.peek().getNormal();

		for(int i = currentStep; i < steps; i++) {
			Vec3d randomOffset = new Vec3d(random.nextGaussian(), random.range(-1 / (steps * 2), 1 / (steps * 2)), random.nextGaussian());
			Vec3d nextPos = startPos.add(direction.multiply((i + 1) / (float) steps)).add(randomOffset.multiply(1 / 12F));

			for(int j = 0; j < 4; j++) {
				Vec3d vert1 = switch(j) {
					case 0 -> lastPos.add(0.025, 0.025, 0);
					case 1 -> lastPos.add(-0.025, 0.025, 0);
					case 2 -> lastPos.add(-0.025, -0.025, 0);
					case 3 -> lastPos.add(0.025, -0.025, 0);
					default -> lastPos;
				};
				Vec3d vert2 = switch(j) {
					case 0 -> lastPos.add(-0.025, 0.025, 0);
					case 1 -> lastPos.add(-0.025, -0.025, 0);
					case 2 -> lastPos.add(0.025, -0.025, 0);
					case 3 -> lastPos.add(0.025, 0.025, 0);
					default -> lastPos;
				};
				Vec3d vert3 = switch(j) {
					case 0 -> nextPos.add(0.025, 0.025, 0);
					case 1 -> nextPos.add(-0.025, 0.025, 0);
					case 2 -> nextPos.add(-0.025, -0.025, 0);
					case 3 -> nextPos.add(0.025, -0.025, 0);
					default -> nextPos;
				};
				Vec3d vert4 = switch(j) {
					case 0 -> nextPos.add(-0.025, 0.025, 0);
					case 1 -> nextPos.add(-0.025, -0.025, 0);
					case 2 -> nextPos.add(0.025, -0.025, 0);
					case 3 -> nextPos.add(0.025, 0.025, 0);
					default -> nextPos;
				};
				Vec3d normal = crossProduct(vert2.subtract(vert1), vert3.subtract(vert1));

				vertex.vertex(modelMatrix, (float) vert2.getX(), (float) vert2.getY(), (float) vert2.getZ()).color(r, g, b, 0.6F).uv(0, 0).overlay(overlay).light(light).normal(normalMatrix, (float) normal.getX(), (float) normal.getY(), (float) normal.getZ()).next();
				vertex.vertex(modelMatrix, (float) vert4.getX(), (float) vert4.getY(), (float) vert4.getZ()).color(r, g, b, 0.6F).uv(0, 0).overlay(overlay).light(light).normal(normalMatrix, (float) normal.getX(), (float) normal.getY(), (float) normal.getZ()).next();
				vertex.vertex(modelMatrix, (float) vert3.getX(), (float) vert3.getY(), (float) vert3.getZ()).color(r, g, b, 0.6F).uv(0, 0).overlay(overlay).light(light).normal(normalMatrix, (float) normal.getX(), (float) normal.getY(), (float) normal.getZ()).next();
				vertex.vertex(modelMatrix, (float) vert1.getX(), (float) vert1.getY(), (float) vert1.getZ()).color(r, g, b, 0.6F).uv(0, 0).overlay(overlay).light(light).normal(normalMatrix, (float) normal.getX(), (float) normal.getY(), (float) normal.getZ()).next();
			}

			while(recurse && random.nextFloat() < 0.2F) {
				Vec3d randomOffset1 = new Vec3d(random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
				renderBolt(matrices, vertex, random, lastPos, endPos.add(randomOffset1.multiply(Math.min(random.nextFloat(), 0.6F))), steps, i + 1, false, r, g, b, overlay, light);
			}

			lastPos = nextPos;
		}
	}

	public static Vec3d crossProduct(Vec3d vec1, Vec3d vec2) {
		return vec1.crossProduct(vec2);
	}

    public static ItemStack applyColorToItem(ItemStack stack, int color) {
        stack.getOrCreateSubNbt(ItemStack.DISPLAY_KEY).putInt(ItemStack.COLOR_KEY, color);
        return stack;
    }
}
