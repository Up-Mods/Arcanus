package dev.cammiescorner.arcanus.fabric.client.models.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanus.fabric.common.entities.living.OpossumEntity;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class OpossumEntityModel extends EntityModel<OpossumEntity> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(Arcanus.id("opossum"), "main");
	public final ModelPart head;
	public final ModelPart hat;
	public final ModelPart body;
	public final ModelPart leftForeleg;
	public final ModelPart rightForeleg;
	public final ModelPart leftHindleg;
	public final ModelPart rightHindleg;
	public final ModelPart tailBase;
	public final ModelPart tailEnd;

	public OpossumEntityModel(ModelPart root) {
		this.head = root.getChild("head");
		this.hat = head.getChild("hat");
		this.body = root.getChild("body");
		this.leftForeleg = root.getChild("leftForeleg");
		this.rightForeleg = root.getChild("rightForeleg");
		this.leftHindleg = root.getChild("leftHindleg");
		this.rightHindleg = root.getChild("rightHindleg");
		this.tailBase = root.getChild("tailBase");
		this.tailEnd = tailBase.getChild("tailEnd");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = new MeshDefinition();
		PartDefinition root = data.getRoot();

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 2).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(1, 8).addBox(-1.0F, 0.0F, -6.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-3.0F, -3.0F, -1.0F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, -5.0F));
		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 7).addBox(-2.0F, -3.25F, -3.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition hatTip = hat.addOrReplaceChild("hatTip", CubeListBuilder.create().texOffs(48, 7).addBox(-1.0F, -3.75F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, -0.1745F, 0.1745F, 0.0F));
		PartDefinition hatBase = hat.addOrReplaceChild("hatBase", CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, -0.75F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 13).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 5.0F, 8.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 19.0F, -1.0F));

		PartDefinition leftForeleg = root.addOrReplaceChild("leftForeleg", CubeListBuilder.create().texOffs(14, 27).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 21.0F, -4.0F));
		PartDefinition rightForeleg = root.addOrReplaceChild("rightForeleg", CubeListBuilder.create().texOffs(14, 27).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 21.0F, -4.0F));

		PartDefinition leftHindleg = root.addOrReplaceChild("leftHindleg", CubeListBuilder.create().texOffs(6, 27).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 21.0F, 2.0F));
		PartDefinition rightHindleg = root.addOrReplaceChild("rightHindleg", CubeListBuilder.create().texOffs(6, 27).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 21.0F, 2.0F));

		PartDefinition tailBase = root.addOrReplaceChild("tailBase", CubeListBuilder.create().texOffs(20, 8).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 3.0F));
		PartDefinition tailEnd = tailBase.addOrReplaceChild("tailEnd", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 0.5F, 3.0F));

		return LayerDefinition.create(data, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		leftForeleg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		rightForeleg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		leftHindleg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		rightHindleg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		tailBase.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(OpossumEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		head.xRot = headPitch * 0.017453292F;
		head.yRot = netHeadYaw * 0.017453292F;

		leftHindleg.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		rightHindleg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		leftForeleg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		rightForeleg.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		leftHindleg.visible = !entity.isOrderedToSit();
		rightHindleg.visible = !entity.isOrderedToSit();
		leftForeleg.visible = !entity.isOrderedToSit();
		rightForeleg.visible = !entity.isOrderedToSit();

		if(entity.isOrderedToSit()) {
			head.y = 21;
			body.y = 21;
			tailBase.y = 21;
			tailBase.xRot = (float) Math.toRadians(-15);
			tailEnd.xRot = (float) Math.toRadians(7);
		}
		else {
			head.y = 19;
			body.y = 19;
			tailBase.y = 19;
			tailBase.xRot = (float) Math.toRadians(-35);
			tailEnd.xRot = (float) Math.toRadians(15);
		}
	}
}
