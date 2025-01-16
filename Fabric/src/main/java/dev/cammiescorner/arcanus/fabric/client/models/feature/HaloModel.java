package dev.cammiescorner.arcanus.fabric.client.models.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.player.Player;

public class HaloModel<T extends Player> extends HumanoidModel<T> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(Arcanus.id("halo"), "main");
	public final ModelPart halo;
	public final ModelPart spinny;

	public HaloModel(ModelPart root) {
		super(root);
		this.halo = head.getChild("halo");
		this.spinny = halo.getChild("spinny");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = PlayerModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = modelData.getRoot().getChild("head");

		PartDefinition halo = root.addOrReplaceChild("halo", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition spinny = halo.addOrReplaceChild("spinny", CubeListBuilder.create().texOffs(-12, 0).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));

		return LayerDefinition.create(modelData, 32, 16);
	}

	@Override
	public void setupAnim(T player, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		super.setupAnim(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		halo.copyFrom(head);
		halo.xRot = (float) (head.xRot - Math.toRadians(30));

		if(player.isCrouching())
			halo.y += 4.2F;
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		halo.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}
