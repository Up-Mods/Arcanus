package dev.cammiescorner.arcanuscontinuum.common.registry;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.recipes.SpellBindingRecipe;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ArcanusRecipes {

	public static final RegistryHandler<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistryHandler.create(Registries.RECIPE_SERIALIZER, Arcanus.MOD_ID);
	public static final RegistrySupplier<SimpleCraftingRecipeSerializer<SpellBindingRecipe>> SPELL_BINDING = RECIPE_SERIALIZERS.register("spell_binding_recipe", () -> new SimpleCraftingRecipeSerializer<>(SpellBindingRecipe::new));

}
