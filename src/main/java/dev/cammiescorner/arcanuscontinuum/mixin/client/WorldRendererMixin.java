package dev.cammiescorner.arcanuscontinuum.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cammiescorner.arcanuscontinuum.client.utils.EntityRendererSorter;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin {
	@ModifyExpressionValue(method = "renderLevel", at = @At(value = "INVOKE",
															target = "Lnet/minecraft/client/multiplayer/ClientLevel;entitiesForRendering()Ljava/lang/Iterable;"
	))
	private Iterable<Entity> arcanuscontinuum$sortEntitiesForRendering(Iterable<Entity> entities) {
		return EntityRendererSorter.ENTITY_ORDERING.sortedCopy(entities);
	}
}
