package dev.cammiescorner.arcanus.fabric.mixin.common;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.LecternMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MenuType.class)
public class ScreenHandlerTypeMixin {
	@Inject(method = "register(Ljava/lang/String;Lnet/minecraft/world/inventory/MenuType$MenuSupplier;)Lnet/minecraft/world/inventory/MenuType;", at = @At("HEAD"), cancellable = true)
	private static void arcanus$register(String id, MenuType.MenuSupplier<AbstractContainerMenu> factory, CallbackInfoReturnable<MenuType<AbstractContainerMenu>> info) {
		if("lectern".equals(id))
			info.setReturnValue(Registry.register(BuiltInRegistries.MENU, id, new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> new LecternMenu(syncId))));
	}
}
