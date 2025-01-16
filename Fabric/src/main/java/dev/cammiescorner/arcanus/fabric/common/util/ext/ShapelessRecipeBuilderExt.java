package dev.cammiescorner.arcanus.fabric.common.util.ext;

import dev.cammiescorner.arcanus.fabric.common.compat.patchouli.ShapelessBookRecipeBuilder;
import net.minecraft.resources.ResourceLocation;

public interface ShapelessRecipeBuilderExt {

	default ShapelessBookRecipeBuilder asPatchouliBook(ResourceLocation bookId) {
		throw new UnsupportedOperationException();
	}
}
