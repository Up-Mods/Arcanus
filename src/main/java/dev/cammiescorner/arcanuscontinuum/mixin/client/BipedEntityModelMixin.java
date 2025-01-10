package dev.cammiescorner.arcanuscontinuum.mixin.client;

import dev.cammiescorner.arcanuscontinuum.common.items.StaffItem;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.StaffType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {
	@Shadow
	@Final
	public ModelPart rightArm;
	@Shadow
	@Final
	public ModelPart leftArm;
	@Shadow
	@Final
	public ModelPart head;

	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE_ASSIGN",
																							target = "Lnet/minecraft/util/Mth;cos(F)F",
																							ordinal = 1
	))
	private void arcanuscontinuum$modifyArmSwing(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
		if(!livingEntity.isSprinting() && !livingEntity.isSwimming() && !livingEntity.isFallFlying()) {
			Minecraft client = Minecraft.getInstance();
			ItemStack rightStack = client.options.mainHand().get() == HumanoidArm.RIGHT ? livingEntity.getMainHandItem() : livingEntity.getOffhandItem();
			ItemStack leftStack = client.options.mainHand().get() == HumanoidArm.RIGHT ? livingEntity.getOffhandItem() : livingEntity.getMainHandItem();

			if(rightStack.getItem() instanceof StaffItem item && item.staffType == StaffType.STAFF)
				rightArm.xRot *= 0.5F;
			if(leftStack.getItem() instanceof StaffItem item && item.staffType == StaffType.STAFF)
				leftArm.xRot *= 0.5F;
		}
	}

	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
	private void arcanuscontinuum$staffRunningAnim(T entity, float f, float g, float h, float i, float j, CallbackInfo info) {
		if(entity.isSprinting() && ArcanusComponents.CASTING_COMPONENT.isProvidedBy(entity) && !ArcanusComponents.isCasting(entity) && !entity.isSwimming() && !entity.isFallFlying()) {
			Minecraft client = Minecraft.getInstance();
			ItemStack rightStack = client.options.mainHand().get() == HumanoidArm.RIGHT ? entity.getMainHandItem() : entity.getOffhandItem();
			ItemStack leftStack = client.options.mainHand().get() == HumanoidArm.RIGHT ? entity.getOffhandItem() : entity.getMainHandItem();

			if(rightStack.getItem() instanceof StaffItem item && item.staffType == StaffType.STAFF) {
				rightArm.zRot = rightArm.zRot * 0.5F - 1.0472F;
				rightArm.xRot = rightArm.xRot * 0.25F - 0.698132F;

				leftArm.zRot = -leftArm.zRot * 0.5F - 0.261799F;
				leftArm.xRot = -leftArm.xRot * 0.25F - 0.436332F;
			}

			if(leftStack.getItem() instanceof StaffItem item && item.staffType == StaffType.STAFF) {
				leftArm.zRot = leftArm.zRot * 0.5F + 1.0472F;
				leftArm.xRot = leftArm.xRot * 0.25F - 0.698132F;

				rightArm.zRot = -rightArm.zRot * 0.5F + 0.261799F;
				rightArm.xRot = -rightArm.xRot * 0.25F - 0.436332F;
			}
		}
	}

	@Inject(method = "poseRightArm", at = @At(value = "FIELD",
											  target = "Lnet/minecraft/client/model/geom/ModelPart;xRot:F",
											  ordinal = 2
	), cancellable = true)
	private void arcanuscontinuum$positionRightArm(T entity, CallbackInfo info) {
		Minecraft client = Minecraft.getInstance();
		ItemStack rightStack = client.options.mainHand().get() == HumanoidArm.RIGHT ? entity.getMainHandItem() : entity.getOffhandItem();

		if(rightStack.getItem() instanceof StaffItem item) {
			if(ArcanusComponents.CASTING_COMPONENT.isProvidedBy(entity) && ArcanusComponents.isCasting(entity)) {
				switch(item.staffType) {
					case STAFF -> {
						head.yRot = head.yRot + 1.13446F;
						rightArm.xRot = -1.13446F;
						rightArm.zRot = -1.13446F;
						rightArm.yRot = 0.610865F;
						leftArm.xRot = -0.349066F;
						leftArm.yRot = -0.610865F;
					}
					case BOOK -> {
						rightArm.xRot = rightArm.xRot * 0.5F - (float) (Math.PI / 10);
						leftArm.xRot = -1.39626F;
					}
					case GUN -> {
						rightArm.xRot = -1.309F;
						leftArm.xRot = -1.309F;
						rightArm.yRot = -0.785398F;
						leftArm.yRot = 0.785398F;
					}
					case WAND, GAUNTLET -> {
						rightArm.xRot = -1.309F;
					}
				}
			}
			else {
				if(item.staffType == StaffType.STAFF)
					rightArm.xRot = rightArm.xRot * 0.5F - 1.22173F;
				else
					rightArm.xRot = rightArm.xRot * 0.5F - (float) (Math.PI / 10);

				rightArm.yRot = 0F;
			}

			info.cancel();
		}
	}

	@Inject(method = "poseLeftArm", at = @At(value = "FIELD",
											 target = "Lnet/minecraft/client/model/geom/ModelPart;xRot:F",
											 ordinal = 2
	), cancellable = true)
	private void arcanuscontinuum$positionLeftArm(T entity, CallbackInfo info) {
		Minecraft client = Minecraft.getInstance();
		ItemStack leftStack = client.options.mainHand().get() == HumanoidArm.RIGHT ? entity.getOffhandItem() : entity.getMainHandItem();

		if(leftStack.getItem() instanceof StaffItem item) {
			if(ArcanusComponents.CASTING_COMPONENT.isProvidedBy(entity) && ArcanusComponents.isCasting(entity)) {
				switch(item.staffType) {
					case STAFF -> {
						head.yRot = head.yRot * 0.5F - 1.13446F;
						leftArm.xRot = -1.13446F;
						leftArm.zRot = 1.13446F;
						leftArm.yRot = -0.610865F;
						rightArm.xRot = -0.349066F;
						rightArm.yRot = 0.610865F;
					}
					case BOOK -> {
						leftArm.xRot = leftArm.xRot * 0.5F - (float) (Math.PI / 10);
						rightArm.xRot = -1.39626F;
					}
					case GUN -> {
						leftArm.xRot = -1.309F;
						rightArm.xRot = -1.309F;
						leftArm.yRot = 0.785398F;
						rightArm.yRot = -0.785398F;
					}
					case WAND, GAUNTLET -> {
						leftArm.xRot = -1.309F;
					}
				}
			}
			else {
				if(item.staffType == StaffType.STAFF)
					leftArm.xRot = leftArm.xRot * 0.5F - 1.22173F;
				else
					leftArm.xRot = leftArm.xRot * 0.5F - (float) (Math.PI / 10);

				leftArm.yRot = 0.0F;
			}

			info.cancel();
		}
	}
}
