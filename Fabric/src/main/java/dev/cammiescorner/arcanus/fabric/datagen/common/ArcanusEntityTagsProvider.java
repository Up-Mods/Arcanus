package dev.cammiescorner.arcanus.fabric.datagen.common;

import dev.cammiescorner.arcanus.fabric.common.data.ArcanusEntityTags;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusEntities;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class ArcanusEntityTagsProvider extends FabricTagProvider.EntityTypeTagProvider {

	public ArcanusEntityTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider arg) {

		getOrCreateTagBuilder(ArcanusEntityTags.C_IMMOVABLE)
				.add(ArcanusEntities.AOE.get())
				.add(ArcanusEntities.BEAM.get())
				.add(ArcanusEntities.MANA_SHIELD.get())
				.add(ArcanusEntities.PORTAL.get())
				.add(ArcanusEntities.SMITE.get());

		getOrCreateTagBuilder(ArcanusEntityTags.DISPELLABLE)
				.add(ArcanusEntities.AGGRESSORB.get())
				.add(ArcanusEntities.AOE.get())
				.add(ArcanusEntities.GUARDIAN_ORB.get())
				.add(ArcanusEntities.MAGIC_RUNE.get())
				.add(ArcanusEntities.MANA_SHIELD.get())
				.add(ArcanusEntities.NECRO_SKELETON.get())
				.add(ArcanusEntities.PORTAL.get());

		getOrCreateTagBuilder(ArcanusEntityTags.SPATIAL_RIFT_IMMUNE)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.addOptionalTag(ArcanusEntityTags.C_IMMOVABLE)
				.add(EntityType.AREA_EFFECT_CLOUD)
				.add(EntityType.BLOCK_DISPLAY)
				.add(EntityType.END_CRYSTAL)
				.add(EntityType.EVOKER_FANGS)
				.add(EntityType.FISHING_BOBBER)
				.add(EntityType.GLOW_ITEM_FRAME)
				.add(EntityType.INTERACTION)
				.add(EntityType.ITEM_DISPLAY)
				.add(EntityType.ITEM_FRAME)
				.add(EntityType.LEASH_KNOT)
				.add(EntityType.LIGHTNING_BOLT)
				.add(EntityType.MARKER)
				.add(EntityType.PAINTING)
				.add(EntityType.TEXT_DISPLAY)
				.add(ArcanusEntities.PORTAL.get());

		getOrCreateTagBuilder(ArcanusEntityTags.RUNE_TRIGGER_IGNORED)
				.addOptionalTag(ArcanusEntityTags.C_IMMOVABLE);
	}
}
