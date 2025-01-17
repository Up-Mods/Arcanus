package dev.cammiescorner.arcanuscontinuum.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.feature.HaloModel;
import dev.cammiescorner.arcanuscontinuum.common.items.StaffItem;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusMobEffects;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.cammiescorner.arcanuscontinuum.common.util.StaffType;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.HaloData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;

public class HaloFeatureRenderer<T extends Player, M extends EntityModel<T>> extends RenderLayer<T, M> {

	private static final ResourceLocation TEXTURE = Arcanus.id("textures/entity/feature/halo.png");
	private final HaloModel<T> model;

	public HaloFeatureRenderer(RenderLayerParent<T, M> context) {
		super(context);
		model = new HaloModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(HaloModel.MODEL_LAYER));
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertices, int light, T player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if(!player.hasEffect(ArcanusMobEffects.ANONYMITY.get())) {
			HaloData data = player.datasync$getOrDefault(Arcanus.HALO_DATA, HaloData.empty());

			if(data.shouldShow()) {
				Color color = data.color();

				model.setupAnim(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
				model.spinny.yRot = (float) Math.toRadians((player.tickCount + player.getId() + tickDelta) * 2);
				model.spinny.z = -3;

				matrices.pushPose();

				if(ArcanusComponents.isCasting(player) && player.getMainHandItem().getItem() instanceof StaffItem item && item.staffType == StaffType.STAFF)
					matrices.mulPose(Axis.YP.rotationDegrees(player.getMainArm() == HumanoidArm.RIGHT ? 65 : -65));

				model.renderToBuffer(matrices, vertices.getBuffer(ArcanusClient.getMagicCircles(TEXTURE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color.redF(), color.greenF(), color.blueF(), 1.0F);
				matrices.popPose();
			}
		}
	}
}
