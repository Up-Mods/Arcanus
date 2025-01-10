package dev.cammiescorner.arcanuscontinuum.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusMobEffects;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ManaWingsFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private static final ResourceLocation TEXTURE = Arcanus.id("/textures/entity/feature/mana_wings.png");
	private final ElytraModel<T> model;

	public ManaWingsFeatureRenderer(RenderLayerParent<T, M> context) {
		super(context);
		this.model = new ElytraModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.ELYTRA));
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertices, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if(entity.hasEffect(ArcanusMobEffects.MANA_WINGS.get())) {

			var color = ArcanusHelper.getMagicColor(entity);

			matrices.pushPose();
			matrices.translate(0.0F, 0.0F, 0.125F);
			getParentModel().copyPropertiesTo(model);
			model.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
			VertexConsumer layer = vertices.getBuffer(ArcanusClient.getMagicCircles(TEXTURE));
			model.renderToBuffer(matrices, layer, light, OverlayTexture.NO_OVERLAY, color.redF(), color.greenF(), color.blueF(), 1.0F);
			matrices.popPose();
		}
	}
}
