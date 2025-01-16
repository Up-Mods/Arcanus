package dev.cammiescorner.arcanuscontinuum.common.util.ext;

import dev.cammiescorner.arcanuscontinuum.common.compat.patchouli.ShapelessBookRecipeBuilder;
import net.minecraft.resources.ResourceLocation;

public interface ShapelessRecipeBuilderExt {

	default ShapelessBookRecipeBuilder asPatchouliBook(ResourceLocation bookId) {
		throw new UnsupportedOperationException();
	}
}
