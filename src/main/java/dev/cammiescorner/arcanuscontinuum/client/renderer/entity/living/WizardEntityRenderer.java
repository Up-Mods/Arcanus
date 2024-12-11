package dev.cammiescorner.arcanuscontinuum.client.renderer.entity.living;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.living.WizardEntityModel;
import dev.cammiescorner.arcanuscontinuum.client.renderer.feature.WizardHeldItemFeatureRenderer;
import dev.cammiescorner.arcanuscontinuum.common.entities.living.WizardEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class WizardEntityRenderer extends MobEntityRenderer<WizardEntity, WizardEntityModel> {
	public static final Identifier TEXTURE = Arcanus.id("textures/entity/living/wizard.png");
	public static final Identifier ROBES_TEXTURE = Arcanus.id("textures/entity/living/wizard_overlay.png");

	public WizardEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new WizardEntityModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(WizardEntityModel.MODEL_LAYER)), 0.6F);
		addFeature(new WizardHeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
	}

	@Override
	public void render(WizardEntity wizard, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertices, int light) {
		super.render(wizard, yaw, tickDelta, matrices, vertices, light);

		int hexColor = wizard.getRobeColor();
		float r = (hexColor >> 16 & 255) / 255F;
		float g = (hexColor >> 8 & 255) / 255F;
		float b = (hexColor & 255) / 255F;

		if(wizard.hasCustomName() && wizard.getName().getString().equals("jeb_")) {
			int m = 15;
			int n = wizard.age / m + wizard.getId();
			int o = DyeColor.values().length;
			float f = ((wizard.age % m) + tickDelta) / 15F;
			float[] fs = SheepEntity.getRgbColor(DyeColor.byId(n % o));
			float[] gs = SheepEntity.getRgbColor(DyeColor.byId((n + 1) % o));
			r = fs[0] * (1F - f) + gs[0] * f;
			g = fs[1] * (1F - f) + gs[1] * f;
			b = fs[2] * (1F - f) + gs[2] * f;
		}

		matrices.push();
		setupTransforms(wizard, matrices, 0, MathHelper.lerpAngleDegrees(tickDelta, wizard.prevBodyYaw, wizard.bodyYaw), tickDelta);
		matrices.scale(-1.0F, -1.0F, 1.0F);
		scale(wizard, matrices, tickDelta);
		matrices.translate(0.0, -1.5, 0.0);
		model.render(matrices, vertices.getBuffer(RenderLayer.getEntityCutout(ROBES_TEXTURE)), light, OverlayTexture.DEFAULT_UV, r, g, b, 1F);
		matrices.pop();
	}

	@Override
	public Identifier getTexture(WizardEntity entity) {
		return TEXTURE;
	}
}
