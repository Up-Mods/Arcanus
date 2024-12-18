package dev.cammiescorner.arcanuscontinuum.common.screens;

import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusScreenHandlers;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class SpellBookScreenHandler extends AbstractContainerMenu {
	private final ItemStack stack;

	public SpellBookScreenHandler(int syncId, Container inventory, ItemStack stack) {
		super(ArcanusScreenHandlers.SPELL_BOOK_SCREEN_HANDLER.get(), syncId);
		this.stack = stack;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int fromIndex) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	public ItemStack getSpellBook() {
		return stack;
	}
}
