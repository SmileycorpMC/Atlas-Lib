package net.smileycorp.atlas.api.util.recipe;

import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.util.RecipeUtils;

/** An OreInput represents a recipe input that can either be an {@link}ItemStack
 * or {@link}OreStack, this makes storing recipes in Arrays, Lists and Maps
 * easier */
public class OreInput {
    private OreStack ore;

    private ItemStack stack;

    public OreInput(ItemStack stack) {
        this.stack = stack;
    }

    public OreInput(OreStack ore) {
        this.ore = ore;
    }

    public boolean doesStackMatch(ItemStack input) {
        if (input != ItemStack.EMPTY && input != null) {
            if (ore != null) {
                for(ItemStack ostack : ore.getOres()) {
                    if (RecipeUtils.compareItemStacksWithSize(input, ostack)) {
                        return true;
                    }
                }
            } else if (stack != null) {
                if (RecipeUtils.compareItemStacksWithSize(input, stack)) {
                    return true;
                }
            }
        }
        return false;
    }

}
