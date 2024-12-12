package dev.cammiescorner.arcanuscontinuum.common.recipes;

import com.google.common.collect.Lists;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.spells.Spell;
import dev.cammiescorner.arcanuscontinuum.common.items.SpellBookItem;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusItems;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusRecipes;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusTags;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.CraftingCategory;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

//TODO use tag for spell books
public class SpellBindingRecipe extends SpecialCraftingRecipe {
	public SpellBindingRecipe(Identifier identifier, CraftingCategory category) {
		super(identifier, category);
	}

	private static final int[] INDICES = new int[]{7, 0, 1, 6, 0, 2, 5, 4, 3};

	@Override
	public DefaultedList<ItemStack> getRemainder(RecipeInputInventory inventory) {
		DefaultedList<ItemStack> list = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);

		for (int i = 0; i < list.size(); ++i) {
			if (inventory.getStack(i).getItem() instanceof SpellBookItem) {
				list.set(i, inventory.getStack(i).copy());
			}
		}

		return list;
	}

	@Override
	public boolean matches(RecipeInputInventory inv, World world) {
		List<ItemStack> list = Lists.newArrayList();
		ItemStack result = ItemStack.EMPTY;

		for (int i = 0; i < inv.size(); ++i) {
			ItemStack stack = inv.getStack(i);

			if (!stack.isEmpty()) {
				if(stack.isIn(ArcanusTags.STAVES)) {
					if (i != 4) {
						return false;
					}

					result = stack.copy();
				}
				else if(stack.isOf(ArcanusItems.SPELL_BOOK.get())) {
					list.add(stack);
				}
				else {
					return false;
				}
			}
		}

		return !result.isEmpty() && !list.isEmpty();
	}


	@Override
	public ItemStack craft(RecipeInputInventory inv, DynamicRegistryManager manager) {
		ItemStack result = inv.getStack(4).copy();

		if (!result.isIn(ArcanusTags.STAVES)) {
			return ItemStack.EMPTY;
		}

		NbtCompound tag = result.getOrCreateSubNbt(Arcanus.MOD_ID);
		NbtList list = tag.getList("Spells", NbtElement.COMPOUND_TYPE);

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

		for (int i = 0; i < inv.size(); i++) {
			if (i == 4) {
				continue;
			}

			ItemStack stack = inv.getStack(i);
			if (stack.isOf(ArcanusItems.SPELL_BOOK.get())) {
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
	public boolean fits(int width, int height) {
		// need the exact size because of the shape
		return width == 3 && height == 3;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ArcanusRecipes.SPELL_BINDING.get();
	}
}
