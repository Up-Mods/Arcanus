package dev.cammiescorner.arcanuscontinuum.common.util.ext;

import net.minecraft.core.RegistrySetBuilder;

import java.util.List;

public interface RegistrySetBuilderExt {

	default List<RegistrySetBuilder.RegistryStub<?>> getEntries() {
		throw new UnsupportedOperationException();
	}
}
