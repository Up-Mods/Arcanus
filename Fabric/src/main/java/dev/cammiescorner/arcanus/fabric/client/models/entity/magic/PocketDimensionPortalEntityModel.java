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

public class PocketDimensionPortalEntityModel extends EntityModel<Entity> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(Arcanus.id("pocket_dimension_portal"), "main");
	private final ModelPart box;
	public final ModelPart skybox;

	public PocketDimensionPortalEntityModel(ModelPart root) {
		this.box = root.getChild("box");
		this.skybox = root.getChild("skybox");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = new MeshDefinition();
		PartDefinition root = data.getRoot();

		PartDefinition box = root.addOrReplaceChild("box", CubeListBuilder.create().texOffs(4, 5).addBox(-24.0F, -32.0F, -24.0F, 48.0F, 32.0F, 48.0F, new CubeDeformation(0.0F))
				.texOffs(0, 85).addBox(-25.0F, -32.0F, -25.0F, 50.0F, 33.0F, 50.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition skybox = root.addOrReplaceChild("skybox", CubeListBuilder.create().texOffs(0, 192).addBox(-32.0F, 1.25F, -32.0F, 64.0F, 0.0F, 64.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(data, 256, 320);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		box.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}
