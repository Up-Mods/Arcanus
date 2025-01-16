package dev.cammiescorner.arcanus;

import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.resources.ResourceLocation;

public class Arcanus implements MainEntryPoint {
	public static final String MOD_ID = "arcanus";

	@Override
	public void onInitialize(ModContainer mod) {

	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
