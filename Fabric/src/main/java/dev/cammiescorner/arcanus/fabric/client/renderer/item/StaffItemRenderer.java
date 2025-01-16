package dev.cammiescorner.arcanus.fabric.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Set;

public class StaffItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer, SimpleSynchronousResourceReloadListener {
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
	public ResourceLocation getFabricId() {
		return this.id;
	}

	@Override
	public Collection<ResourceLocation> getFabricDependencies() {
		return Set.of(ResourceReloadListenerKeys.MODELS);
	}

	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		final Minecraft client = Minecraft.getInstance();
		itemRenderer = client.getItemRenderer();
		inventoryItemModel = client.getModelManager().getModel(new ModelResourceLocation(itemId.withSuffix("_gui"), "inventory"));
		worldItemModel = client.getModelManager().getModel(new ModelResourceLocation(itemId.withSuffix("_handheld"), "inventory"));
	}

	@Override
	public void render(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		matrices.popPose();
		matrices.pushPose();

		switch(mode) {
			case FIRST_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND ->
				itemRenderer.render(stack, mode, true, matrices, vertexConsumers, light, overlay, worldItemModel);
			case FIRST_PERSON_RIGHT_HAND, THIRD_PERSON_RIGHT_HAND ->
				itemRenderer.render(stack, mode, false, matrices, vertexConsumers, light, overlay, worldItemModel);
			default ->
				itemRenderer.render(stack, mode, false, matrices, vertexConsumers, light, overlay, inventoryItemModel);
		}
	}
}
