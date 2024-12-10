package dev.cammiescorner.arcanuscontinuum.client.renderer.feature;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.feature.LotusHaloModel;
import dev.cammiescorner.arcanuscontinuum.common.items.StaffItem;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusStatusEffects;
import dev.cammiescorner.arcanuscontinuum.common.util.StaffType;
import net.minecraft.client.MinecraftClient;
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

public class LotusHaloFeatureRenderer<T extends PlayerEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	private static final Identifier TEXTURE = Arcanus.id("textures/entity/feature/lotus_halo.png");
	private final LotusHaloModel<T> model;

	public LotusHaloFeatureRenderer(FeatureRendererContext<T, M> context) {
		super(context);
		model = new LotusHaloModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(LotusHaloModel.MODEL_LAYER));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertices, int light, T player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if ((player.getUuidAsString().equals("6147825f-5493-4154-87c5-5c03c6b0a7c2") || player.getUuidAsString().equals("883a7a9a-ecca-48fd-9d8c-5ba5c0175eb4")) && !player.hasStatusEffect(ArcanusStatusEffects.ANONYMITY.get())) {
			model.setAngles(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
			model.spinny.yaw = (float) Math.toRadians((player.age + player.getId() + tickDelta) * 2);
			model.spinny.pivotZ = -3;

			matrices.push();

			if (ArcanusComponents.isCasting(player) && player.getMainHandStack().getItem() instanceof StaffItem item && item.staffType == StaffType.STAFF)
				matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(player.getMainArm() == Arm.RIGHT ? 65 : -65));

			model.render(matrices, vertices.getBuffer(ArcanusClient.getMagicCircles(TEXTURE)), 15728850, OverlayTexture.DEFAULT_UV, 1F, 1F, 1F, 1F);
			matrices.pop();
		}

	}
}
