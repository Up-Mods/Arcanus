package dev.cammiescorner.arcanus.fabric.client.models.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanus.api.spells.Pattern;
import dev.cammiescorner.arcanus.fabric.common.items.StaffItem;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.fabric.common.util.StaffType;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class SpellPatternModel<T extends Player> extends HumanoidModel<T> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(Arcanus.id("spell_pattern"), "main");
	private final Minecraft client = Minecraft.getInstance();
	private final ModelPart base;
	public final ModelPart first;
	private final ModelPart left1;
	private final ModelPart right1;
	public final ModelPart second;
	private final ModelPart left2;
	private final ModelPart right2;
	public final ModelPart third;
	private final ModelPart left3;
	private final ModelPart right3;

	public SpellPatternModel(ModelPart root) {
		super(root);
		this.base = root.getChild("body").getChild("base");
		this.first = base.getChild("first");
		this.left1 = first.getChild("left1");
		this.right1 = first.getChild("right1");
		this.second = base.getChild("second");
		this.left2 = second.getChild("left2");
		this.right2 = second.getChild("right2");
		this.third = base.getChild("third");
		this.left3 = third.getChild("left3");
		this.right3 = third.getChild("right3");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = PlayerModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = modelData.getRoot().getChild("body");


		PartDefinition base = root.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0F, 0F, 0F));

		PartDefinition first = base.addOrReplaceChild("first", CubeListBuilder.create(), PartPose.offset(0F, -6F, -8F));
		PartDefinition left1 = first.addOrReplaceChild("left1", CubeListBuilder.create().texOffs(0, 0).addBox(-8.5F, -8.5F, 0F, 17F, 17F, 0F, new CubeDeformation(0F)), PartPose.offset(0F, 0F, 0F));
		PartDefinition right1 = first.addOrReplaceChild("right1", CubeListBuilder.create().texOffs(0, 24).addBox(-8.5F, -8.5F, 0F, 17F, 17F, 0F, new CubeDeformation(0F)), PartPose.offset(0F, 0F, 0F));

		PartDefinition second = base.addOrReplaceChild("second", CubeListBuilder.create(), PartPose.offset(0F, -6F, -11F));
		PartDefinition left2 = second.addOrReplaceChild("left2", CubeListBuilder.create().texOffs(34, 0).addBox(-8.5F, -8.5F, 0F, 17F, 17F, 0F, new CubeDeformation(0F)), PartPose.offset(0F, 0F, 0F));
		PartDefinition right2 = second.addOrReplaceChild("right2", CubeListBuilder.create().texOffs(34, 24).addBox(-8.5F, -8.5F, 0F, 17F, 17F, 0F, new CubeDeformation(0F)), PartPose.offset(0F, 0F, 0F));

		PartDefinition third = base.addOrReplaceChild("third", CubeListBuilder.create(), PartPose.offset(0F, -6F, -14F));
		PartDefinition left3 = third.addOrReplaceChild("left3", CubeListBuilder.create().texOffs(68, 0).addBox(-8.5F, -8.5F, 0F, 17F, 17F, 0F, new CubeDeformation(0F)), PartPose.offset(0F, 0F, 0F));
		PartDefinition right3 = third.addOrReplaceChild("right3", CubeListBuilder.create().texOffs(68, 24).addBox(-8.5F, -8.5F, 0F, 17F, 17F, 0F, new CubeDeformation(0F)), PartPose.offset(0F, 0F, 0F));

		return LayerDefinition.create(modelData, 128, 48);
	}

	@Override
	public void setupAnim(T player, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		super.setupAnim(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		base.y = 0;
		base.x = 0;
		base.z = 0;
		first.zRot = (float) Math.toRadians((player.tickCount + player.getId() + client.getFrameTime()) * 5);
		second.zRot = (float) Math.toRadians((player.tickCount + player.getId() + client.getFrameTime()) * -8);
		third.zRot = (float) Math.toRadians((player.tickCount + player.getId() + client.getFrameTime()) * 11);

		if(player.isCrouching()) {
			base.y = 4.2F;

			if(ArcanusComponents.isCasting(player) && player.getMainHandItem().getItem() instanceof StaffItem staff && staff.staffType == StaffType.STAFF) {
				base.y = 8;
				base.x = 4;
				base.z = 3;
			}
		}
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	public void showMagicCircles(List<Pattern> pattern) {
		first.visible = pattern.size() > 0;
		second.visible = pattern.size() > 1;
		third.visible = pattern.size() > 2;

		if(first.visible) {
			left1.visible = pattern.get(0) == Pattern.LEFT;
			right1.visible = pattern.get(0) == Pattern.RIGHT;
		}
		if(second.visible) {
			left2.visible = pattern.get(1) == Pattern.LEFT;
			right2.visible = pattern.get(1) == Pattern.RIGHT;
		}
		if(third.visible) {
			left3.visible = pattern.get(2) == Pattern.LEFT;
			right3.visible = pattern.get(2) == Pattern.RIGHT;
		}
	}
}
