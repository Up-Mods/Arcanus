package dev.cammiescorner.arcanuscontinuum.mixin.client;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.renderer.feature.LotusHaloFeatureRenderer;
import dev.cammiescorner.arcanuscontinuum.client.renderer.feature.SpellPatternFeatureRenderer;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusStatusEffects;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) { super(ctx, model, shadowRadius); }

	@Inject(method = "<init>", at = @At("TAIL"))
	private void arcanuscontinuum$init(EntityRendererFactory.Context context, boolean bl, CallbackInfo info) {
		addFeature(new SpellPatternFeatureRenderer<>(this));
		addFeature(new LotusHaloFeatureRenderer<>(this));
	}

	@Inject(method = "getTexture(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
	private void arcanuscontinuum$getTexture(AbstractClientPlayerEntity player, CallbackInfoReturnable<Identifier> info) {
		if(player.hasStatusEffect(ArcanusStatusEffects.ANONYMITY.get()))
			info.setReturnValue(Arcanus.id("textures/entity/player/anonymous.png"));
	}
}
