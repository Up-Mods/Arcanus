package dev.cammiescorner.arcanuscontinuum.common.screens;

import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusScreenHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class SpellcraftScreenHandler extends AbstractContainerMenu {
	private final Container inventory;
	private final ContainerLevelAccess context;
	private final ItemStack stack;
	private final BlockPos pos;

	public SpellcraftScreenHandler(int syncId, Container inventory, BlockPos pos, ItemStack stack) {
		this(syncId, inventory, ContainerLevelAccess.NULL, pos, stack);
	}

	public SpellcraftScreenHandler(int syncId, Container inventory, ContainerLevelAccess context, BlockPos pos, ItemStack stack) {
		super(ArcanusScreenHandlers.SPELLCRAFT_SCREEN_HANDLER.get(), syncId);
		this.inventory = inventory;
		this.stack = stack;
		this.pos = pos;
		this.context = context;
	}

	@Override
	public boolean clickMenuButton(Player player, int id) {
		if(!player.mayBuild())
			return false;

		ItemStack itemStack = inventory.removeItemNoUpdate(0);
		inventory.setChanged();

		if(!player.getInventory().add(itemStack))
			player.drop(itemStack, false);

		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int fromIndex) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	public BlockPos getPos() {
		return pos;
	}

	public ItemStack getSpellBook() {
		return stack;
	}

	public ContainerLevelAccess getContext() {
		return context;
	}
}
