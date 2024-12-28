package dev.cammiescorner.arcanuscontinuum.common.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class ConventionsHelper {

	public static final String COMMON_NAMESPACE = "c";

	public static ResourceLocation id(String path) {
		return new ResourceLocation(COMMON_NAMESPACE, path);
	}

	public static <T> TagKey<T> tag(ResourceKey<Registry<T>> registry, String path) {
		return TagKey.create(registry, id(path));
	}
}
