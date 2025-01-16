package dev.cammiescorner.arcanus.fabric.common.registry;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class ArcanusPointsOfInterest {
	public static final ResourceKey<PoiType> MAGIC_DOOR = create("magic_door");

	public static void register() {
		PointOfInterestHelper.register(MAGIC_DOOR.location(), 0, 1, ArcanusBlocks.MAGIC_DOOR.get());
	}

	public static ResourceKey<PoiType> create(String id) {
		return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, FabricMain.id(id));
	}
}
