package dev.cammiescorner.arcanuscontinuum.common.blocks.entities;

import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlockEntities;
import dev.cammiescorner.arcanuscontinuum.common.screens.ArcaneWorkbenchScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ArcaneWorkbenchBlockEntity extends BlockEntity implements MenuProvider {
	public ArcaneWorkbenchBlockEntity(BlockPos pos, BlockState state) {
		super(ArcanusBlockEntities.ARCANE_WORKBENCH.get(), pos, state);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable(getBlockState().getBlock().getDescriptionId());
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
		return new ArcaneWorkbenchScreenHandler(i, playerInventory, ContainerLevelAccess.create(getLevel(), getBlockPos()));
	}
}
