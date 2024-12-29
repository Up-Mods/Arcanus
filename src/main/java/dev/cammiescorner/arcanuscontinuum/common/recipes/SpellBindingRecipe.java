package dev.cammiescorner.arcanuscontinuum.common.recipes;

import com.google.common.collect.Lists;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.spells.Spell;
import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusItemTags;
import dev.cammiescorner.arcanuscontinuum.common.items.SpellBookItem;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

public class SpellBindingRecipe extends CustomRecipe {
	public SpellBindingRecipe(ResourceLocation identifier, CraftingBookCategory category) {
		super(identifier, category);
	}

	private static final int[] INDICES = new int[]{7, 0, 1, 6, 0, 2, 5, 4, 3};

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inventory) {
		NonNullList<ItemStack> list = NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);

		for (int i = 0; i < list.size(); ++i) {
			ItemStack stack = inventory.getItem(i);
			if (stack.is(ArcanusItemTags.CRAFTING_SPELLBINDING_SPELLBOOKS)) {
				list.set(i, stack.copy());
			}
		}

		return list;
	}

	@Override
	public boolean matches(CraftingContainer inv, Level world) {
		List<ItemStack> spellBooks = Lists.newArrayList();
		ItemStack result = ItemStack.EMPTY;

		for (int i = 0; i < inv.getContainerSize(); ++i) {
			ItemStack stack = inv.getItem(i);

			if (!stack.isEmpty()) {
				if(stack.is(ArcanusItemTags.STAVES)) {
					if (i != 4) {
						return false;
					}

					result = stack.copy();
				}
				else if(stack.is(ArcanusItemTags.CRAFTING_SPELLBINDING_SPELLBOOKS)) {
					spellBooks.add(stack);
				}
				else {
					return false;
				}
			}
		}

		return !result.isEmpty() && !spellBooks.isEmpty();
	}


	@Override
	public ItemStack assemble(CraftingContainer inv, RegistryAccess manager) {
		ItemStack result = inv.getItem(4).copy();

		if (!result.is(ArcanusItemTags.STAVES)) {
			return ItemStack.EMPTY;
		}

		CompoundTag tag = result.getOrCreateTagElement(Arcanus.MOD_ID);
		ListTag list = tag.getList("Spells", Tag.TAG_COMPOUND);

		var spells = new Spell[8];
		Arrays.fill(spells, new Spell());

		for (int i = 0; i < list.size(); i++) {
			try {
				spells[i] = Spell.fromNbt(list.getCompound(i));
			} catch (Exception e) {
				Arcanus.LOGGER.error("Failed to load spell from NBT", e);
			}
		}

		int count = 0;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			if (i == 4) {
				continue;
			}

			ItemStack stack = inv.getItem(i);
			if (stack.is(ArcanusItemTags.CRAFTING_SPELLBINDING_SPELLBOOKS)) {
				spells[INDICES[i]] = SpellBookItem.getSpell(stack);
				count++;
			}
		}

		if(count == 0 || result.isEmpty()) {
			return ItemStack.EMPTY;
		}

		list.clear();
		for (Spell spell : spells) {
			list.add(spell.toNbt());
		}
		tag.put("Spells", list);

		return result;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		// need the exact size because of the shape
		return width == 3 && height == 3;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ArcanusRecipes.SPELL_BINDING.get();
	}
}
