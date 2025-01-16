package dev.cammiescorner.arcanus.fabric.client.models.entity.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.cammiescorner.arcanus.fabric.common.entities.magic.AreaOfEffectEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class AreaOfEffectEntityModel extends EntityModel<AreaOfEffectEntity> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(FabricMain.id("area_of_effect"), "main");
	public final ModelPart base;
	public final ModelPart pillar;
	public final ModelPart walls;

	public AreaOfEffectEntityModel(ModelPart root) {
		this.base = root.getChild("base");
		this.pillar = base.getChild("pillar");
		this.walls = base.getChild("walls");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition root = modelData.getRoot();

		PartDefinition base = root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(-72, 80).addBox(-36.0F, 0.0F, -36.0F, 72.0F, 0.0F, 72.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition pillar = base.addOrReplaceChild("pillar", CubeListBuilder.create().texOffs(192, 0).addBox(-8.0F, -40.0F, -8.0F, 16.0F, 40.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition walls = base.addOrReplaceChild("walls", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition wall1 = walls.addOrReplaceChild("wall1", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -32.0F, -24.0F, 48.0F, 32.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition wall2 = walls.addOrReplaceChild("wall2", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -32.0F, -24.0F, 48.0F, 32.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		return LayerDefinition.create(modelData, 256, 256);
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(AreaOfEffectEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}
}
