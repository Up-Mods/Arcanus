package dev.cammiescorner.arcanuscontinuum.client.renderer.armour;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.models.armour.WizardArmourModel;
import dev.cammiescorner.arcanuscontinuum.common.items.WizardArmorItem;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class WizardArmourRenderer implements ArmorRenderer {
	private final Minecraft client = Minecraft.getInstance();
	private final ResourceLocation mainTexture = Arcanus.id("textures/entity/armor/wizard_robes.png");
	private final ResourceLocation overlayTexture = Arcanus.id("textures/entity/armor/wizard_robes_overlay.png");
	private WizardArmourModel<LivingEntity> model;

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
		if(model == null)
			model = new WizardArmourModel<>(client.getEntityModels().bakeLayer(WizardArmourModel.MODEL_LAYER));

		if(stack.getItem() instanceof WizardArmorItem wizardArmour) {
			int hexColour = wizardArmour.getColor(stack);
			float r = (hexColour >> 16 & 255) / 255F;
			float g = (hexColour >> 8 & 255) / 255F;
			float b = (hexColour & 255) / 255F;

			if(stack.hasCustomHoverName() && stack.getHoverName().getString().equals("jeb_")) {
				int m = 15;
				int n = entity.tickCount / m + entity.getId();
				int o = DyeColor.values().length;
				float f = ((entity.tickCount % m) + client.getFrameTime()) / 15F;
				float[] fs = Sheep.getColorArray(DyeColor.byId(n % o));
				float[] gs = Sheep.getColorArray(DyeColor.byId((n + 1) % o));
				r = fs[0] * (1F - f) + gs[0] * f;
				g = fs[1] * (1F - f) + gs[1] * f;
				b = fs[2] * (1F - f) + gs[2] * f;
			}

			contextModel.copyPropertiesTo(model);
			model.setAllVisible(true);
			model.wizardHat.visible = slot == EquipmentSlot.HEAD;
			model.robes.visible = slot == EquipmentSlot.CHEST;
			model.rightSleeve.visible = slot == EquipmentSlot.CHEST;
			model.leftSleeve.visible = slot == EquipmentSlot.CHEST;
			model.rightPants.visible = slot == EquipmentSlot.LEGS;
			model.leftPants.visible = slot == EquipmentSlot.LEGS;
			model.rightBoot.visible = slot == EquipmentSlot.FEET;
			model.leftBoot.visible = slot == EquipmentSlot.FEET;

			model.renderToBuffer(matrices, ItemRenderer.getArmorFoilBuffer(vertexConsumers, RenderType.armorCutoutNoCull(mainTexture), false, false), light, OverlayTexture.NO_OVERLAY, r, g, b, 1F);
			model.renderToBuffer(matrices, ItemRenderer.getArmorFoilBuffer(vertexConsumers, RenderType.armorCutoutNoCull(overlayTexture), false, false), light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
		}
	}
}
