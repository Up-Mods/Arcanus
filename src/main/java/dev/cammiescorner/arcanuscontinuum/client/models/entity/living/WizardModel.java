package dev.cammiescorner.arcanuscontinuum.client.models.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.entities.living.Wizard;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class WizardModel extends EntityModel<Wizard> implements ArmedModel, HeadedModel {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(Arcanus.id("wizard"), "main");
	public final ModelPart head;
	public final ModelPart leftArm;
	public final ModelPart rightArm;
	public final ModelPart body;
	public final ModelPart leftLeg;
	public final ModelPart rightLeg;

	public WizardModel(ModelPart root) {
		this.head = root.getChild("head");
		this.leftArm = root.getChild("leftArm");
		this.rightArm = root.getChild("rightArm");
		this.body = root.getChild("body");
		this.leftLeg = root.getChild("leftLeg");
		this.rightLeg = root.getChild("rightLeg");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition root = modelData.getRoot();

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.0F, -8.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
			.texOffs(32, 0).addBox(-4.0F, 4.0F, -8.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
			.texOffs(64, 0).addBox(-2.0F, 1.0F, -12.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
			.texOffs(92, 30).addBox(-4.5F, -7.0F, -8.5F, 9.0F, 6.0F, 9.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, 2.0F, -3.0F));

		PartDefinition hatTip = head.addOrReplaceChild("hatTip", CubeListBuilder.create().texOffs(100, 2).addBox(-2.5F, -13.2F, -4.7F, 5.0F, 4.0F, 9.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.0F, 4.0F, -4.0F, -0.6545F, 0.0F, 0.0F));
		PartDefinition hatStalk = head.addOrReplaceChild("hatStalk", CubeListBuilder.create().texOffs(96, 15).addBox(-3.5F, -11.8F, -1.5F, 7.0F, 6.0F, 9.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.0F, 4.0F, -4.0F, 0.2618F, 0.0F, 0.0F));
		PartDefinition hatRim = head.addOrReplaceChild("hatRim", CubeListBuilder.create().texOffs(60, 45).addBox(-9.0F, -6.0F, -8.5F, 17.0F, 2.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, -4.0F, 0.0F, 0.0F, 0.1309F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
			.texOffs(0, 38).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 20.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition leftArm = root.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(28, 22).mirror().addBox(-1.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false)
			.texOffs(30, 38).mirror().addBox(-1.5F, -1.5F, -2.5F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(5.0F, 4.0F, -1.0F));
		PartDefinition leftCuff = leftArm.addOrReplaceChild("leftCuff", CubeListBuilder.create().texOffs(30, 55).mirror().addBox(-2.5F, -4.6F, -0.4F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 10.0F, 2.5F, 0.7418F, 0.0F, 0.0F));

		PartDefinition rightArm = root.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(28, 22).addBox(-3.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.01F))
			.texOffs(30, 38).addBox(-3.5F, -1.5F, -2.5F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offset(-5.0F, 4.0F, -1.0F));
		PartDefinition rightCuff = rightArm.addOrReplaceChild("rightCuff", CubeListBuilder.create().texOffs(30, 55).addBox(-2.5F, -4.6F, -0.4F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 10.0F, 2.5F, 0.7418F, 0.0F, 0.0F));

		PartDefinition leftLeg = root.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(44, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 12.0F, 0.0F));
		PartDefinition rightLeg = root.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(44, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

		return LayerDefinition.create(modelData, 128, 64);
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		leftArm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		rightArm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		leftLeg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		rightLeg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(Wizard wizard, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		rightArm.xRot = Mth.cos(limbAngle * 0.6662F + (float) Math.PI) * 2.0F * limbDistance * 0.5F;
		leftArm.xRot = Mth.cos(limbAngle * 0.6662F) * 2.0F * limbDistance * 0.5F;
		rightArm.yRot = 0;
		leftArm.yRot = 0;

		rightLeg.xRot = Mth.cos(limbAngle * 0.6662F) * 1.4F * limbDistance * 0.5F;
		leftLeg.xRot = Mth.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance * 0.5F;
		head.xRot = (float) Math.toRadians(headPitch);
		head.yRot = (float) Math.toRadians(headYaw);

		if(!wizard.getMainHandItem().isEmpty()) {
			if(wizard.isLeftHanded()) {
				leftArm.xRot = (float) Math.toRadians(-75) + Mth.cos(limbAngle * 0.6662F) * 2F * limbDistance * 0.25F;
				leftArm.yRot = (float) Math.toRadians(-20);
			}
			else {
				rightArm.xRot = (float) Math.toRadians(-75) + Mth.cos(limbAngle * 0.6662F + (float) Math.PI) * 2F * limbDistance * 0.25F;
				rightArm.yRot = (float) Math.toRadians(20);
			}
		}

		leftLeg.xRot = Mth.lerp(wizard.getSwimAmount(animationProgress), leftLeg.xRot, 0.3F * Mth.cos(limbAngle * 0.33333334F + (float) Math.PI));
		rightLeg.xRot = Mth.lerp(wizard.getSwimAmount(animationProgress), rightLeg.xRot, 0.3F * Mth.cos(limbAngle * 0.33333334F));
	}

	@Override
	public ModelPart getHead() {
		return head;
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack matrices) {
		if(arm == HumanoidArm.LEFT)
			leftArm.translateAndRotate(matrices);
		else
			rightArm.translateAndRotate(matrices);
	}
}
