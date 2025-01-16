package dev.cammiescorner.arcanus.fabric.common.compat;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.world.item.ItemStack;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliCompat {

	public static ItemStack getCompendiumArcanus() {
		return PatchouliAPI.get().getBookStack(FabricMain.id("compendium_arcanus"));
	}

}
