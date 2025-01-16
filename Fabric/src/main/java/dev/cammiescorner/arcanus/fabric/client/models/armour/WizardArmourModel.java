package dev.cammiescorner.arcanus.fabric.client.models.armour;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class WizardArmourModel<T extends LivingEntity> extends HumanoidModel<T> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(Arcanus.id("wizard_robes"), "main");
	public final ModelPart wizardHat;
	public final ModelPart robes;
	public final ModelPart rightSleeve;
	public final ModelPart leftSleeve;
	public final ModelPart rightPants;
	public final ModelPart leftPants;
	public final ModelPart rightBoot;
	public final ModelPart leftBoot;

	public WizardArmourModel(ModelPart root) {
		super(root);
		wizardHat = head.getChild("wizardHat");
		robes = body.getChild("robes");
		rightSleeve = rightArm.getChild("rightSleeve");
		leftSleeve = leftArm.getChild("leftSleeve");
		rightPants = rightLeg.getChild("rightPants");
		leftPants = leftLeg.getChild("leftPants");
		rightBoot = rightLeg.getChild("rightBoot");
		leftBoot = leftLeg.getChild("leftBoot");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition head = data.getRoot().getChild(PartNames.HEAD);
		PartDefinition body = data.getRoot().getChild(PartNames.BODY);
		PartDefinition rightArm = data.getRoot().getChild(PartNames.RIGHT_ARM);
		PartDefinition leftArm = data.getRoot().getChild(PartNames.LEFT_ARM);
		PartDefinition rightLeg = data.getRoot().getChild(PartNames.RIGHT_LEG);
		PartDefinition leftLeg = data.getRoot().getChild(PartNames.LEFT_LEG);

		PartDefinition wizardHat = head.addOrReplaceChild("wizardHat", CubeListBuilder.create().texOffs(51, 64).addBox(-4.5F, -11F, -4.5F, 9F, 6F, 9F, new CubeDeformation(0.05F)), PartPose.offset(0F, 0F, 0F));
		PartDefinition hatTip = wizardHat.addOrReplaceChild("hatTip", CubeListBuilder.create().texOffs(100, 79).addBox(-2.5F, -13.2F, -4.7F, 5F, 4F, 9F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0F, 0F, 0F, -0.6545F, 0F, 0F));
		PartDefinition hatStalk = wizardHat.addOrReplaceChild("hatStalk", CubeListBuilder.create().texOffs(87, 64).addBox(-3.5F, -11.8F, -1.5F, 7F, 6F, 9F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0.2618F, 0F, 0F));
		PartDefinition hatRim = wizardHat.addOrReplaceChild("hatRim", CubeListBuilder.create().texOffs(0, 64).addBox(-9F, -6F, -8.5F, 17F, 2F, 17F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0.1309F));

		PartDefinition robes = body.addOrReplaceChild("robes", CubeListBuilder.create().texOffs(0, 83).addBox(-4.5F, -0.5F, -2.5F, 9F, 13F, 5F, new CubeDeformation(0.05F)).texOffs(28, 83).addBox(-5F, -0.5F, -2.5F, 10F, 13F, 5F, new CubeDeformation(0.1F)), PartPose.offset(0F, 0F, 0F));
		PartDefinition leftFlap = robes.addOrReplaceChild("leftFlap", CubeListBuilder.create().texOffs(36, 101).addBox(-2.5F, 12.6F, -0.95F, 5F, 9F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0.2618F, 1.5708F, 0F));
		PartDefinition rightFlap = robes.addOrReplaceChild("rightFlap", CubeListBuilder.create().texOffs(22, 101).addBox(-2.5F, 12.6F, -0.95F, 5F, 9F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0.2618F, -1.5708F, 0F));
		PartDefinition backFlap = robes.addOrReplaceChild("backFlap", CubeListBuilder.create().texOffs(0, 101).addBox(-4.5F, 12.1F, -2.75F, 9F, 9F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0.2618F, 0F, 0F));

		PartDefinition rightSleeve = rightArm.addOrReplaceChild("rightSleeve", CubeListBuilder.create().texOffs(58, 84).addBox(-3.5F, -2.5F, -2.5F, 5F, 12F, 5F, new CubeDeformation(0.01F)), PartPose.offset(0F, 0F, 0F));
		PartDefinition rightCuff = rightSleeve.addOrReplaceChild("rightCuff", CubeListBuilder.create().texOffs(78, 92).addBox(-2.5F, -4.6F, -0.3F, 5F, 5F, 4F, new CubeDeformation(0F)), PartPose.offsetAndRotation(-1F, 9F, 2.5F, 0.7418F, 0F, 0F));
		PartDefinition leftSleeve = leftArm.addOrReplaceChild("leftSleeve", CubeListBuilder.create().texOffs(58, 84).mirror().addBox(-1.5F, -2.5F, -2.5F, 5F, 12F, 5F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(0F, 0F, 0F));
		PartDefinition leftCuff = leftSleeve.addOrReplaceChild("leftCuff", CubeListBuilder.create().texOffs(78, 92).mirror().addBox(-2.5F, -4.6F, -0.3F, 5F, 5F, 4F, new CubeDeformation(0F)).mirror(false), PartPose.offsetAndRotation(1F, 9F, 2.5F, 0.7418F, 0F, 0F));

		PartDefinition rightPants = rightLeg.addOrReplaceChild("rightPants", CubeListBuilder.create().texOffs(0, 112).addBox(-2F, 0F, -2F, 4F, 12F, 4F, new CubeDeformation(0.3F)), PartPose.offset(0F, 0F, 0F));
		PartDefinition leftPants = leftLeg.addOrReplaceChild("leftPants", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(-2F, 0F, -2F, 4F, 12F, 4F, new CubeDeformation(0.3F)).mirror(false), PartPose.offset(0F, 0F, 0F));

		PartDefinition rightBoot = rightLeg.addOrReplaceChild("rightBoot", CubeListBuilder.create().texOffs(16, 112).addBox(-2F, 0F, -2F, 4F, 12F, 4F, new CubeDeformation(0.4F)), PartPose.offset(0F, 0F, 0F));
		PartDefinition leftBoot = leftLeg.addOrReplaceChild("leftBoot", CubeListBuilder.create().texOffs(16, 112).mirror().addBox(-2F, 0F, -2F, 4F, 12F, 4F, new CubeDeformation(0.4F)).mirror(false), PartPose.offset(0F, 0F, 0F));

		return LayerDefinition.create(data, 128, 128);
	}

	@Override
	public void setupAnim(T livingEntity, float f, float g, float h, float i, float j) {
		super.setupAnim(livingEntity, f, g, h, i, j);
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		super.renderToBuffer(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}
