package net.smileycorp.atlas.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class OreIngredientReadable extends OreIngredient {
	
	protected final String ore;

	public OreIngredientReadable(String ore) {
		super(ore);
		this.ore = ore;
	}
	
	public String getOreName() {
		return ore;
	}

	public static OreIngredientReadable from(OreIngredient ingredient) {
		for (int id : OreDictionary.getOreIDs(ingredient.getMatchingStacks()[0])) {
			String ore = OreDictionary.getOreName(id);
			for (ItemStack stack : OreDictionary.getOres(ore)){
				if (!ingredient.apply(stack)) continue;
				return new OreIngredientReadable(ore);
			}
		}
		return new OreIngredientReadable("");
	}

}
