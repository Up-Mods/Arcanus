package dev.cammiescorner.arcanuscontinuum.common.util;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.resources.ResourceLocation;

public enum WorkbenchMode {
	SPELLBINDING(Arcanus.id("textures/gui/arcane_workbench_spellbinding.png")), CUSTOMIZE(Arcanus.id("textures/gui/arcane_workbench_customize.png"));

	final ResourceLocation texture;

	WorkbenchMode(ResourceLocation texture) {
		this.texture = texture;
	}

	public ResourceLocation getTexture() {
		return texture;
	}
}
