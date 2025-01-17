package dev.cammiescorner.arcanuscontinuum.mixin.datagen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.resources.RegistryDataLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.stream.Stream;

@Mixin(FabricDynamicRegistryProvider.Entries.class)
public class FabricDynamicRegistryProviderEntriesMixin {
	@WrapOperation(method = "<init>", at = @At(
		value = "INVOKE",
		target = "Ljava/util/List;stream()Ljava/util/stream/Stream;"
	))
	private Stream<RegistryDataLoader.RegistryData<?>> arcanuscontinuum$wrapStream(List<RegistryDataLoader.RegistryData<?>> instance, Operation<Stream<RegistryDataLoader.RegistryData<?>>> original) {
		return Stream.concat(original.call(instance), RegistryDataLoader.DIMENSION_REGISTRIES.stream());
	}
}
