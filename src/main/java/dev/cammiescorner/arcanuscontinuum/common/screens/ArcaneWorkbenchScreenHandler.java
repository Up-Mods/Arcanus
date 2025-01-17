package dev.cammiescorner.arcanuscontinuum.common.screens;

import dev.cammiescorner.arcanuscontinuum.common.items.StaffItem;
import dev.cammiescorner.arcanuscontinuum.common.packets.s2c.SyncStaffTemplatePacket;
import dev.cammiescorner.arcanuscontinuum.common.packets.s2c.SyncWorkbenchModePacket;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlocks;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusScreenHandlers;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.cammiescorner.arcanuscontinuum.common.util.WorkbenchMode;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.WizardData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class ArcaneWorkbenchScreenHandler extends RecipeBookMenu<TransientCraftingContainer> {
	private final ResultContainer result = new ResultContainer();
	private final Inventory playerInventory;
	private final ContainerLevelAccess context;
	private final Player player;
	private final List<StaffItem> templates;
	private TransientCraftingContainer input;
	private WorkbenchMode mode;
	private Item template = Items.AIR;

	public ArcaneWorkbenchScreenHandler(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, ContainerLevelAccess.NULL);
	}

	public ArcaneWorkbenchScreenHandler(int syncId, Inventory playerInventory, ContainerLevelAccess context) {
		super(ArcanusScreenHandlers.ARCANE_WORKBENCH_SCREEN_HANDLER.get(), syncId);
		this.playerInventory = playerInventory;
		this.mode = WorkbenchMode.CUSTOMIZE;
		this.context = context;
		this.player = playerInventory.player;
		getSlotsForMode(mode);
		var isSupporter = player.datasync$getEntitlements().keys().contains(WizardData.ID);
		templates = BuiltInRegistries.ITEM.stream().filter(item -> item instanceof StaffItem).map(StaffItem.class::cast).filter(staffItem -> !staffItem.isDonorOnly || isSupporter).toList();
	}

	@Override
	public boolean clickMenuButton(Player player, int id) {
		if(id == 0) {
			switch(getMode()) {
				case CUSTOMIZE -> setMode(WorkbenchMode.SPELLBINDING);
				case SPELLBINDING -> setMode(WorkbenchMode.CUSTOMIZE);
			}

			if(player instanceof ServerPlayer serverPlayer)
				SyncWorkbenchModePacket.send(serverPlayer, getMode());
		}

		if(template instanceof StaffItem staff && templates.contains(staff) && (id == 1 || id == 2)) {
			int index = templates.indexOf(staff);

			if(id == 1) {
				index -= 1;

				if(index < 0)
					index = templates.size() - 1;
			}

			if(id == 2) {
				index += 1;

				if(index > templates.size() - 1)
					index = 0;
			}

			setTemplate(templates.get(index));

			if(player instanceof ServerPlayer serverPlayer)
				SyncStaffTemplatePacket.send(serverPlayer, getTemplate());
		}

		return super.clickMenuButton(player, id);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int fromIndex) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = slots.get(fromIndex);
		int maxSlots = switch(mode) {
			case CUSTOMIZE -> 4;
			case SPELLBINDING -> 10;
		};

		if(slot.hasItem()) {
			ItemStack itemStack2 = slot.getItem();
			itemStack = itemStack2.copy();

			if(fromIndex == 0) {
				context.execute((world, pos) -> itemStack2.getItem().onCraftedBy(itemStack2, world, player));

				if(!moveItemStackTo(itemStack2, maxSlots, maxSlots + 36, true))
					return ItemStack.EMPTY;

				slot.onQuickCraft(itemStack2, itemStack);
			}
			else if(fromIndex >= maxSlots && fromIndex < maxSlots + 36) {
				if(!moveItemStackTo(itemStack2, 1, maxSlots, false)) {
					if(fromIndex < maxSlots + 27) {
						if(!moveItemStackTo(itemStack2, maxSlots + 27, maxSlots + 36, false))
							return ItemStack.EMPTY;
					}
					else if(!moveItemStackTo(itemStack2, maxSlots, maxSlots + 27, false)) {
						return ItemStack.EMPTY;
					}
				}
			}
			else if(!moveItemStackTo(itemStack2, maxSlots, maxSlots + 36, false)) {
				return ItemStack.EMPTY;
			}

			if(itemStack2.isEmpty())
				slot.set(ItemStack.EMPTY);
			else
				slot.setChanged();

			if(itemStack2.getCount() == itemStack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(player, itemStack2);

			if(fromIndex == 0)
				player.drop(itemStack2, false);
		}

		return itemStack;
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(context, player, ArcanusBlocks.ARCANE_WORKBENCH.get());
	}

	public Inventory getPlayerInventory() {
		return playerInventory;
	}

	public ContainerLevelAccess getContext() {
		return context;
	}

	public WorkbenchMode getMode() {
		return mode;
	}

	public void setMode(WorkbenchMode mode) {
		this.mode = mode;
		context.execute((world, pos) -> clearContainer(player, input));
		result.clearContent();
		getSlotsForMode(mode);
	}

	public void getSlotsForMode(WorkbenchMode mode) {
		slots.clear();

		switch(mode) {
			case SPELLBINDING -> {
				input = new TransientCraftingContainer(this, 3, 3);

				addSlot(new ResultSlot(player, input, result, 0, 135, 31));

				for(int i = 0; i < 3; ++i)
					for(int j = 0; j < 3; ++j)
						addSlot(new Slot(input, j + i * 3, 24 + j * 20, 11 + i * 20));
			}
			case CUSTOMIZE -> {
				input = new TransientCraftingContainer(this, 3, 1);

				addSlot(new ResultSlot(player, input, result, 0, 136, 35) {
					@Override
					public void onTake(Player player, ItemStack stack) {
						for(int i = 0; i < input.getContainerSize(); i++)
							input.getItem(i).shrink(1);

						if(player instanceof ServerPlayer serverPlayer)
							SyncStaffTemplatePacket.send(serverPlayer, Items.AIR);
					}
				});

				addSlot(new Slot(input, 0, 54, 35) {
					@Override
					public void set(ItemStack stack) {
						super.set(stack);
						setTemplate(stack.getItem());
					}

					@Override
					public boolean mayPlace(ItemStack stack) {
						return stack.getItem() instanceof StaffItem;
					}
				});
				addSlot(new Slot(input, 1, 95, 24) {
					@Override
					public boolean mayPlace(ItemStack stack) {
						return stack.getItem() instanceof DyeItem;
					}
				});
				addSlot(new Slot(input, 2, 95, 46) {
					@Override
					public boolean mayPlace(ItemStack stack) {
						return stack.getItem() instanceof DyeItem;
					}
				});
			}
		}

		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 9; ++j)
				addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for(int i = 0; i < 9; ++i)
			addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
	}

	protected static void updateResult(ArcaneWorkbenchScreenHandler handler, Level world, Player player, TransientCraftingContainer input, ResultContainer result) {
		if(!world.isClientSide() && player instanceof ServerPlayer serverPlayer) {
			ItemStack itemStack = ItemStack.EMPTY;

			if(handler.getMode() == WorkbenchMode.SPELLBINDING) {
				Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, world);

				if(optional.isPresent()) {
					CraftingRecipe craftingRecipe = optional.get();

					if(result.setRecipeUsed(world, serverPlayer, craftingRecipe)) {
						ItemStack itemStack2 = craftingRecipe.assemble(input, world.registryAccess());

						if(itemStack2.isItemEnabled(world.enabledFeatures()))
							itemStack = itemStack2;
					}
				}
			}
			else {
				ItemStack staffStack = input.getItem(0);

				if(staffStack.getItem() instanceof StaffItem && handler.getTemplate() instanceof StaffItem) {
					ItemStack itemStack2 = new ItemStack(handler.getTemplate(), staffStack.getCount());
					itemStack2.setTag(staffStack.copy().getTag());

					if(input.getItem(1).getItem() instanceof DyeItem dye) {
						StaffItem.setPrimaryColor(itemStack2, Color.fromInt(dye.getDyeColor().getFireworkColor(), Color.Ordering.RGB));
					}

					if(input.getItem(2).getItem() instanceof DyeItem dye) {
						int color = dye.getDyeColor().getTextColor();

						if(dye.getDyeColor() == DyeColor.BLACK) {
							color = dye.getDyeColor().getFireworkColor();
						}

						StaffItem.setSecondaryColor(itemStack2, Color.fromInt(color, Color.Ordering.RGB));
					}

					if(!ItemStack.isSameItem(itemStack2, staffStack) || !StaffItem.getPrimaryColor(itemStack2).equals(StaffItem.getPrimaryColor(staffStack)) || !StaffItem.getSecondaryColor(itemStack2).equals(StaffItem.getSecondaryColor(staffStack))) {
						itemStack = itemStack2;
					}
				}
			}

			result.setItem(0, itemStack);
			handler.setRemoteSlot(0, itemStack);
			serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(handler.containerId, handler.incrementStateId(), 0, itemStack));
		}
	}

	@Override
	public void slotsChanged(Container inventory) {
		context.execute((world, pos) -> updateResult(this, world, player, input, result));
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		context.execute((world, pos) -> clearContainer(player, input));
	}

	@Override
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
		return slot.container != result && super.canTakeItemForPickAll(stack, slot);
	}

	@Override
	public void fillCraftSlotsStackedContents(StackedContents finder) {
		input.fillStackedContents(finder);
	}

	@Override
	public void clearCraftingContent() {
		input.clearContent();
		result.clearContent();
	}

	@Override
	public boolean recipeMatches(Recipe<? super TransientCraftingContainer> recipe) {
		if(mode == WorkbenchMode.SPELLBINDING)
			return recipe.matches(input, player.level());
		else
			return true;
	}

	@Override
	public int getResultSlotIndex() {
		return 0;
	}

	@Override
	public int getGridWidth() {
		return input.getWidth();
	}

	@Override
	public int getGridHeight() {
		return input.getHeight();
	}

	@Override
	public int getSize() {
		return input.getContainerSize() + 1;
	}

	@Override
	public RecipeBookType getRecipeBookType() {
		return RecipeBookType.CRAFTING;
	}

	@Override
	public boolean shouldMoveToInventory(int index) {
		return index != this.getResultSlotIndex();
	}

	public Item getTemplate() {
		return template;
	}

	public void setTemplate(Item template) {
		this.template = template;
		context.execute((world, pos) -> updateResult(this, world, player, input, result));
	}
}
