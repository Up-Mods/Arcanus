package dev.cammiescorner.arcanus.fabric.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanus.fabric.client.models.feature.SpellPatternModel;
import dev.cammiescorner.arcanus.fabric.common.items.StaffItem;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.fabric.common.util.ArcanusHelper;
import dev.cammiescorner.arcanus.fabric.common.util.Color;
import dev.cammiescorner.arcanus.fabric.common.util.StaffType;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricClient;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SpellPatternFeatureRenderer<T extends Player, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private static final ResourceLocation TEXTURE = FabricMain.id("textures/entity/feature/magic_circles.png");
	private final Minecraft client = Minecraft.getInstance();
	private final SpellPatternModel<Player> model;

	public SpellPatternFeatureRenderer(RenderLayerParent<T, M> context) {
		super(context);
		model = new SpellPatternModel<>(client.getEntityModels().bakeLayer(SpellPatternModel.MODEL_LAYER));
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertices, int light, Player player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		ItemStack stack = player.getMainHandItem();
		Color color = ArcanusHelper.getMagicColor(player);

		model.showMagicCircles(ArcanusComponents.getPattern(player));

		matrices.pushPose();
		matrices.translate(0, 0.65, -0.35);

		model.setupAnim(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		model.showMagicCircles(ArcanusComponents.getPattern(player));

		if(ArcanusComponents.isCasting(player) && stack.getItem() instanceof StaffItem item) {
			if(item.staffType == StaffType.STAFF) {
				matrices.translate(player.getMainArm() == HumanoidArm.RIGHT ? -0.35 : 0.35, 0, 0.05);
				matrices.mulPose(Axis.YP.rotationDegrees(player.getMainArm() == HumanoidArm.RIGHT ? 65 : -65));
			}
			else if(item.staffType == StaffType.WAND) {
				matrices.translate(player.getMainArm() == HumanoidArm.RIGHT ? -0.35 : 0.35, -0.05, -0.1);
			}
			else if(item.staffType == StaffType.GAUNTLET) {
				matrices.translate(player.getMainArm() == HumanoidArm.RIGHT ? -0.35 : 0.35, -0.05, 0.4);
			}
			else if(item.staffType == StaffType.BOOK) {
				matrices.translate(player.getMainArm() == HumanoidArm.RIGHT ? 0.35 : -0.35, -0.05, 0.4);
			}
			else if(item.staffType == StaffType.GUN) {
				matrices.translate(0, -0.15, 0.15);
			}
		}

		matrices.pushPose();
		matrices.translate(0, 0, Mth.sin((player.tickCount + player.getId() + client.getFrameTime()) / (Mth.PI * 2)) * 0.05F);
		model.first.render(matrices, vertices.getBuffer(FabricClient.getMagicCircles(TEXTURE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color.redF(), color.greenF(), color.blueF(), color.alphaF());
		matrices.popPose();

		matrices.pushPose();
		matrices.translate(0, 0, Mth.cos((player.tickCount + player.getId() + client.getFrameTime()) / (Mth.PI * 2)) * 0.05F);
		model.second.render(matrices, vertices.getBuffer(FabricClient.getMagicCircles(TEXTURE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color.redF(), color.greenF(), color.blueF(), color.alphaF());
		matrices.popPose();

		matrices.pushPose();
		matrices.translate(0, 0, Mth.sin((player.tickCount + player.getId() + client.getFrameTime()) / (Mth.PI * 2)) * 0.05F);
		model.third.render(matrices, vertices.getBuffer(FabricClient.getMagicCircles(TEXTURE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color.redF(), color.greenF(), color.blueF(), color.alphaF());
		matrices.popPose();

		matrices.popPose();
	}
}
