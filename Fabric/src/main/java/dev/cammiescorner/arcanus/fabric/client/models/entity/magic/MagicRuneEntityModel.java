package dev.cammiescorner.arcanus.fabric.client.models.entity.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanus.fabric.common.entities.magic.MagicRuneEntity;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class MagicRuneEntityModel extends EntityModel<MagicRuneEntity> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(Arcanus.id("magic_rune"), "main");
	public final ModelPart rune;

	public MagicRuneEntityModel(ModelPart root) {
		this.rune = root.getChild("rune");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();

		modelPartData.addOrReplaceChild("rune", CubeListBuilder.create().texOffs(-15, 0).addBox(-7.5F, 0F, -7.5F, 15F, 0F, 15F, new CubeDeformation(0F)), PartPose.offset(0F, 1F, 0F));

		return LayerDefinition.create(modelData, 32, 16);
	}

	@Override
	public void setupAnim(MagicRuneEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		rune.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}
