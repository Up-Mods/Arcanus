package dev.cammiescorner.arcanuscontinuum.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class StaffItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer, IdentifiableResourceReloader {
	private final ResourceLocation id;
	private final ResourceLocation itemId;
	private ItemRenderer itemRenderer;
	private BakedModel inventoryItemModel;
	private BakedModel worldItemModel;

	public StaffItemRenderer(ResourceLocation itemId) {
		this.id = new ResourceLocation(itemId.getNamespace(), itemId.getPath() + "_renderer");
		this.itemId = itemId;
	}

	@Override
	public ResourceLocation getQuiltId() {
		return this.id;
	}

	@Override
	public CompletableFuture<Void> reload(PreparationBarrier synchronizer, ResourceManager manager, ProfilerFiller prepareProfiler, ProfilerFiller applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		return synchronizer.wait(Unit.INSTANCE).thenRunAsync(() -> {
			applyProfiler.startTick();
			applyProfiler.push("listener");
			final Minecraft client = Minecraft.getInstance();
			itemRenderer = client.getItemRenderer();
			inventoryItemModel = client.getModelManager().getModel(new ModelResourceLocation(itemId.withPath(itemId.getPath() + "_gui"), "inventory"));
			worldItemModel = client.getModelManager().getModel(new ModelResourceLocation(itemId.withPath(itemId.getPath() + "_handheld"), "inventory"));
			applyProfiler.pop();
			applyProfiler.endTick();
		}, applyExecutor);
	}

	@Override
	public void render(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		matrices.popPose();
		matrices.pushPose();

		if(mode != ItemDisplayContext.FIRST_PERSON_LEFT_HAND && mode != ItemDisplayContext.FIRST_PERSON_RIGHT_HAND && mode != ItemDisplayContext.THIRD_PERSON_LEFT_HAND && mode != ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
			itemRenderer.render(stack, mode, false, matrices, vertexConsumers, light, overlay, inventoryItemModel);
		}
		else {
			boolean leftHanded;

			switch(mode) {
				case FIRST_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND -> leftHanded = true;
				default -> leftHanded = false;
			}

			itemRenderer.render(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, worldItemModel);
		}
	}
}
