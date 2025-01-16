package dev.cammiescorner.arcanus.fabric.client.renderer.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.arcanus.fabric.client.models.entity.living.WizardEntityModel;
import dev.cammiescorner.arcanus.fabric.client.renderer.feature.WizardHeldItemFeatureRenderer;
import dev.cammiescorner.arcanus.fabric.common.entities.living.WizardEntity;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

public class WizardEntityRenderer extends MobRenderer<WizardEntity, WizardEntityModel> {
	public static final ResourceLocation TEXTURE = Arcanus.id("textures/entity/living/wizard.png");
	public static final ResourceLocation ROBES_TEXTURE = Arcanus.id("textures/entity/living/wizard_overlay.png");

	public WizardEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new WizardEntityModel(Minecraft.getInstance().getEntityModels().bakeLayer(WizardEntityModel.MODEL_LAYER)), 0.6F);
		addLayer(new WizardHeldItemFeatureRenderer<>(this, context.getItemInHandRenderer()));
	}

	@Override
	public void render(WizardEntity wizard, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertices, int light) {
		super.render(wizard, yaw, tickDelta, matrices, vertices, light);

		int hexColor = wizard.getRobeColor();
		float r = (hexColor >> 16 & 255) / 255F;
		float g = (hexColor >> 8 & 255) / 255F;
		float b = (hexColor & 255) / 255F;

		if(wizard.hasCustomName() && wizard.getName().getString().equals("jeb_")) {
			int m = 15;
			int n = wizard.tickCount / m + wizard.getId();
			int o = DyeColor.values().length;
			float f = ((wizard.tickCount % m) + tickDelta) / 15F;
			float[] fs = Sheep.getColorArray(DyeColor.byId(n % o));
			float[] gs = Sheep.getColorArray(DyeColor.byId((n + 1) % o));
			r = fs[0] * (1F - f) + gs[0] * f;
			g = fs[1] * (1F - f) + gs[1] * f;
			b = fs[2] * (1F - f) + gs[2] * f;
		}

		matrices.pushPose();
		setupRotations(wizard, matrices, 0, Mth.rotLerp(tickDelta, wizard.yBodyRotO, wizard.yBodyRot), tickDelta);
		matrices.scale(-1.0F, -1.0F, 1.0F);
		scale(wizard, matrices, tickDelta);
		matrices.translate(0.0, -1.5, 0.0);
		model.renderToBuffer(matrices, vertices.getBuffer(RenderType.entityCutout(ROBES_TEXTURE)), light, OverlayTexture.NO_OVERLAY, r, g, b, 1F);
		matrices.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(WizardEntity entity) {
		return TEXTURE;
	}
}
