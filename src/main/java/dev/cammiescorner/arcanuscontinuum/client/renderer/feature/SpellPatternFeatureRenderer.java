package dev.cammiescorner.arcanuscontinuum.client.renderer.feature;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.models.feature.SpellPatternModel;
import dev.cammiescorner.arcanuscontinuum.common.items.StaffItem;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.cammiescorner.arcanuscontinuum.common.util.StaffType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.MathHelper;

public class SpellPatternFeatureRenderer<T extends PlayerEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	private static final Identifier TEXTURE = Arcanus.id("textures/entity/feature/magic_circles.png");
	private final MinecraftClient client = MinecraftClient.getInstance();
	private final SpellPatternModel<PlayerEntity> model;

	public SpellPatternFeatureRenderer(FeatureRendererContext<T, M> context) {
		super(context);
		model = new SpellPatternModel<>(client.getEntityModelLoader().getModelPart(SpellPatternModel.MODEL_LAYER));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertices, int light, PlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		ItemStack stack = player.getMainHandStack();
		Color color = ArcanusHelper.getMagicColor(player);

		model.showMagicCircles(ArcanusComponents.getPattern(player));

		matrices.push();
		matrices.translate(0, 0.65, -0.35);

		model.setAngles(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		model.showMagicCircles(ArcanusComponents.getPattern(player));

		if(ArcanusComponents.isCasting(player) && stack.getItem() instanceof StaffItem item) {
			if(item.staffType == StaffType.STAFF) {
				matrices.translate(player.getMainArm() == Arm.RIGHT ? -0.35 : 0.35, 0, 0.05);
				matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(player.getMainArm() == Arm.RIGHT ? 65 : -65));
			}
			else if(item.staffType == StaffType.WAND) {
				matrices.translate(player.getMainArm() == Arm.RIGHT ? -0.35 : 0.35, -0.05, -0.1);
			}
			else if(item.staffType == StaffType.GAUNTLET) {
				matrices.translate(player.getMainArm() == Arm.RIGHT ? -0.35 : 0.35, -0.05, 0.4);
			}
			else if(item.staffType == StaffType.BOOK) {
				matrices.translate(player.getMainArm() == Arm.RIGHT ? 0.35 : -0.35, -0.05, 0.4);
			}
			else if(item.staffType == StaffType.GUN) {
				matrices.translate(0, -0.15, 0.15);
			}
		}

		matrices.push();
		matrices.translate(0, 0, MathHelper.sin((player.age + player.getId() + client.getTickDelta()) / (MathHelper.PI * 2)) * 0.05F);
		model.first.render(matrices, vertices.getBuffer(ArcanusClient.getMagicCircles(TEXTURE)), LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, color.redF(), color.greenF(), color.blueF(), color.alphaF());
		matrices.pop();

		matrices.push();
		matrices.translate(0, 0, MathHelper.cos((player.age + player.getId() + client.getTickDelta()) / (MathHelper.PI * 2)) * 0.05F);
		model.second.render(matrices, vertices.getBuffer(ArcanusClient.getMagicCircles(TEXTURE)), LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, color.redF(), color.greenF(), color.blueF(), color.alphaF());
		matrices.pop();

		matrices.push();
		matrices.translate(0, 0, MathHelper.sin((player.age + player.getId() + client.getTickDelta()) / (MathHelper.PI * 2)) * 0.05F);
		model.third.render(matrices, vertices.getBuffer(ArcanusClient.getMagicCircles(TEXTURE)), LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, color.redF(), color.greenF(), color.blueF(), color.alphaF());
		matrices.pop();

		matrices.pop();
	}
}
