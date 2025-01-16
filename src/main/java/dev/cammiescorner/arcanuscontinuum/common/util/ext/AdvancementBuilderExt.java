package dev.cammiescorner.arcanuscontinuum.common.util.ext;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public interface AdvancementBuilderExt {

	default Advancement save(Consumer<Advancement> consumer, ResourceLocation id) {
		throw new UnsupportedOperationException();
	}
}
