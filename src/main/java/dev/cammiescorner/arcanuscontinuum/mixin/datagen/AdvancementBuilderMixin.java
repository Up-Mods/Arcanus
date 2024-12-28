package dev.cammiescorner.arcanuscontinuum.mixin.datagen;

import dev.cammiescorner.arcanuscontinuum.common.util.ext.AdvancementBuilderExt;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(Advancement.Builder.class)
public abstract class AdvancementBuilderMixin implements AdvancementBuilderExt {

	@Shadow
	public abstract Advancement save(Consumer<Advancement> consumer, String id);

	@Override
	public Advancement save(Consumer<Advancement> consumer, ResourceLocation id) {
		return save(consumer, id.toString());
	}
}
