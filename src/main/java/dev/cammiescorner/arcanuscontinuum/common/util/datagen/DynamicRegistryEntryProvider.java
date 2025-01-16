package dev.cammiescorner.arcanuscontinuum.common.util.datagen;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class DynamicRegistryEntryProvider {

	private static RegistrySetBuilder fabricBuilderHack;

	protected abstract void generate(RegistrySetBuilder builder);

	public static EntriesProvider.Builder builder() {
		return new EntriesProvider.Builder();
	}

	public static FabricDynamicRegistryProvider getGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		return new FabricDynamicRegistryProvider(output, registriesFuture) {
			@Override
			protected void configure(HolderLookup.Provider registries, Entries entries) {
				if(fabricBuilderHack != null) {
					fabricBuilderHack.getEntries().stream()
						.map(RegistrySetBuilder.RegistryStub::key)
						.distinct()
						.map(registries::lookupOrThrow)
						.forEach(entries::addAll);
				}
			}

			@Override
			public String getName() {
				return Arcanus.id("dynamic_registries").toString();
			}
		};
	}

	public static class EntriesProvider {

		public static class Builder {

			private final List<Supplier<DynamicRegistryEntryProvider>> providers = new ArrayList<>();

			private Builder() {
			}

			public Builder add(Supplier<DynamicRegistryEntryProvider> provider) {
				providers.add(provider);
				return this;
			}

			public void build(RegistrySetBuilder registrySetBuilder) {
				fabricBuilderHack = registrySetBuilder;
				for(var provider : providers) {
					provider.get().generate(registrySetBuilder);
				}
			}
		}
	}
}
