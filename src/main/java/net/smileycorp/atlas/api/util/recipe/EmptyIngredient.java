package net.smileycorp.atlas.api.util.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class EmptyIngredient extends Ingredient {

	@Override
	public boolean test(ItemStack stack) {
		return stack.isEmpty();
	}

}
