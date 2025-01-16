package dev.cammiescorner.arcanus.fabric.client.models.entity.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanus.fabric.common.entities.magic.MagicProjectileEntity;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class MagicProjectileEntityModel extends EntityModel<MagicProjectileEntity> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(FabricMain.id("magic_projectile"), "main");
	private final ModelPart base;
	public final ModelPart ring1;
	public final ModelPart ring2;
	public final ModelPart ring3;

	public MagicProjectileEntityModel(ModelPart root) {
		this.base = root.getChild("base");
		this.ring1 = base.getChild("ring1");
		this.ring2 = base.getChild("ring2");
		this.ring3 = base.getChild("ring3");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition root = modelData.getRoot();

		PartDefinition base = root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, -16).addBox(0.0F, -2.5F, -8.0F, 0.0F, 5.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(-16, 5).addBox(-2.5F, 0.0F, -8.0F, 5.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.5F, 0.0F));
		PartDefinition ring1 = base.addOrReplaceChild("ring1", CubeListBuilder.create().texOffs(10, 5).addBox(-3.5F, -3.5F, 0.0F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.0F));
		PartDefinition ring2 = base.addOrReplaceChild("ring2", CubeListBuilder.create().texOffs(10, 12).addBox(-3.5F, -3.5F, 0.0F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition ring3 = base.addOrReplaceChild("ring3", CubeListBuilder.create().texOffs(10, 19).addBox(-3.5F, -3.5F, 0.0F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));

		return LayerDefinition.create(modelData, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(MagicProjectileEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}
}
