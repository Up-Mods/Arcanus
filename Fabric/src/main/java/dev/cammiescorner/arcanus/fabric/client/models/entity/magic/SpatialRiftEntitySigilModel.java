package dev.cammiescorner.arcanus.fabric.client.models.entity.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class SpatialRiftEntitySigilModel extends EntityModel<Entity> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(FabricMain.id("spatial_rift_sigil"), "main");
	public final ModelPart sigil;

	public SpatialRiftEntitySigilModel(ModelPart root) {
		this.sigil = root.getChild("sigil");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition sigil = modelPartData.addOrReplaceChild("sigil", CubeListBuilder.create().texOffs(-64, 0).addBox(-32.0F, 0.0F, -32.0F, 64.0F, 0.0F, 64.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(modelData, 128, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		sigil.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}
