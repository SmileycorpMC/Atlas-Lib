package net.smileycorp.atlas.api.util.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

/** Represents an {@link}OreDictionary input, with the added effect of
 * containing an amount of the ore to use, rather than just one */
public class OreStack {

    private String ore;

    private int count;

    public OreStack(String ore) {
        this(ore, 1);
    }

    public OreStack(String ore, int count) {
        this.ore = ore;
        this.count = count;
    }

    public NonNullList<ItemStack> getOres() {
        NonNullList<ItemStack> results = OreDictionary.getOres(ore);
        for(ItemStack result : results) {
            result.setCount(count);
        }
        return results;
    }

	public int getCount() {
		return count;
	}
}
