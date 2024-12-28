package dev.cammiescorner.arcanuscontinuum.datagen.common;

import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusEntityTags;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusEntities;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ArcanusEntityTagsProvider extends FabricTagProvider.EntityTypeTagProvider {

	public ArcanusEntityTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider arg) {
		getOrCreateTagBuilder(ArcanusEntityTags.DISPELLABLE)
			.add(ArcanusEntities.AGGRESSORB.get())
			.add(ArcanusEntities.AOE.get())
			.add(ArcanusEntities.GUARDIAN_ORB.get())
			.add(ArcanusEntities.MAGIC_RUNE.get())
			.add(ArcanusEntities.MANA_SHIELD.get())
			.add(ArcanusEntities.NECRO_SKELETON.get())
			.add(ArcanusEntities.PORTAL.get());
	}
}
