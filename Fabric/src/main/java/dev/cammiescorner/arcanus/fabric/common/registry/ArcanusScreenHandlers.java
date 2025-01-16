package dev.cammiescorner.arcanus.fabric.common.registry;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.cammiescorner.arcanus.fabric.common.screens.ArcaneWorkbenchScreenHandler;
import dev.cammiescorner.arcanus.fabric.common.screens.SpellBookScreenHandler;
import dev.cammiescorner.arcanus.fabric.common.screens.SpellcraftScreenHandler;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ArcanusScreenHandlers {

	public static final RegistryHandler<MenuType<?>> SCREEN_HANDLERS = RegistryHandler.create(Registries.MENU, Arcanus.MOD_ID);

	public static final RegistrySupplier<MenuType<SpellcraftScreenHandler>> SPELLCRAFT_SCREEN_HANDLER = SCREEN_HANDLERS.register("spellcraft_screen_handler", () -> new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> new SpellcraftScreenHandler(syncId, inventory, buf.readBlockPos(), buf.readItem())));
	public static final RegistrySupplier<MenuType<SpellBookScreenHandler>> SPELL_BOOK_SCREEN_HANDLER = SCREEN_HANDLERS.register("spell_book_screen_handler", () -> new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> new SpellBookScreenHandler(syncId, inventory, buf.readItem())));
	public static final RegistrySupplier<MenuType<ArcaneWorkbenchScreenHandler>> ARCANE_WORKBENCH_SCREEN_HANDLER = SCREEN_HANDLERS.register("arcane_workbench_screen_handler", () -> new MenuType<>(ArcaneWorkbenchScreenHandler::new, FeatureFlags.VANILLA_SET));
}
