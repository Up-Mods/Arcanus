package dev.cammiescorner.arcanuscontinuum.common.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.common.components.MagicColorComponent;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.WizardData;
import net.minecraft.Util;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.UUID;

public class ArcanusHelper {

	public static Color getMagicColor(@Nullable Object provider) {
		if(provider == null) {
			return Arcanus.DEFAULT_MAGIC_COLOUR;
		}

		var component = ArcanusComponents.MAGIC_COLOR.getNullable(provider);
		if (component != null) {
			return component.getColor();
		}

		// if Entity
		if (provider instanceof TraceableEntity ownable) {
			var owner = ownable.getOwner();
			if (owner != null) {
				return getMagicColor(owner);
			}
		}

		return Arcanus.DEFAULT_MAGIC_COLOUR;
	}

	public static Color getMagicColor(@Nullable UUID playerId) {
		if (playerId == null || Util.NIL_UUID.equals(playerId)) {
			return Arcanus.DEFAULT_MAGIC_COLOUR;
		}

		return WizardData.getOrEmpty(playerId).magicColor();
	}

	public static HitResult raycast(Entity origin, double maxDistance, boolean includeEntities, boolean includeFluids) {
		Vec3 startPos = origin.getEyePosition(1F);
		Vec3 rotation = origin.getViewVector(1F);
		Vec3 endPos = startPos.add(rotation.scale(maxDistance));
		HitResult hitResult = origin.level().clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, includeFluids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, origin));

		endPos = hitResult.getLocation();
		maxDistance *= maxDistance;
		HitResult entityHitResult = ProjectileUtil.getEntityHitResult(origin, startPos, endPos, origin.getBoundingBox().expandTowards(rotation.scale(maxDistance)).inflate(1.0D, 1D, 1D), entity -> !entity.isSpectator() && entity instanceof Targetable targetable && targetable.arcanuscontinuum$canBeTargeted(), maxDistance);

		if (includeEntities && entityHitResult != null)
			hitResult = entityHitResult;

		return hitResult;
	}

	@ClientOnly
	public static void renderBolts(LivingEntity entity, Vec3 startPos, PoseStack matrices, MultiBufferSource vertices) {
		if (ArcanusComponents.shouldRenderBolt(entity)) {
			VertexConsumer vertex = vertices.getBuffer(ArcanusClient.getMagicCircles(ArcanusClient.WHITE));
			RandomSource random = RandomSource.create((entity.tickCount + entity.getId()) / 2);
			Vec3 endPos = ArcanusComponents.getBoltPos(entity);

			var color = getMagicColor(entity);

			int steps = (int) (startPos.distanceTo(endPos) * 5);

			renderBolt(matrices, vertex, random, startPos, endPos, steps, 0, true, color.redF(), color.greenF(), color.blueF(), OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT);
		}
	}

	@ClientOnly
	private static void renderBolt(PoseStack matrices, VertexConsumer vertex, RandomSource random, Vec3 startPos, Vec3 endPos, int steps, int currentStep, boolean recurse, float r, float g, float b, int overlay, int light) {
		Vec3 direction = endPos.subtract(startPos);
		Vec3 lastPos = startPos;
		Matrix4f modelMatrix = matrices.last().pose();
		Matrix3f normalMatrix = matrices.last().normal();

		for (int i = currentStep; i < steps; i++) {
			Vec3 randomOffset = new Vec3(random.nextGaussian(), random.nextIntBetweenInclusive(-1 / (steps * 2), 1 / (steps * 2)), random.nextGaussian());
			Vec3 nextPos = startPos.add(direction.scale((i + 1) / (float) steps)).add(randomOffset.scale(1 / 12F));

			for (int j = 0; j < 4; j++) {
				Vec3 vert1 = switch (j) {
					case 0 -> lastPos.add(0.025, 0.025, 0);
					case 1 -> lastPos.add(-0.025, 0.025, 0);
					case 2 -> lastPos.add(-0.025, -0.025, 0);
					case 3 -> lastPos.add(0.025, -0.025, 0);
					default -> lastPos;
				};
				Vec3 vert2 = switch (j) {
					case 0 -> lastPos.add(-0.025, 0.025, 0);
					case 1 -> lastPos.add(-0.025, -0.025, 0);
					case 2 -> lastPos.add(0.025, -0.025, 0);
					case 3 -> lastPos.add(0.025, 0.025, 0);
					default -> lastPos;
				};
				Vec3 vert3 = switch (j) {
					case 0 -> nextPos.add(0.025, 0.025, 0);
					case 1 -> nextPos.add(-0.025, 0.025, 0);
					case 2 -> nextPos.add(-0.025, -0.025, 0);
					case 3 -> nextPos.add(0.025, -0.025, 0);
					default -> nextPos;
				};
				Vec3 vert4 = switch (j) {
					case 0 -> nextPos.add(-0.025, 0.025, 0);
					case 1 -> nextPos.add(-0.025, -0.025, 0);
					case 2 -> nextPos.add(0.025, -0.025, 0);
					case 3 -> nextPos.add(0.025, 0.025, 0);
					default -> nextPos;
				};
				Vec3 normal = crossProduct(vert2.subtract(vert1), vert3.subtract(vert1));

				vertex.vertex(modelMatrix, (float) vert2.x(), (float) vert2.y(), (float) vert2.z()).color(r, g, b, 0.6F).uv(0, 0).overlayCoords(overlay).uv2(light).normal(normalMatrix, (float) normal.x(), (float) normal.y(), (float) normal.z()).endVertex();
				vertex.vertex(modelMatrix, (float) vert4.x(), (float) vert4.y(), (float) vert4.z()).color(r, g, b, 0.6F).uv(0, 0).overlayCoords(overlay).uv2(light).normal(normalMatrix, (float) normal.x(), (float) normal.y(), (float) normal.z()).endVertex();
				vertex.vertex(modelMatrix, (float) vert3.x(), (float) vert3.y(), (float) vert3.z()).color(r, g, b, 0.6F).uv(0, 0).overlayCoords(overlay).uv2(light).normal(normalMatrix, (float) normal.x(), (float) normal.y(), (float) normal.z()).endVertex();
				vertex.vertex(modelMatrix, (float) vert1.x(), (float) vert1.y(), (float) vert1.z()).color(r, g, b, 0.6F).uv(0, 0).overlayCoords(overlay).uv2(light).normal(normalMatrix, (float) normal.x(), (float) normal.y(), (float) normal.z()).endVertex();
			}

			while (recurse && random.nextFloat() < 0.2F) {
				Vec3 randomOffset1 = new Vec3(random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
				renderBolt(matrices, vertex, random, lastPos, endPos.add(randomOffset1.scale(Math.min(random.nextFloat(), 0.6F))), steps, i + 1, false, r, g, b, overlay, light);
			}

			lastPos = nextPos;
		}
	}

	public static Vec3 crossProduct(Vec3 vec1, Vec3 vec2) {
		return vec1.cross(vec2);
	}

	public static ItemStack applyColorToItem(ItemStack stack, int color) {
		stack.getOrCreateTagElement(ItemStack.TAG_DISPLAY).putInt(ItemStack.TAG_COLOR, color);
		return stack;
	}

	/**
	 * sets the magic color if the entity can store it
	 *
	 * @param sourceId the UUID of the source player to get the magic color from
	 */
	public static void setMagicColorSource(Object obj, UUID sourceId) {
		ArcanusComponents.MAGIC_COLOR.maybeGet(obj).ifPresent(component -> component.setSourceId(sourceId));
	}

	/**
	 * sets the magic color if the entity can store it
	 *
	 * @param from the entity to take the color from.
	 *               If this entity does not have an attached {@link MagicColorComponent},
	 *               will default to {@link Arcanus#DEFAULT_MAGIC_COLOUR}
	 */
	public static void copyMagicColor(Object to, Entity from) {
		ArcanusComponents.MAGIC_COLOR.maybeGet(from).ifPresent(sourceComponent -> setMagicColorSource(to, sourceComponent.getSourceId()));
	}
}
