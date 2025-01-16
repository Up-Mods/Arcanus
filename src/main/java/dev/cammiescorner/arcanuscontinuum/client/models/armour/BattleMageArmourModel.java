package dev.cammiescorner.arcanuscontinuum.client.models.armour;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class BattleMageArmourModel<T extends LivingEntity> extends HumanoidModel<T> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(Arcanus.id("battle_mage_armor"), "main");
	public final ModelPart helmet;
	public final ModelPart chestplate;
	public final ModelPart surcoatFront;
	public final ModelPart surcoatBack;
	public final ModelPart rightGauntlet;
	public final ModelPart leftGauntlet;
	public final ModelPart rightGreaves;
	public final ModelPart rightBoot;
	public final ModelPart leftGreaves;
	public final ModelPart leftBoot;

	public BattleMageArmourModel(ModelPart root) {
		super(root);
		helmet = head.getChild("armorHead");
		chestplate = body.getChild("armorBody");
		surcoatFront = chestplate.getChild("surcoatFront");
		surcoatBack = chestplate.getChild("surcoatBack");
		rightGauntlet = rightArm.getChild("armorRightArm");
		leftGauntlet = leftArm.getChild("armorLeftArm");
		rightGreaves = rightLeg.getChild("armorRightLeg");
		leftGreaves = leftLeg.getChild("armorLeftLeg");
		rightBoot = rightLeg.getChild("armorRightBoot");
		leftBoot = leftLeg.getChild("armorLeftBoot");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = data.getRoot();
		PartDefinition head = root.getChild(PartNames.HEAD);
		PartDefinition body = root.getChild(PartNames.BODY);
		PartDefinition rightArm = root.getChild(PartNames.RIGHT_ARM);
		PartDefinition leftArm = root.getChild(PartNames.LEFT_ARM);
		PartDefinition rightLeg = root.getChild(PartNames.RIGHT_LEG);
		PartDefinition leftLeg = root.getChild(PartNames.LEFT_LEG);

		PartDefinition armorHead = head.addOrReplaceChild("armorHead", CubeListBuilder.create().texOffs(18, 223).addBox(0.0F, -17.0F, -7.5F, 0.0F, 15.0F, 18.0F, new CubeDeformation(0.0F))
			.texOffs(61, 130).addBox(-1.0F, -10.0F, -6.0F, 2.0F, 10.0F, 12.0F, new CubeDeformation(0.0F))
			.texOffs(0, 144).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.55F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition armet = armorHead.addOrReplaceChild("armet", CubeListBuilder.create().texOffs(49, 137).addBox(-4.5F, 2.25F, -4.75F, 9.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.0472F, 0.0F, 0.0F));

		PartDefinition armorBody = body.addOrReplaceChild("armorBody", CubeListBuilder.create().texOffs(0, 192).addBox(-4.5F, 9.2F, -2.5F, 9.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
			.texOffs(40, 160).addBox(-5.0F, 0.0F, -2.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.66F))
			.texOffs(0, 160).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.42F))
			.texOffs(0, 204).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.51F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition surcoatFront = armorBody.addOrReplaceChild("surcoatFront", CubeListBuilder.create().texOffs(0, 216).addBox(-4.0F, 0.5F, 0.5F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, 8.5F, -2.5F, -0.0436F, 0.0F, 0.0F));
		PartDefinition surcoatBack = armorBody.addOrReplaceChild("surcoatBack", CubeListBuilder.create().texOffs(20, 216).addBox(-4.0F, 0.5F, -2.5F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, 8.5F, 2.5F, 0.0436F, 0.0F, 0.0F));
		PartDefinition trinketStuff = armorBody.addOrReplaceChild("trinketStuff", CubeListBuilder.create().texOffs(0, 226).addBox(-5.0F, 9.0F, -3.0F, 10.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition armorRightArm = rightArm.addOrReplaceChild("armorRightArm", CubeListBuilder.create().texOffs(60, 191).addBox(-3.0F, -2.0F, -2.0F, 5.0F, 6.0F, 4.0F, new CubeDeformation(0.6F))
			.texOffs(40, 168).addBox(-4.5F, -3.75F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(44, 191).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.41F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition armorLeftArm = leftArm.addOrReplaceChild("armorLeftArm", CubeListBuilder.create().texOffs(60, 191).mirror().addBox(-2.0F, -2.0F, -2.0F, 5.0F, 6.0F, 4.0F, new CubeDeformation(0.6F)).mirror(false)
			.texOffs(40, 168).mirror().addBox(2.5F, -3.75F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
			.texOffs(44, 191).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.41F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition armorRightLeg = rightLeg.addOrReplaceChild("armorRightLeg", CubeListBuilder.create().texOffs(0, 176).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightTasset = armorRightLeg.addOrReplaceChild("rightTasset", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition armorRightBoot = rightLeg.addOrReplaceChild("armorRightBoot", CubeListBuilder.create().texOffs(16, 176).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition armorLeftLeg = leftLeg.addOrReplaceChild("armorLeftLeg", CubeListBuilder.create().texOffs(0, 176).mirror().addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftTasset = armorLeftLeg.addOrReplaceChild("leftTasset", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition armorLeftBoot = leftLeg.addOrReplaceChild("armorLeftBoot", CubeListBuilder.create().texOffs(16, 176).mirror().addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(data, 128, 256);
	}

	@Override
	public void setupAnim(T livingEntity, float f, float g, float h, float i, float j) {
		super.setupAnim(livingEntity, f, g, h, i, j);
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		super.renderToBuffer(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
