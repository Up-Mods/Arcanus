package dev.cammiescorner.arcanus.fabric.common.util;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.resources.ResourceLocation;

public enum WorkbenchMode {
	SPELLBINDING(FabricMain.id("textures/gui/arcane_workbench_spellbinding.png")), CUSTOMIZE(FabricMain.id("textures/gui/arcane_workbench_customize.png"));

	final ResourceLocation texture;

	WorkbenchMode(ResourceLocation texture) {
		this.texture = texture;
	}

	public ResourceLocation getTexture() {
		return texture;
	}
}
