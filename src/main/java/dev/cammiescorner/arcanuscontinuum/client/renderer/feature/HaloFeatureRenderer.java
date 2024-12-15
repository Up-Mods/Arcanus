package dev.cammiescorner.arcanuscontinuum.client.renderer.feature;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.feature.HaloModel;
import dev.cammiescorner.arcanuscontinuum.common.items.StaffItem;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusStatusEffects;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.cammiescorner.arcanuscontinuum.common.util.StaffType;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.HaloData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;

public class HaloFeatureRenderer<T extends PlayerEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

	private static final Identifier TEXTURE = Arcanus.id("textures/entity/feature/halo.png");
	private final HaloModel<T> model;

	public HaloFeatureRenderer(FeatureRendererContext<T, M> context) {
		super(context);
		model = new HaloModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(HaloModel.MODEL_LAYER));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertices, int light, T player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if(!player.hasStatusEffect(ArcanusStatusEffects.ANONYMITY.get())) {
			HaloData data = player.datasync$getOrDefault(Arcanus.HALO_DATA, HaloData.empty());

			if(data.shouldShow()) {
				Color color = data.color();

				model.setAngles(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
				model.spinny.yaw = (float) Math.toRadians((player.age + player.getId() + tickDelta) * 2);
				model.spinny.pivotZ = -3;

				matrices.push();

				if (ArcanusComponents.isCasting(player) && player.getMainHandStack().getItem() instanceof StaffItem item && item.staffType == StaffType.STAFF)
					matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(player.getMainArm() == Arm.RIGHT ? 65 : -65));

				model.render(matrices, vertices.getBuffer(ArcanusClient.getMagicCircles(TEXTURE)), LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, color.redF(), color.greenF(), color.blueF(), 1.0F);
				matrices.pop();
			}
		}
	}
}
