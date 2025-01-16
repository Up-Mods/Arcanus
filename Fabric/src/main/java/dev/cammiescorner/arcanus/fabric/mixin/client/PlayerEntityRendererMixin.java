package dev.cammiescorner.arcanus.fabric.mixin.client;

import dev.cammiescorner.arcanus.fabric.client.renderer.feature.HaloFeatureRenderer;
import dev.cammiescorner.arcanus.fabric.client.renderer.feature.SpellPatternFeatureRenderer;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusMobEffects;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	public PlayerEntityRendererMixin(EntityRendererProvider.Context ctx, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void arcanus$init(EntityRendererProvider.Context context, boolean bl, CallbackInfo info) {
		addLayer(new SpellPatternFeatureRenderer<>(this));
		addLayer(new HaloFeatureRenderer<>(this));
	}

	@Inject(method = "getTextureLocation(Lnet/minecraft/client/player/AbstractClientPlayer;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
	private void arcanus$getTexture(AbstractClientPlayer player, CallbackInfoReturnable<ResourceLocation> info) {
		if(player.hasEffect(ArcanusMobEffects.ANONYMITY.get()))
			info.setReturnValue(Arcanus.id("textures/entity/player/anonymous.png"));
	}
}