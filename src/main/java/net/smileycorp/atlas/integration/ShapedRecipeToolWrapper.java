package net.smileycorp.atlas.integration;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipeWrapper;
import net.smileycorp.atlas.api.recipe.RecipeTool;

public class ShapedRecipeToolWrapper extends ShapelessRecipeWrapper<RecipeTool.Shaped> implements IShapedCraftingRecipeWrapper {

    public ShapedRecipeToolWrapper(IJeiHelpers jeiHelpers, RecipeTool.Shaped recipe) {
        super(jeiHelpers, recipe);
    }

    @Override
    public int getWidth() {
        return recipe.getRecipeWidth();
    }

    @Override
    public int getHeight() {
        return recipe.getRecipeHeight();
    }

}
