package net.smileycorp.atlas.integration;

import mezz.jei.api.*;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipeWrapper;
import net.smileycorp.atlas.api.recipe.RecipeTool;

import javax.annotation.Nonnull;

@JEIPlugin
public class JEIIntegration implements IModPlugin {

    @Override
    public void register(@Nonnull IModRegistry registry) {
        IJeiHelpers helpers = registry.getJeiHelpers();
        registry.handleRecipes(RecipeTool.Shaped.class, recipe -> new ShapedRecipeToolWrapper(helpers, recipe), VanillaRecipeCategoryUid.CRAFTING);
        registry.handleRecipes(RecipeTool.Shapeless.class, recipe -> new ShapelessRecipeWrapper<>(helpers, recipe), VanillaRecipeCategoryUid.CRAFTING);
    }

}
