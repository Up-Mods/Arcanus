package dev.cammiescorner.arcanuscontinuum.common.screens;

import dev.cammiescorner.arcanuscontinuum.common.items.StaffItem;
import dev.cammiescorner.arcanuscontinuum.common.packets.s2c.SyncStaffTemplatePacket;
import dev.cammiescorner.arcanuscontinuum.common.packets.s2c.SyncWorkbenchModePacket;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlocks;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusScreenHandlers;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.cammiescorner.arcanuscontinuum.common.util.WorkbenchMode;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.WizardData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.Registries;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class ArcaneWorkbenchScreenHandler extends AbstractRecipeScreenHandler<CraftingInventory> {
	private final CraftingResultInventory result = new CraftingResultInventory();
	private final PlayerInventory playerInventory;
	private final ScreenHandlerContext context;
	private final PlayerEntity player;
	private final List<StaffItem> templates;
	private CraftingInventory input;
	private WorkbenchMode mode;
	private Item template = Items.AIR;

	public ArcaneWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
	}

	public ArcaneWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(ArcanusScreenHandlers.ARCANE_WORKBENCH_SCREEN_HANDLER.get(), syncId);
		this.playerInventory = playerInventory;
		this.mode = WorkbenchMode.CUSTOMIZE;
		this.context = context;
		this.player = playerInventory.player;
		getSlotsForMode(mode);
		var isSupporter = player.datasync$getEntitlements().keys().contains(WizardData.ID);
		templates = Registries.ITEM.stream().filter(item -> item instanceof StaffItem).map(StaffItem.class::cast).filter(staffItem -> !staffItem.isDonorOnly || isSupporter).toList();
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		if (id == 0) {
			switch (getMode()) {
				case CUSTOMIZE -> setMode(WorkbenchMode.SPELLBINDING);
				case SPELLBINDING -> setMode(WorkbenchMode.CUSTOMIZE);
			}

			if (player instanceof ServerPlayerEntity serverPlayer)
				SyncWorkbenchModePacket.send(serverPlayer, getMode());
		}

		if (template instanceof StaffItem staff && templates.contains(staff) && (id == 1 || id == 2)) {
			int index = templates.indexOf(staff);

			if (id == 1) {
				index -= 1;

				if (index < 0)
					index = templates.size() - 1;
			}

			if (id == 2) {
				index += 1;

				if (index > templates.size() - 1)
					index = 0;
			}

			setTemplate(templates.get(index));

			if (player instanceof ServerPlayerEntity serverPlayer)
				SyncStaffTemplatePacket.send(serverPlayer, getTemplate());
		}

		return super.onButtonClick(player, id);
	}

	@Override
	public ItemStack quickTransfer(PlayerEntity player, int fromIndex) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = slots.get(fromIndex);
		int maxSlots = switch (mode) {
			case CUSTOMIZE -> 4;
			case SPELLBINDING -> 10;
		};

		if (slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();

			if (fromIndex == 0) {
				context.run((world, pos) -> itemStack2.getItem().onCraft(itemStack2, world, player));

				if (!insertItem(itemStack2, maxSlots, maxSlots + 36, true))
					return ItemStack.EMPTY;

				slot.onQuickTransfer(itemStack2, itemStack);
			} else if (fromIndex >= maxSlots && fromIndex < maxSlots + 36) {
				if (!insertItem(itemStack2, 1, maxSlots, false)) {
					if (fromIndex < maxSlots + 27) {
						if (!insertItem(itemStack2, maxSlots + 27, maxSlots + 36, false))
							return ItemStack.EMPTY;
					} else if (!insertItem(itemStack2, maxSlots, maxSlots + 27, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!insertItem(itemStack2, maxSlots, maxSlots + 36, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty())
				slot.setStack(ItemStack.EMPTY);
			else
				slot.markDirty();

			if (itemStack2.getCount() == itemStack.getCount())
				return ItemStack.EMPTY;

			slot.onTakeItem(player, itemStack2);

			if (fromIndex == 0)
				player.dropItem(itemStack2, false);
		}

		return itemStack;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return canUse(context, player, ArcanusBlocks.ARCANE_WORKBENCH.get());
	}

	public PlayerInventory getPlayerInventory() {
		return playerInventory;
	}

	public ScreenHandlerContext getContext() {
		return context;
	}

	public WorkbenchMode getMode() {
		return mode;
	}

	public void setMode(WorkbenchMode mode) {
		this.mode = mode;
		context.run((world, pos) -> dropInventory(player, input));
		result.clear();
		getSlotsForMode(mode);
	}

	public void getSlotsForMode(WorkbenchMode mode) {
		slots.clear();

		switch (mode) {
			case SPELLBINDING -> {
				input = new CraftingInventory(this, 3, 3);

				addSlot(new CraftingResultSlot(player, input, result, 0, 135, 31));

				for (int i = 0; i < 3; ++i)
					for (int j = 0; j < 3; ++j)
						addSlot(new Slot(input, j + i * 3, 24 + j * 20, 11 + i * 20));
			}
			case CUSTOMIZE -> {
				input = new CraftingInventory(this, 3, 1);

				addSlot(new CraftingResultSlot(player, input, result, 0, 136, 35) {
					@Override
					public void onTakeItem(PlayerEntity player, ItemStack stack) {
						for (int i = 0; i < input.size(); i++)
							input.getStack(i).decrement(1);

						if (player instanceof ServerPlayerEntity serverPlayer)
							SyncStaffTemplatePacket.send(serverPlayer, Items.AIR);
					}
				});

				addSlot(new Slot(input, 0, 54, 35) {
					@Override
					public void setStack(ItemStack stack) {
						super.setStack(stack);
						setTemplate(stack.getItem());
					}

					@Override
					public boolean canInsert(ItemStack stack) {
						return stack.getItem() instanceof StaffItem;
					}
				});
				addSlot(new Slot(input, 1, 95, 24) {
					@Override
					public boolean canInsert(ItemStack stack) {
						return stack.getItem() instanceof DyeItem;
					}
				});
				addSlot(new Slot(input, 2, 95, 46) {
					@Override
					public boolean canInsert(ItemStack stack) {
						return stack.getItem() instanceof DyeItem;
					}
				});
			}
		}

		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for (int i = 0; i < 9; ++i)
			addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
	}

	protected static void updateResult(ArcaneWorkbenchScreenHandler handler, World world, PlayerEntity player, CraftingInventory input, CraftingResultInventory result) {
		if (!world.isClient() && player instanceof ServerPlayerEntity serverPlayer) {
			ItemStack itemStack = ItemStack.EMPTY;

			if (handler.getMode() == WorkbenchMode.SPELLBINDING) {
				Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, input, world);

				if (optional.isPresent()) {
					CraftingRecipe craftingRecipe = optional.get();

					if (result.shouldCraftRecipe(world, serverPlayer, craftingRecipe)) {
						ItemStack itemStack2 = craftingRecipe.craft(input, world.getRegistryManager());

						if (itemStack2.isEnabled(world.getEnabledFlags()))
							itemStack = itemStack2;
					}
				}
			} else {
				ItemStack staffStack = input.getStack(0);

				if (staffStack.getItem() instanceof StaffItem && handler.getTemplate() instanceof StaffItem) {
					ItemStack itemStack2 = new ItemStack(handler.getTemplate(), staffStack.getCount());
					itemStack2.setNbt(staffStack.copy().getNbt());

					if (input.getStack(1).getItem() instanceof DyeItem dye) {
						StaffItem.setPrimaryColor(itemStack2, Color.fromInt(dye.getColor().getFireworkColor(), Color.Ordering.RGB));
					}

					if (input.getStack(2).getItem() instanceof DyeItem dye) {
						int color = dye.getColor().getSignColor();

						if (dye.getColor() == DyeColor.BLACK) {
							color = dye.getColor().getFireworkColor();
						}

						StaffItem.setSecondaryColor(itemStack2, Color.fromInt(color, Color.Ordering.RGB));
					}

					if (!ItemStack.itemsMatch(itemStack2, staffStack) || !StaffItem.getPrimaryColor(itemStack2).equals(StaffItem.getPrimaryColor(staffStack)) || !StaffItem.getSecondaryColor(itemStack2).equals(StaffItem.getSecondaryColor(staffStack))) {
						itemStack = itemStack2;
					}
				}
			}

			result.setStack(0, itemStack);
			handler.setPreviousTrackedSlot(0, itemStack);
			serverPlayer.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
		}
	}

	@Override
	public void onContentChanged(Inventory inventory) {
		context.run((world, pos) -> updateResult(this, world, player, input, result));
	}

	@Override
	public void close(PlayerEntity player) {
		super.close(player);
		context.run((world, pos) -> dropInventory(player, input));
	}

	@Override
	public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
		return slot.inventory != result && super.canInsertIntoSlot(stack, slot);
	}

	@Override
	public void populateRecipeFinder(RecipeMatcher finder) {
		input.provideRecipeInputs(finder);
	}

	@Override
	public void clearCraftingSlots() {
		input.clear();
		result.clear();
	}

	@Override
	public boolean matches(Recipe<? super CraftingInventory> recipe) {
		if (mode == WorkbenchMode.SPELLBINDING)
			return recipe.matches(input, player.getWorld());
		else
			return true;
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return 0;
	}

	@Override
	public int getCraftingWidth() {
		return input.getWidth();
	}

	@Override
	public int getCraftingHeight() {
		return input.getHeight();
	}

	@Override
	public int getCraftingSlotCount() {
		return input.size() + 1;
	}

	@Override
	public RecipeBookCategory getCategory() {
		return RecipeBookCategory.CRAFTING;
	}

	@Override
	public boolean canInsertIntoSlot(int index) {
		return index != this.getCraftingResultSlotIndex();
	}

	public Item getTemplate() {
		return template;
	}

	public void setTemplate(Item template) {
		this.template = template;
		context.run((world, pos) -> updateResult(this, world, player, input, result));
	}
}
