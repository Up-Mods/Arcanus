package dev.cammiescorner.arcanus.fabric.mixin.datagen;

import dev.cammiescorner.arcanus.fabric.common.util.ext.RegistrySetBuilderExt;
import net.minecraft.core.RegistrySetBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(RegistrySetBuilder.class)
public abstract class RegistrySetBuilderMixin implements RegistrySetBuilderExt {
	@Override
	@Accessor("entries")
	public abstract List<RegistrySetBuilder.RegistryStub<?>> getEntries();
}
