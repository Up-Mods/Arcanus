package dev.cammiescorner.arcanus.fabric.client.renderer.armour;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.arcanus.fabric.client.models.armour.BattleMageArmourModel;
import dev.cammiescorner.arcanus.fabric.common.items.BattleMageArmorItem;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
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
import net.minecraft.world.item.ItemStack;

public class BattleMageArmourRenderer implements ArmorRenderer {
	private final Minecraft client = Minecraft.getInstance();
	private final ResourceLocation[] mainTextures = {
			Arcanus.id("textures/entity/armor/battle_mage_armor_stage_0.png"),
			Arcanus.id("textures/entity/armor/battle_mage_armor_stage_1.png"),
			Arcanus.id("textures/entity/armor/battle_mage_armor_stage_2.png"),
			Arcanus.id("textures/entity/armor/battle_mage_armor_stage_3.png")
	};
	private final ResourceLocation overlayTexture = Arcanus.id("textures/entity/armor/battle_mage_armor_overlay.png");
	private BattleMageArmourModel<LivingEntity> model;

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
		if(model == null)
			model = new BattleMageArmourModel<>(client.getEntityModels().bakeLayer(BattleMageArmourModel.MODEL_LAYER));

		if(stack.getItem() instanceof BattleMageArmorItem battleMageArmorItem) {
			ResourceLocation mainTexture = mainTextures[battleMageArmorItem.getOxidation(stack).ordinal()];

			contextModel.copyPropertiesTo(model);
			model.setAllVisible(true);
			model.helmet.visible = slot == EquipmentSlot.HEAD;
			model.chestplate.visible = slot == EquipmentSlot.CHEST;
			model.surcoatFront.visible = slot == EquipmentSlot.CHEST;
			model.surcoatBack.visible = slot == EquipmentSlot.CHEST;
			model.rightGauntlet.visible = slot == EquipmentSlot.CHEST;
			model.leftGauntlet.visible = slot == EquipmentSlot.CHEST;
			model.rightGreaves.visible = slot == EquipmentSlot.LEGS;
			model.leftGreaves.visible = slot == EquipmentSlot.LEGS;
			model.rightBoot.visible = slot == EquipmentSlot.FEET;
			model.leftBoot.visible = slot == EquipmentSlot.FEET;

			model.surcoatFront.xRot = Math.min(contextModel.leftLeg.xRot, contextModel.rightLeg.xRot) - 0.0436f;
			model.surcoatBack.xRot = Math.max(contextModel.leftLeg.xRot, contextModel.rightLeg.xRot) + 0.0436f;

			model.renderToBuffer(matrices, ItemRenderer.getArmorFoilBuffer(vertexConsumers, RenderType.armorCutoutNoCull(mainTexture), false, false), light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
			model.renderToBuffer(matrices, ItemRenderer.getArmorFoilBuffer(vertexConsumers, RenderType.armorCutoutNoCull(overlayTexture), false, false), light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
		}
	}
}
