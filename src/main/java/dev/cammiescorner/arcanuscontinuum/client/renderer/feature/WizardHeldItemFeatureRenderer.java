package dev.cammiescorner.arcanuscontinuum.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.arcanuscontinuum.common.entities.living.WizardEntity;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WizardHeldItemFeatureRenderer<T extends WizardEntity, M extends EntityModel<T> & ArmedModel & HeadedModel> extends ItemInHandLayer<T, M> {
	private final ItemInHandRenderer itemRenderer;
	private static final float HEAD_YAW = (float) (-Math.PI / 6);
	private static final float HEAD_ROLL = (float) (Math.PI / 2);

	public WizardHeldItemFeatureRenderer(RenderLayerParent<T, M> featureRendererContext, ItemInHandRenderer heldItemRenderer) {
		super(featureRendererContext, heldItemRenderer);
		this.itemRenderer = heldItemRenderer;
	}

	@Override
	protected void renderArmWithItem(LivingEntity entity, ItemStack stack, ItemDisplayContext transformationMode, HumanoidArm arm, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		if(stack.is(Items.SPYGLASS) && entity.getUseItem() == stack && entity.swingTime == 0)
			renderSpyglass(entity, stack, arm, matrices, vertexConsumers, light);
		else
			super.renderArmWithItem(entity, stack, transformationMode, arm, matrices, vertexConsumers, light);
	}

	private void renderSpyglass(LivingEntity entity, ItemStack stack, HumanoidArm arm, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		matrices.pushPose();
		ModelPart modelPart = getParentModel().getHead();
		float pitch = modelPart.xRot;
		modelPart.xRot = Mth.clamp(modelPart.xRot, HEAD_YAW, HEAD_ROLL);
		modelPart.translateAndRotate(matrices);
		modelPart.xRot = pitch;
		CustomHeadLayer.translateToHead(matrices, false);
		boolean bl = arm == HumanoidArm.LEFT;
		matrices.translate((bl ? -2.5F : 2.5F) / 16F, -0.0625, 0);
		itemRenderer.renderItem(entity, stack, ItemDisplayContext.HEAD, false, matrices, vertexConsumers, light);
		matrices.popPose();
	}
}
