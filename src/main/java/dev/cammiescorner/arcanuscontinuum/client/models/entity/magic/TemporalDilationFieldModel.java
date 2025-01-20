package dev.cammiescorner.arcanuscontinuum.client.models.entity.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.TemporalDilationField;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class TemporalDilationFieldModel extends EntityModel<TemporalDilationField> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(Arcanus.id("temporal_dilation_field"), "main");
	private final ModelPart root;
	public final ModelPart xPlaneRing;
	public final ModelPart xClockHand;
	public final ModelPart yPlaneRing;
	public final ModelPart yClockHand;
//	public final ModelPart zPlaneRing;
//	public final ModelPart zClockHand;

	public TemporalDilationFieldModel(ModelPart root) {
		this.root = root.getChild("root");
		this.xPlaneRing = this.root.getChild("xPlaneRing");
		this.xClockHand = this.xPlaneRing.getChild("xClockHand");
		this.yPlaneRing = this.root.getChild("yPlaneRing");
		this.yClockHand = this.yPlaneRing.getChild("yClockHand");
//		this.zPlaneRing = this.root.getChild("zPlaneRing");
//		this.zClockHand = this.zPlaneRing.getChild("zClockHand");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition xPlaneRing = root.addOrReplaceChild("xPlaneRing", CubeListBuilder.create().texOffs(0, -55).addBox(0.0F, -27.5F, -27.5F, 0.0F, 55.0F, 55.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition xClockHand = xPlaneRing.addOrReplaceChild("xClockHand", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -27.5F, -27.5F, 0.0F, 55.0F, 55.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition yPlaneRing = root.addOrReplaceChild("yPlaneRing", CubeListBuilder.create().texOffs(-55, 0).addBox(-27.5F, 0.0F, -27.5F, 55.0F, 0.0F, 55.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition yClockHand = yPlaneRing.addOrReplaceChild("yClockHand", CubeListBuilder.create().texOffs(-55, 55).addBox(-27.5F, 0.0F, -27.5F, 55.0F, 0.0F, 55.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

//		PartDefinition zPlaneRing = root.addOrReplaceChild("zPlaneRing", CubeListBuilder.create().texOffs(0, 0).addBox(-27.5F, -27.5F, 0.0F, 55.0F, 55.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
//		PartDefinition zClockHand = zPlaneRing.addOrReplaceChild("zClockHand", CubeListBuilder.create().texOffs(0, 55).addBox(-27.5F, -27.5F, 0.0F, 55.0F, 55.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 110, 110);
	}

	@Override
	public void setupAnim(TemporalDilationField entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
