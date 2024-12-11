package dev.cammiescorner.arcanuscontinuum.client.models.feature;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

public class HaloModel<T extends PlayerEntity> extends BipedEntityModel<T> {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(Arcanus.id("halo"), "main");
	public final ModelPart halo;
	public final ModelPart spinny;

	public HaloModel(ModelPart root) {
		super(root);
		this.halo = head.getChild("halo");
		this.spinny = halo.getChild("spinny");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = PlayerEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData root = modelData.getRoot().getChild("head");

		ModelPartData halo = root.addChild("halo", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData spinny = halo.addChild("spinny", ModelPartBuilder.create().uv(-12, 0).cuboid(-6.0F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -11.0F, 0.0F));

		return TexturedModelData.of(modelData, 32, 16);
	}

	@Override
	public void setAngles(T player, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		super.setAngles(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		halo.copyTransform(head);
		halo.pitch = (float) (head.pitch - Math.toRadians(30));

		if(player.isInSneakingPose())
			halo.pivotY += 4.2F;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		halo.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}
