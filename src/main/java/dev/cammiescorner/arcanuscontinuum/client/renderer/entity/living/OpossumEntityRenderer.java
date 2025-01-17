package dev.cammiescorner.arcanuscontinuum.client.renderer.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.living.OpossumEntityModel;
import dev.cammiescorner.arcanuscontinuum.common.entities.living.OpossumEntity;
import dev.cammiescorner.arcanuscontinuum.common.items.WizardRobesArmorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class OpossumEntityRenderer extends MobRenderer<OpossumEntity, OpossumEntityModel> {
	public static final ResourceLocation TEXTURE = Arcanus.id("textures/entity/living/opossum.png");
	public static final ResourceLocation HAT_TEXTURE = Arcanus.id("textures/entity/living/opossum_hat.png");

	public OpossumEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new OpossumEntityModel(Minecraft.getInstance().getEntityModels().bakeLayer(OpossumEntityModel.MODEL_LAYER)), 0.3F);
	}

	@Override
	public void render(OpossumEntity opossum, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource verteces, int i) {
		ItemStack hatStack = opossum.getItemBySlot(EquipmentSlot.HEAD);
		model.hat.visible = !hatStack.isEmpty();

		super.render(opossum, yaw, tickDelta, matrices, verteces, i);

		if(hatStack.getItem() instanceof WizardRobesArmorItem wizardArmour) {
			int hexColour = wizardArmour.getColor(hatStack);
			float r = (hexColour >> 16 & 255) / 255F;
			float g = (hexColour >> 8 & 255) / 255F;
			float b = (hexColour & 255) / 255F;

			if(hatStack.hasCustomHoverName() && hatStack.getHoverName().getString().equals("jeb_")) {
				int m = 15;
				int n = opossum.tickCount / m + opossum.getId();
				int o = DyeColor.values().length;
				float f = ((opossum.tickCount % m) + tickDelta) / 15F;
				float[] fs = Sheep.getColorArray(DyeColor.byId(n % o));
				float[] gs = Sheep.getColorArray(DyeColor.byId((n + 1) % o));
				r = fs[0] * (1F - f) + gs[0] * f;
				g = fs[1] * (1F - f) + gs[1] * f;
				b = fs[2] * (1F - f) + gs[2] * f;
			}

			matrices.pushPose();
			setupRotations(opossum, matrices, 0, Mth.rotLerp(tickDelta, opossum.yBodyRotO, opossum.yBodyRot), tickDelta);
			matrices.scale(-1.0F, -1.0F, 1.0F);
			scale(opossum, matrices, tickDelta);
			matrices.translate(0.0, -1.5, 0.0);
			model.renderToBuffer(matrices, verteces.getBuffer(RenderType.entityCutout(HAT_TEXTURE)), i, OverlayTexture.NO_OVERLAY, r, g, b, 1F);
			matrices.popPose();
		}
	}

	@Override
	public ResourceLocation getTextureLocation(OpossumEntity entity) {
		return TEXTURE;
	}
}
