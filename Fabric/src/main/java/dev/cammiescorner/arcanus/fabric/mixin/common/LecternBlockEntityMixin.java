package dev.cammiescorner.arcanus.fabric.mixin.common;

import dev.cammiescorner.arcanus.fabric.common.items.SpellBookItem;
import dev.cammiescorner.arcanus.fabric.common.screens.SpellcraftScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LecternBlockEntity.class)
public abstract class LecternBlockEntityMixin extends BlockEntity implements Clearable, MenuProvider, ExtendedScreenHandlerFactory {
	@Shadow @Final private Container bookAccess;

	@Shadow public abstract ItemStack getBook();

	public LecternBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	@Inject(method = "createMenu", at = @At("HEAD"), cancellable = true)
	private void arcanus$createSpellcraftScreen(int i, Inventory playerInventory, Player playerEntity, CallbackInfoReturnable<AbstractContainerMenu> info) {
		if(getBook().getItem() instanceof SpellBookItem)
			info.setReturnValue(new SpellcraftScreenHandler(i, bookAccess, ContainerLevelAccess.create(getLevel(), getBlockPos()), getBlockPos(), getBook()));
	}

	@Override
	public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
		if(getBook().getItem() instanceof SpellBookItem) {
			buf.writeBlockPos(getBlockPos());
			buf.writeItem(getBook());
		}
	}
}
