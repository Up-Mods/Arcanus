package dev.cammiescorner.arcanus.fabric.common.compat.patchouli;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import dev.cammiescorner.arcanus.fabric.common.compat.ArcanusCompat;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.common.book.BookRegistry;
import vazkii.patchouli.common.item.PatchouliItems;
import vazkii.patchouli.common.recipe.ShapelessBookRecipe;

import java.util.function.Consumer;

public class ShapelessBookRecipeBuilder extends ShapelessRecipeBuilder {

	static {
		ArcanusCompat.PATCHOULI.orThrow();
	}

	private final ResourceLocation bookId;

	private ShapelessBookRecipeBuilder(RecipeCategory category, ResourceLocation bookId) {
		super(category, PatchouliItems.BOOK, 1);
		this.bookId = bookId;
	}

	public static ShapelessBookRecipeBuilder book(RecipeCategory category, ResourceLocation bookId) {
		Preconditions.checkArgument(BookRegistry.INSTANCE.books.containsKey(bookId), "No book registered with ID '%s'", bookId);
		return new ShapelessBookRecipeBuilder(category, bookId);
	}

	@Override
	public void save(Consumer<FinishedRecipe> finishedRecipeConsumer) {
		this.save(finishedRecipeConsumer, this.bookId);
	}

	@Override
	public void save(Consumer<FinishedRecipe> writer, ResourceLocation recipeId) {
		Consumer<FinishedRecipe> dummy = delegate -> writer.accept(new FinishedRecipe() {
			@Override
			public void serializeRecipeData(JsonObject json) {
				delegate.serializeRecipeData(json);
				json.addProperty("book", bookId.toString());
			}

			@Override
			public ResourceLocation getId() {
				return delegate.getId();
			}

			@Override
			public RecipeSerializer<?> getType() {
				return ShapelessBookRecipe.SERIALIZER;
			}

			@Override
			public @Nullable JsonObject serializeAdvancement() {
				return delegate.serializeAdvancement();
			}

			@Override
			public @Nullable ResourceLocation getAdvancementId() {
				return delegate.getAdvancementId();
			}
		});
		super.save(dummy, recipeId);
	}
}
